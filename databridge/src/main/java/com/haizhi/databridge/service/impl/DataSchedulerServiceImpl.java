package com.haizhi.databridge.service.impl;

import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_CRONTAB;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_DELTA;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_MINUTE;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_ORIGIN;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerType.CRON;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_CRONTAB;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_DELTA;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_MINUTE;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_ORIGIN;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_STOP;
import static com.haizhi.databridge.constants.DataSourceConstants.TaskType.IMPORT;
import static com.haizhi.databridge.service.impl.DataTableServiceImpl.getSchemafromRef;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;
import com.haizhi.databridge.bean.domain.importdata.TSchedulerBean;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;
import com.haizhi.databridge.bean.dto.DataSchedulerDto;
import com.haizhi.databridge.bean.dto.DataSourceObjDto;
import com.haizhi.databridge.bean.dto.DataTableDto;
import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.client.xxljob.JobClientApi;
import com.haizhi.databridge.client.xxljob.request.DataTransJobParam;
import com.haizhi.databridge.config.DmcClientProperties;
import com.haizhi.databridge.constants.DataSourceConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.importdata.TSchedulerRepository;
import com.haizhi.databridge.repository.importdata.TTableRepository;
import com.haizhi.databridge.repository.importdata.TdataBaseSourceRepository;
import com.haizhi.databridge.service.DataSchedulerService;
import com.haizhi.databridge.util.IdUtils;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.util.SpringUtils;
import com.haizhi.databridge.util.ZLibUtils;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;
import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataFieldResp;
import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;

@Service
@Log4j2
public class DataSchedulerServiceImpl extends RequestCommonData implements DataSchedulerService {

	@Autowired
	private DataTableServiceImpl tableServiceImpl;

	@Autowired
	private TSchedulerRepository tSchedulerRepo;

	@Autowired
	private DmcClientProperties dmcProp;

	@Autowired
	private TTableRepository tTableRepo;

	@Autowired
	private TdataBaseSourceRepository databaseRepo;

	@Autowired
	private JobClientApi jobClientApi;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public DataSchedulerVo.RetrieveVo retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException {
//		List<DataSchedulerVo.RetrieveVo> retrieveVos = new ArrayList<>();
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				retrieveForm.getSchedulerId(), retrieveForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			return null;
		}
		DataSchedulerDto.OptionsDto optionsDto = JsonUtils.toObject(
				optionalTSchedulerBean.get().getOptions(), DataSchedulerDto.OptionsDto.class);
		if (ObjectUtils.isEmpty(optionsDto.getTables())) {
			return null;

		}
		List<DataTableVo.RetrieveVo> tableRetrievesVo = new ArrayList<>();
		for (String tableId : optionsDto.getTables()) {
			DataTableVo.RetrieveVo tableVo = tableServiceImpl.retrieveTable(tableId, retrieveForm.getUserId());
			tableRetrievesVo.add(tableVo);
		}
		TSchedulerBean tSchedulerBean = optionalTSchedulerBean.get();
		Timestamp bastTimeStamp = tSchedulerBean.getCreateAt().after(tSchedulerBean.getCreateAt())
				? tSchedulerBean.getCreateAt() : tSchedulerBean.getCreateAt();
		Date bastDt = new Date(bastTimeStamp.getTime());

		String nextTime = "";
		if (!ObjectUtils.isEmpty(getNextTime(JsonUtils.toObject(tSchedulerBean.getTiming(),
				DataSchedulerDto.TimingDto.class), bastDt))) {
			nextTime = DateFormatUtils.format(getNextTime(JsonUtils.toObject(tSchedulerBean.getTiming(),
					DataSchedulerDto.TimingDto.class), bastDt), "yyyy-MM-dd HH:mm:ss");
		}
		return DataSchedulerVo.RetrieveVo.builder()
				.schedulerId(retrieveForm.getSchedulerId())
				.nextTime(nextTime)
				.status(tSchedulerBean.getStatus())
				.tables(tableRetrievesVo)
				.timing(JsonUtils.toObject(tSchedulerBean.getTiming(), DataSchedulerDto.TimingDto.class))
				.build();
	}

	//	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(DataSchedulerForm.UpdateForm updateForm) throws UnsupportedEncodingException {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				updateForm.getSchedulerId(), updateForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("任务%s不存在", updateForm.getSchedulerId()));
		}
		updateScheduler(updateForm, optionalTSchedulerBean.get());
	}

	public void delete(DataSchedulerForm.DeleteForm deleteForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				deleteForm.getSchedulerId(), deleteForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("任务%s不存在", deleteForm.getSchedulerId()));
		}
		tableServiceImpl.cleanSchedulerId(deleteForm.getUserId(), deleteForm.getSchedulerId());
		tSchedulerRepo.logicDeleteBySchedulerId(deleteForm.getSchedulerId());
		jobClientApi.remove(optionalTSchedulerBean.get().getSchedulerId());
	}

	public DataSchedulerVo.ListVo list(DataSchedulerForm.ListForm listForm) throws IOException {
		// 不管是搜索还是全量，总的思想就是先拿到该拿到的scheduler和table，然后构建返回结果
		List<TSchedulerBean> querySchedulerBeanResult = new ArrayList<>();
		List<TTableBean> queryTableBeanResult = new ArrayList<>();
		int pageCount;
		Integer total;

		if (ObjectUtils.isEmpty(listForm.getKeyword())) {
			// 获取最大值
			// 分页操作
			//limit查询scheduler
			//查table

			total = buildSchedulerCount(listForm.getUserId());
			Integer pageSize = listForm.getLimit() > total ? total : listForm.getLimit();
			// mysql查询是从0开始的
			Integer startNum = (listForm.getPage() - 1) * pageSize;
			Integer endNum = listForm.getPage() * pageSize - 1;
			Optional<List<TSchedulerBean>> optionalSchedulerList = tSchedulerRepo.findSchedulerByOwner(
					listForm.getUserId(), startNum, endNum);
			// 通过查询到的scheduler拿到schedulerId获取table
			if (optionalSchedulerList.isPresent()) {
				querySchedulerBeanResult = optionalSchedulerList.get();
				queryTableBeanResult = getTableBeanBySchedulerIds(listForm.getUserId(), querySchedulerBeanResult
						.stream().map(TSchedulerBean::getSchedulerId).collect(Collectors.toList()));
			}

		} else {
			// 根本思想就是，如果搜索命中scheduler_name， 那么需要把scheduler下边的表都查出来，
			// 如果命中tb_name,那么需要把对应的scheduler查出来

			// 通过搜索scheduler拿到命中的schedulerBean，添加到querySchedulerBeanResult中，然后再通过schedulerId查询scheduler下边的表，并添加到queryTableBeanResult
			Optional<List<TSchedulerBean>> optionalTSchedulerBeans = tSchedulerRepo.findTSchedulerByOwnerAndSchedulerNameLike(
					listForm.getUserId(), listForm.getKeyword());
			if (optionalTSchedulerBeans.isPresent()) {
				querySchedulerBeanResult.addAll(optionalTSchedulerBeans.get());

				List<String> schedulerIds = optionalTSchedulerBeans.get().stream().map(
						TSchedulerBean::getSchedulerId).collect(Collectors.toList());
				if (!ObjectUtils.isEmpty(schedulerIds)) {
					queryTableBeanResult.addAll(
							tableServiceImpl.getTableBeanListBySchedulerIds(listForm.getUserId(), schedulerIds));
				}
			}

			// // 通过搜索table拿到命中的tableBean，添加到queryTableBeanResult中，然后再通过table中的schedulerIds查询scheduler下，并添加到queryTableBeanResult
			List<TTableBean> tTableBeans = tableServiceImpl.getTableListByOwnerAndTbNameLike(listForm.getUserId(), listForm.getKeyword());
			if (!ObjectUtils.isEmpty(tTableBeans)) {
				queryTableBeanResult.addAll(tTableBeans);
				queryTableBeanResult.addAll(getTableBeanBySchedulerIds(listForm.getUserId(),
						tTableBeans.stream().map(TTableBean::getSchedulerId).collect(Collectors.toList())));
			}
			//table去重
			queryTableBeanResult = queryTableBeanResult.stream().distinct().collect(Collectors.toList());
//			// scheduler去重
			querySchedulerBeanResult = sortAndDistinctSchedulerBean(querySchedulerBeanResult);
			total = querySchedulerBeanResult.size();
			// 搜索先全量吧，理论上来说需要根据limt对querySchedulerBeanResult进行截取
		}

		pageCount = (int) Math.ceil((double) total / listForm.getLimit());
		Map<String, DataSchedulerVo.SchedulerVo> schedulerVoMap = buildSchedulerId2Map(
				listForm.getUserId(), queryTableBeanResult, querySchedulerBeanResult);
		return DataSchedulerVo.ListVo.builder()
				.pagecount(pageCount)
				.query(buildQueryVo(listForm.getKeyword(), listForm.getLimit(), listForm.getPage()))
				.schedulers(querySchedulerBeanResult.stream().map(TSchedulerBean::getSchedulerId)
						.map(schedulerVoMap::get).collect(Collectors.toList()))
				.status(DataSchedulerVo.StatusVo.builder().total(total).build())
				.totalitems(total).build();
	}

	public Map<String, DataSchedulerVo.SchedulerVo> buildSchedulerId2Map(
			String owner, List<TTableBean> tableBeans, List<TSchedulerBean> schedulerBeans) throws IOException {
		Map<String, DataTableVo.TableVo> tableVoMap = tableServiceImpl.buildTableId2TableVoMap(owner, tableBeans);
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 构建scheduler
		Map<String, DataSchedulerVo.SchedulerVo> schedulerId2SchedulerVoMap = new HashMap<>();
		for (TSchedulerBean schedulerBean: schedulerBeans) {
			DataSchedulerDto.OptionsDto optionsDto = JsonUtils.toObject(schedulerBean.getOptions(), DataSchedulerDto.OptionsDto.class);
			schedulerId2SchedulerVoMap.put(schedulerBean.getSchedulerId(), DataSchedulerVo.SchedulerVo.builder()
					.database(optionsDto.getTables().stream().map(tableId -> tableVoMap.get(tableId).getDbType())
							.distinct().collect(Collectors.toList()))
					.exception(schedulerBean.getException())
					.posted(optionsDto.getTables().stream().map(tableVoMap::get).mapToInt(DataTableVo.TableVo::getPosted).sum())
					.fetched(optionsDto.getTables().stream().map(tableVoMap::get).mapToInt(DataTableVo.TableVo::getFetched).sum())
					.finishTbCount((int) optionsDto.getTables().stream().map(tableVoMap::get).filter(tableVo ->
							tableVo.getStatus().equals(DataSourceConstants.DataTableStatus.FINISHED)
									|| tableVo.getStatus().equals(DataSourceConstants.DataTableStatus.STATUS_NEW)
									|| tableVo.getStatus().equals(
									DataSourceConstants.DataTableStatus.STATUS_IGNORED)).count())
					.schedulerDesc(schedulerBean.getSchedulerDesc())
					.schedulerId(schedulerBean.getSchedulerId())
					.schedulerName(schedulerBean.getSchedulerName())
					.startAt(ft.format(schedulerBean.getStartAt()))
					.status(schedulerBean.getStatus())
					.syncCycle(getSyncCycle(schedulerBean.getTiming()))
					.tables(optionsDto.getTables().stream().map(tableVoMap::get)
							.sorted(Comparator.comparing(DataTableVo.TableVo::getStartAt)).collect(Collectors.toList()))
					.tbCount(optionsDto.getTables().size())
					.build()
			);
		}
		return schedulerId2SchedulerVoMap;
	}


	public DataSchedulerVo.QueryVo buildQueryVo(String keyword, Integer limit, Integer page) {
		return DataSchedulerVo.QueryVo.builder()
				.keyword(keyword)
				.limit(limit)
				.page(page)
				.build();
	}

	private String getSyncCycle(String timing) {
		if (ObjectUtils.isEmpty(timing) || "null".equals(timing)) {
			return SYNC_CYCLE_STOP;
		}
		DataSchedulerDto.TimingDto timingDto = JsonUtils.toObject(timing, DataSchedulerDto.TimingDto.class);
		if (!Boolean.TRUE.equals(timingDto.getEnable())) {
			return SYNC_CYCLE_STOP;
		}

		if (timingDto.getType().equals(TIMING_TYPE_DELTA)) {
			return SYNC_CYCLE_DELTA;
		} else if (timingDto.getType().equals(TIMING_TYPE_CRONTAB)) {
			return !ObjectUtils.isEmpty(timingDto.getCrontab()) ? timingDto.getCrontab() : SYNC_CYCLE_CRONTAB;
		} else if (timingDto.getType().equals(TIMING_TYPE_MINUTE)) {
			return String.format("每%s分钟", ObjectUtils.isEmpty(timingDto.getMinute()) ? timingDto.getMinute() : SYNC_CYCLE_MINUTE);
		} else {
			return SYNC_CYCLE_ORIGIN;
		}
	}

	public Integer buildSchedulerCount(String owner) {
		Map<String, BigInteger> schedulerCountMap = tSchedulerRepo.countTSchedulerBeanByOwner(owner);
		Integer schedulerCount = Integer.parseInt(String.valueOf(schedulerCountMap.get("count")));
		return schedulerCount;
	}

	private List<TTableBean> getTableBeanBySchedulerIds(String owner, List<String> schedulerIds) {
		List<TTableBean> result;
		result = tableServiceImpl.getTableBeanListBySchedulerIds(owner, schedulerIds);
		result.sort((o1, o2) -> {
			Timestamp createAt1 = o1.getCreateAt();
			Timestamp createAt2 = o2.getCreateAt();
			return createAt1.compareTo(createAt2);
		});
		return result;
	}
	private List<TSchedulerBean> sortAndDistinctSchedulerBean(List<TSchedulerBean> schedulerBeans) {
		// scheduler去重
		schedulerBeans = schedulerBeans.stream().distinct().collect(Collectors.toList());
		// 根据要显示的scheduler和table进行构建返回结果
		schedulerBeans.sort((o1, o2) -> {
			Timestamp createAt1 = o1.getCreateAt();
			Timestamp createAt2 = o2.getCreateAt();
			return createAt1.compareTo(createAt2);
		});
		return schedulerBeans;
	}

	public Date getNextTime(DataSchedulerDto.TimingDto timingDto, Date baseDt) {
		if (ObjectUtils.isEmpty(timingDto) || ObjectUtils.isEmpty(timingDto.getEnable())) {
			return null;
		}

		if (TIMING_TYPE_ORIGIN.equals(timingDto.getType())) {
			List<Date> timeLineList = getTimeLine(baseDt, timingDto);
			Date minDate = null;
			for (Date t: timeLineList) {
				if (ObjectUtils.isEmpty(minDate)) {
					minDate = t;
				} else if (t.before(minDate)) {
					minDate = t;
				}
			}
			return minDate;
		} else if (TIMING_TYPE_MINUTE.equals(timingDto.getType())) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(baseDt);
			calendar.add(calendar.MINUTE, timingDto.getMinute());
			Date date = calendar.getTime();
			baseDt.setYear(date.getYear());
			baseDt.setMonth(date.getMonth());
			baseDt.setDate(date.getDate());
			baseDt.setMinutes(date.getMinutes());
			return baseDt;
		} else if (TIMING_TYPE_CRONTAB.equals(timingDto.getType())) {
			CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(timingDto.getCrontab());
			Date nextNextTimePoint = cronSequenceGenerator.next(baseDt);
			return nextNextTimePoint;
		} else if (TIMING_TYPE_DELTA.equals(timingDto.getType())) {
			List<Date> timeLineList = getTimeLineBetweenStartAndEndTime(
					baseDt, timingDto.getStart(), timingDto.getEnd(), timingDto.getDelta());
			Date minDate = null;
			for (Date d: timeLineList) {
				if (d.before(minDate)) {
					minDate = d;
				}
			}
			return minDate;

		}

		return null;
	}

	private List<Date> getTimeLine(Date baseDt, DataSchedulerDto.TimingDto timingDto) {
		List<Date> result = new ArrayList<>();
		for (DataSchedulerDto.Origin o: timingDto.getOrigin()) {

			Date nextTime = new Date(baseDt.getYear(), baseDt.getMonth(), baseDt.getDate(),
					Integer.valueOf(o.getHour()), Integer.valueOf(o.getMinute()));
			if (Integer.valueOf(o.getHour()) < baseDt.getHours() || (Integer.valueOf(o.getHour()) == baseDt.getHours()
					&& Integer.valueOf(o.getMinute()) <= baseDt.getMinutes())) {
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(nextTime);
				calendar.add(calendar.DATE, 1);
				Date date = calendar.getTime();
				nextTime.setYear(date.getYear());
				nextTime.setMonth(date.getMonth());
				nextTime.setDate(date.getDate());
			}
			result.add(nextTime);

		}
		return result;
	}


	private List<Date> getTimeLineBetweenStartAndEndTime(Date baseDt, DataSchedulerDto.Origin start, DataSchedulerDto.Origin end, String delta) {
		List<Date> result = new ArrayList<>();

		while (Integer.valueOf(start.getHour()) < Integer.valueOf(end.getHour()) || (start.getHour().equals(end.getHour())
				&& Integer.valueOf(start.getMinute()) <= Integer.valueOf(end.getMinute()))) {
			Date currentDt = new Date(baseDt.getYear(), baseDt.getMonth(), baseDt.getDate(),
					Integer.valueOf(start.getHour()), Integer.valueOf(start.getMinute()));
			if (Integer.valueOf(start.getHour()) < baseDt.getHours() || (start.getHour().equals(baseDt.getHours())
					&& Integer.valueOf(start.getMinute()) <= baseDt.getMinutes())) {
				result.add(currentDt);
				start.setHour(start.getHour() + 1);
			}
		}
		return result;
	}

	@Override
	public DataTransJobVo getJobExecInfo(String jobId) {
		TSchedulerBean tSchedulerBean = tSchedulerRepo.findBySchedulerId(jobId).orElseThrow(() -> new DatabridgeException("jog not exist"));
		String dmcUrl = JsonUtils.toJson(DmcConfig.builder().pentagonProp(dmcProp.getPentagon())
				.noahProp(dmcProp.getNoah()).mobiusProp(dmcProp.getMobius()).tassadarProp(dmcProp.getTassadar()).build());
		List<TTableBean> tableBeans = JsonUtils.toObject(tSchedulerBean.getOptions(), DataSchedulerDto.OptionsDto.class)
				.getTables().stream().map(tableId -> tTableRepo.findByTableId(tableId).orElse(null)).collect(Collectors.toList());

		return DataTransJobVo.builder()
				.jobId(jobId)
				.jobType("import")
				.userId(tSchedulerBean.getOwner())
				.syncUnits(tableBeans.stream().map(tableBean -> {
							DataTableDto.SyncConfigDto syncConfig =
									JsonUtils.toObject(
											tableBean.getSyncConfig(), DataTableDto.SyncConfigDto.class);
							return getSyncUnit(syncConfig, tableBean, dmcUrl, tSchedulerBean.getOwner());
						}).collect(Collectors.toList())
				).build();
	}

	private DataTransJobVo.Sync.SyncCondition getSyncCondition(DataTableDto.SyncConfigDto syncConfig) {
		DataTransJobVo.Sync.SyncCondition syncCondition = null;
		if (ObjectUtils.isEmpty(syncConfig.getIncrease())) {
			syncCondition = null;
		} else {
			if ("maximum".equalsIgnoreCase(syncConfig.getIncrease().getType())) {
				syncCondition = DataTransJobVo.Sync.SyncCondition.builder()
						.field(syncConfig.getIncrease().getField())
						.start(DataTransJobVo.Sync.SyncCondition.Conditon.builder()
								.operator(syncConfig.getIncrease().getMaximum().getStart().getCompare())
								.enable("true".equals(
										syncConfig.getIncrease().getMaximum().getStart().getEnable()) ? 1 : 0)
								.value(syncConfig.getIncrease().getMaximum().getStart().getValue()).build())
						.end(DataTransJobVo.Sync.SyncCondition.Conditon.builder()
								.operator(syncConfig.getIncrease().getMaximum().getEnd().getMode())
								.enable(syncConfig.getIncrease().getMaximum().getEnd().getEnable() ? 1 : 0)
								.value(syncConfig.getIncrease().getMaximum().getEnd().getValue().toString()).build())
						.build();
			} else if ("relativetime".equalsIgnoreCase(syncConfig.getIncrease().getType())) {
				syncCondition = null;
			}
		}

		return syncCondition;
	}

	private DataTransJobVo.Filter getFilter(DataTableDto.SyncConfigDto syncConfig) {
		DataTransJobVo.Filter filter;
		if (syncConfig.getFilter() == null) {
			filter = null;
		} else {
			List<DataTransJobVo.Filter.FilterCondition.Condition> conditions =
					Optional.ofNullable(syncConfig.getFilter().getList()).orElse(new ArrayList<>()).stream()
							.map(con -> DataTransJobVo.Filter.FilterCondition.Condition.builder()
									.name(con.getName())
									.type(con.getType())
									.value(con.getValue())
									.build())
							.collect(Collectors.toList());
			filter = DataTransJobVo.Filter.builder()
					.filterConditions(DataTransJobVo.Filter.FilterCondition.builder()
							.relationType(syncConfig.getFilter().getType())
							.conditions(conditions).build())
					.sqlConditions(Collections.singletonList(syncConfig.getSql())).build();
		}

		return filter;
	}

	private DataTransJobVo.SyncUnit getSyncUnit(DataTableDto.SyncConfigDto syncConfig, TTableBean tableBean,
												String dmcUrl, String userId) {
		TDataBaseSourceBean dbBean = databaseRepo.findByDbId(tableBean.getDbId()).orElse(new TDataBaseSourceBean());
		DataSourceObjDto.SetUp setup = JsonUtils.toObject(dbBean.getSetup(), DataSourceObjDto.SetUp.class);
		List<DataTransJobVo.Column> columns =
				getFromColumns(syncConfig, dbBean, tableBean.getTbName(), syncConfig.getFields(), userId);

		DataTransJobVo.Sink toSink = DataTransJobVo.Sink.builder().url(dmcUrl).type("dmc")
				.schema(dbBean.getDbId()).catalog(dbBean.getDsName()).build();

		DataTransJobVo.Sync.SyncCondition syncCondition = getSyncCondition(syncConfig);

		DataTransJobVo.Filter filter = getFilter(syncConfig);

		String toTablePath = null;
		String toTableId = null;
		if (!ObjectUtils.isEmpty(syncConfig.getOutputRef())) {
			List<String> folderTable = JsonUtils.json2List(base64Decode(syncConfig.getOutputRef(), "+-"));
			toTablePath = folderTable.get(0);
			toTableId = folderTable.get(1);
		}

		List<String> schemaList = new ArrayList<>();
		try {
			schemaList = getSchemafromRef(syncConfig.getRef());
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
		assert schemaList != null;
		return DataTransJobVo.SyncUnit.builder()
				.reader(DataTransJobVo.Reader.builder()
						.columns(columns)
						.sync(DataTransJobVo.Sync.builder().type(syncConfig.getModel()).fetchSize(syncConfig.getRows())
								.syncCondition(syncCondition).build())
						.filter(filter)
						.tableId(tableBean.getTableId())
						.tableName(tableBean.getTbName())
						.build())
				.writer(DataTransJobVo.Writer.builder()
						.columns(columns)
						.tableId(toTableId)
						.tablePath(toTablePath)
						.tableName(tableBean.getTbName())
						.build())
				.toSink(toSink)
				.fromSink(DataTransJobVo.Sink.builder()
						.url(setup.getServer() + ":" + setup.getPort())
						.username(setup.getUid())
						.password(setup.getPwd())
						.type(dbBean.getDbType())
						.catalog(Objects.requireNonNull(schemaList).get(0))
						.build())
				.build();
	}

	public static String base64Encode(byte[] src, String altchars) {
		String base64Str = Base64.getEncoder().encodeToString(src);
		if (altchars.length() < 2) {
			return base64Str;
		}

		base64Str = base64Str.replace('+', altchars.charAt(0));
		base64Str = base64Str.replace('/', altchars.charAt(1));

		return base64Str;
	}

	public static String base64Decode(String src, String altchars) {
		byte[] base64Dest = Base64.getDecoder().decode(src.getBytes(UTF_8));

		String base64Str = new String(base64Dest, 0, base64Dest.length);
		if (altchars.length() < 2) {
			return base64Str;
		}

		base64Str = base64Str.replace(altchars.charAt(0), '+');
		base64Str = base64Str.replace(altchars.charAt(1), '/');



		return base64Str;
	}

	@SneakyThrows
	public List<DataTransJobVo.Column> getFromColumns(DataTableDto.SyncConfigDto syncConfig,
													  TDataBaseSourceBean tDataBaseSourceBean,
													  String tableId,
													  List<String> userConfigFields,
													  String userId) {
		String base64 = Base64.getEncoder()
				.encodeToString((ZLibUtils.compress(tDataBaseSourceBean.getSetup().getBytes(UTF_8))));
		base64 = base64.replace('/', '-');
		String connectId = base64;
		String ref = syncConfig.getRef();

		DmcTableApi client = SpringUtils.getBean(DmcTableApi.class);
		GetTableDataFieldResp tableDataFieldResp = client.getTableDataField(connectId, ref, tableId, userId);
		List<GetTableDataFieldResp.Field> fieldList = tableDataFieldResp.getResult().getFields();

		List<DataTransJobVo.Column> result = new ArrayList<>();

		for (GetTableDataFieldResp.Field field : fieldList) {
			if ("BDP_AUDIT".equalsIgnoreCase(field.getName())) {
				continue;
			}

			if (ObjectUtils.isEmpty(userConfigFields) || userConfigFields.contains(field.getName())) {
				result.add(DataTransJobVo.Column.builder()
						.name(field.getName()).remark(field.getRemark()).uniqIndex(field.isUniq_index())
						.type(field.getType()).build());
			}
		}

		return result;
	}

	@Override
	public String updateJobStatus(String jobId, Integer jobStatus, Long startTime, Long endTime) {
		TSchedulerBean schedulerBean = tSchedulerRepo.findBySchedulerId(jobId)
				.orElseThrow(() -> new DatabridgeException("job not exist"));
		schedulerBean.setStartAt(new Timestamp(startTime));
		if (null != endTime) {
			schedulerBean.setFinishAt(new Timestamp(endTime));
		}

		String status = "";
		switch (jobStatus) {
			case 0: status = "syncing"; break;
			case 1: status = "error"; break;
			case 2: status = "terminated"; break;
			default: break;
		}

		schedulerBean.setStatus(status);
		tSchedulerRepo.update(schedulerBean);

		return null;
	}

	@Override
	public String updateJobExecUnit(JobUnitStateForm form) {
		TTableBean tTableBean = tTableRepo.findByTableId(form.getFromTableId())
				.orElseThrow(() -> new DatabridgeException("table not exist"));
		tTableBean.setStartAt(new Timestamp(form.getStartTime()));
		if (!ObjectUtils.isEmpty(form.getEndTime())) {
			tTableBean.setFinishAt(new Timestamp(form.getEndTime()));
		}

		String outputRef = "";
		if (!StringUtils.isEmpty(form.getToFolderId()) && !StringUtils.isEmpty(form.getToTableId())) {
			outputRef = base64Encode(JsonUtils.toJson(Arrays.asList(form.getToFolderId(), form.getToTableId())).getBytes(UTF_8),
					"+-");
		}

		String status = "";
		switch (form.getTableStatus()) {
			case 0:
				status = "syncing";
				break;
			case 1:
				status = "error";
				break;
			case 2:
				status = "finished";
				break;
			default:
				break;
		}
		tTableBean.setStatus(status);
		DataTableDto.SyncConfigDto syncConfig =
				JsonUtils.toObject(tTableBean.getSyncConfig(), DataTableDto.SyncConfigDto.class);
		if (!StringUtils.isEmpty(outputRef)) {
			syncConfig.setOutputRef(outputRef);
		}

		if (syncConfig.getIncrease() != null) {
			syncConfig.getIncrease().getMaximum().getStart().setValue(form.getStartLocation());
		}

		tTableBean.setSyncConfig(JsonUtils.toJson(syncConfig));

		tTableRepo.update(tTableBean);
		return null;
	}

	public void create(DataSchedulerForm.CreateForm createForm) throws UnsupportedEncodingException {
		if (tSchedulerRepo.findBySchedulerNameAndOwner(createForm.getSchedulerName(), createForm.getUserId()).isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
					String.format("任务%s已存在", createForm.getSchedulerName()));
		}
		TSchedulerBean tSchedulerBean = new TSchedulerBean();
		String schedulerId = IdUtils.genKey("nsched");
		tSchedulerBean.setSchedulerId(schedulerId);
		updateScheduler(createForm, tSchedulerBean);
		String cronExpr = genCrontab(createForm.getTiming());
		DataTransJobParam dataTransJobParam = new DataTransJobParam();
		dataTransJobParam.setJobId(schedulerId);
		dataTransJobParam.setJobType(IMPORT);
		jobClientApi.add(cronExpr, CRON, dataTransJobParam);
		jobClientApi.start(schedulerId);
	}

	private void updateScheduler(DataSchedulerForm.ChangeBaseForm changeBaseForm, TSchedulerBean tSchedulerBean)
			throws UnsupportedEncodingException {

		if (!ObjectUtils.isEmpty(changeBaseForm.getTiming())) {
			tSchedulerBean.setTiming(JsonUtils.toJson(changeBaseForm.getTiming()));
		}
		// 变更schedulerName
		if (!ObjectUtils.isEmpty(changeBaseForm.getSchedulerName())) {

			if (!changeBaseForm.getSchedulerName().equals(tSchedulerBean.getSchedulerName())) {
				if (tSchedulerRepo.findBySchedulerNameAndOwner(changeBaseForm.getSchedulerName(),
						changeBaseForm.getUserId()).isPresent()) {
					throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
							String.format("任务%s已存在", changeBaseForm.getSchedulerName()));
				} else {
					tSchedulerBean.setSchedulerName(changeBaseForm.getSchedulerName());
				}
			}
		}

		// 变更scheduler_desc
		if (!ObjectUtils.isEmpty(changeBaseForm.getSchedulerDesc())) {
			tSchedulerBean.setSchedulerDesc(changeBaseForm.getSchedulerDesc());
		}

		// 处理表勾选操作
		DataSchedulerDto.OptionsDto optionsDto = JsonUtils.toObject(tSchedulerBean.getOptions(),
				DataSchedulerDto.OptionsDto.class);
		if (!ObjectUtils.isEmpty(optionsDto) && !ObjectUtils.isEmpty(optionsDto.getTables())) {
			for (String tableId : optionsDto.getTables()) {
				if (!changeBaseForm.getTables().contains(tableId)) {
					tableServiceImpl.updateSchedulerId(tableId, changeBaseForm.getUserId(), null);
				}
			}
		}

		for (String tableId : changeBaseForm.getTables()) {
			DataTableVo.RetrieveVo retrieveVo = tableServiceImpl.retrieveTable(tableId, changeBaseForm.getUserId());
			if (ObjectUtils.isEmpty(retrieveVo)) {
				continue;
//				throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("表%s不存在", tableId));
			}
			if (!ObjectUtils.isEmpty(retrieveVo.getSchedulerId())
					&& !tSchedulerBean.getSchedulerId().equals(retrieveVo.getSchedulerId())) {
				if (tSchedulerRepo.findBySchedulerIdAndOwner(retrieveVo.getSchedulerId(), changeBaseForm.getUserId()).isPresent()) {
					throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
							String.format("表%s已存在任务%s中", tableId, retrieveVo.getSchedulerId()));
				}
				tableServiceImpl.updateSchedulerId(tableId, changeBaseForm.getUserId(), tSchedulerBean.getSchedulerId());
			}
		}
		optionsDto.setTables(changeBaseForm.getTables());
		tSchedulerBean.setOwner(changeBaseForm.getUserId());
		tSchedulerBean.setOptions(JsonUtils.toJson(optionsDto));
		tSchedulerRepo.save(tSchedulerBean);
		if (!ObjectUtils.isEmpty(tSchedulerBean.getTiming())) {
			updateJob(tSchedulerBean.getSchedulerId(), tSchedulerBean.getTiming());
		}
	}

	private void updateJob(String schedulerId, String timing) {
		DataTransJobParam dataTransJobParam = new DataTransJobParam();
		dataTransJobParam.setJobId(schedulerId);
		dataTransJobParam.setJobType(IMPORT);
		jobClientApi.update(schedulerId,
				genCrontab(JsonUtils.toObject(timing, DataSchedulerDto.TimingDto.class)),
				IMPORT,
				dataTransJobParam
		);
	}

	public String genCrontab(DataSchedulerDto.TimingDto timingDto) {
		DataSchedulerDto.Cron cron = new DataSchedulerDto.Cron();
		if (TIMING_TYPE_DELTA.equals(timingDto.getType())) {
			DataSchedulerDto.Origin start = timingDto.getStart();
			DataSchedulerDto.Origin end = timingDto.getEnd();
			Integer delta = Integer.valueOf(timingDto.getDelta());

			Integer startHour = Integer.valueOf(start.getHour());
			Integer statMinute = Integer.valueOf(start.getMinute());

			Integer endHour = Integer.valueOf(end.getHour());
			Integer endMinute = Integer.valueOf(end.getMinute());

			Integer maxHour = null;

			if (statMinute <= endMinute) {
				maxHour = endHour;
			} else {
				Integer tmp = startHour;
				while (true) {
					tmp	+= delta;
					if (tmp > endHour) {
						break;
					}
					maxHour += delta;
				}
			}
			cron.setMonth("*");
			cron.setDay("*");
			cron.setWeek("*");
			cron.setHour(String.format("%s-%s", startHour.toString(), maxHour.toString()));
			cron.setMinute(statMinute.toString());
		} else if (TIMING_TYPE_ORIGIN.equals(timingDto.getType())) {
			DataSchedulerDto.Origin origin = timingDto.getOrigin().get(0);

			cron.setMonth("*");
			cron.setDay("*");
			cron.setWeek("*");
			cron.setHour(Integer.valueOf(origin.getHour()).toString());
			cron.setMinute(Integer.valueOf(origin.getMinute()).toString());
		} else if (TIMING_TYPE_CRONTAB.equals(timingDto.getType())) {
			return timingDto.getCrontab();
		} else if (TIMING_TYPE_MINUTE.equals(timingDto.getType())) {
			cron.setMinute(Integer.valueOf(timingDto.getMinute()).toString());
		} else {
			throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
					String.format("暂不支持的类型：%s", timingDto.getType()));
		}
		return String.format("%s %s %s %s %s", cron.getMinute(), cron.getHour(), cron.getDay(), cron.getWeek(), cron.getMonth());
	}

	public void trigger(DataSchedulerForm.TriggerForm triggerForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				triggerForm.getSchedulerId(), triggerForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("任务不存在：%s", triggerForm.getSchedulerId()));
		}
		jobClientApi.trigger(triggerForm.getSchedulerId());
	}

	public void start(DataSchedulerForm.StartForm startForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				startForm.getSchedulerId(), startForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("任务不存在：%s", startForm.getSchedulerId()));
		}
		jobClientApi.start(startForm.getSchedulerId());
	}

	public void stop(DataSchedulerForm.StopForm stopForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				stopForm.getSchedulerId(), stopForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("任务不存在：%s", stopForm.getSchedulerId()));
		}
		jobClientApi.stop(stopForm.getSchedulerId());
	}
}
