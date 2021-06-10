package com.haizhi.databridge.service.impl;

import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_CRONTAB;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_DELTA;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerTiming.TIMING_TYPE_MINUTE;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_CRONTAB;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_DELTA;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_MINUTE;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_ORIGIN;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncCycle.SYNC_CYCLE_STOP;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.TSchedulerBean;
import com.haizhi.databridge.bean.domain.TTableBean;
import com.haizhi.databridge.bean.dto.DataSchedulerDto;
import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.constants.DataSourceConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.TSchedulerRepository;
import com.haizhi.databridge.service.DataSchedulerService;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.result.StatusCode;

@Service
@Log4j2
public class DataSchedulerServiceImpl extends RequestCommonData implements DataSchedulerService {

	@Autowired
	private DataTableServiceImpl tableServiceImpl;

	@Autowired
	private TSchedulerRepository tSchedulerRepo;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<DataSchedulerVo.RetrieveVo> retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException {
		List<DataSchedulerVo.RetrieveVo> retrieveVos = new ArrayList<>();
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				retrieveForm.getSchedulerId(), retrieveForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			return retrieveVos;
		}
		DataSchedulerDto.OptionsDto optionsDto = JsonUtils.toObject(
				optionalTSchedulerBean.get().getOptions(), DataSchedulerDto.OptionsDto.class);
		if (!ObjectUtils.isEmpty(optionsDto.getTables())) {
			return retrieveVos;

		}
		for (String tableId : optionsDto.getTables()) {
			DataTableVo.RetrieveVo tableVo = tableServiceImpl.retrieveTable(tableId, retrieveForm.getUserId());
			retrieveVos.add(DataSchedulerVo.RetrieveVo.builder().tableId(tableVo.getTableId())
					.schedulerId(retrieveForm.getSchedulerId())
					.schema(tableServiceImpl.getSchemafromRef(tableVo.getRef()))
					.build()
			);
		}
		return retrieveVos;
	}

	//	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(DataSchedulerForm.UpdateForm updateForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				updateForm.getSchedulerId(), updateForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("任务%s不存在", updateForm.getSchedulerId()));
		}
		// 变更定时设置
		TSchedulerBean tSchedulerBean = optionalTSchedulerBean.get();
		if (!ObjectUtils.isEmpty(updateForm.getTiming())) {
			tSchedulerBean.setTiming(JsonUtils.toJson(updateForm.getTiming()));
		}
		// 变更schedulerName
		if (!ObjectUtils.isEmpty(updateForm.getSchedulerName())) {

			if (!updateForm.getSchedulerName().equals(tSchedulerBean.getSchedulerName())) {
				if (tSchedulerRepo.findBySchedulerNameAndOwner(updateForm.getSchedulerName(), updateForm.getUserId()).isPresent()) {
					throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
							String.format("任务%s已存在", updateForm.getSchedulerName()));
				} else {
					tSchedulerBean.setSchedulerName(updateForm.getSchedulerName());
				}
			}
		}

		// 变更scheduler_desc
		if (!ObjectUtils.isEmpty(updateForm.getSchedulerDesc())) {
			tSchedulerBean.setSchedulerDesc(updateForm.getSchedulerDesc());
		}

		// 处理表勾选操作
		DataSchedulerDto.OptionsDto optionsDto = JsonUtils.toObject(optionalTSchedulerBean.get().getOptions(),
				DataSchedulerDto.OptionsDto.class);
		for (String tableId : optionsDto.getTables()) {
			if (!updateForm.getTables().contains(tableId)) {
				tableServiceImpl.delete(tableId, updateForm.getUserId());
			}
			tSchedulerBean.setSchedulerId(null);
		}
		for (String tableId : updateForm.getTables()) {
			DataTableVo.RetrieveVo retrieveVo = tableServiceImpl.retrieveTable(tableId, updateForm.getUserId());
			if (ObjectUtils.isEmpty(retrieveVo)) {
				throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("表%s不存在", tableId));
			}
			if (!ObjectUtils.isEmpty(retrieveVo.getSchedulerId())
					&& !tSchedulerBean.getSchedulerId().equals(retrieveVo.getSchedulerId())) {
				if (tSchedulerRepo.findBySchedulerIdAndOwner(retrieveVo.getSchedulerId(), updateForm.getUserId()).isPresent()) {
					throw new DatabridgeException(StatusCode.SOURCE_EXISTS,
							String.format("表%s已存在任务%s中", tableId, retrieveVo.getSchedulerId()));
				}
				tableServiceImpl.updateSchedulerId(tableId, updateForm.getUserId(), updateForm.getSchedulerId());
			}
			optionsDto.setTables(updateForm.getTables());
			tSchedulerBean.setOptions(JsonUtils.toJson(optionsDto));
			tSchedulerRepo.save(tSchedulerBean);
		}
	}

	public void delete(DataSchedulerForm.DeleteForm deleteForm) {
		Optional<TSchedulerBean> optionalTSchedulerBean = tSchedulerRepo.findBySchedulerIdAndOwner(
				deleteForm.getSchedulerId(), deleteForm.getUserId());
		if (!optionalTSchedulerBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, String.format("任务%s不存在", deleteForm.getSchedulerId()));
		}
		tableServiceImpl.cleanSchedulerId(deleteForm.getUserId(), deleteForm.getSchedulerId());
		tSchedulerRepo.logicDeleteBySchedulerId(deleteForm.getSchedulerId());
	}

	public DataSchedulerVo.ListVo list(DataSchedulerForm.ListForm listForm) throws UnsupportedEncodingException {
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
			Optional<List<TSchedulerBean>> optionalSchedulerList = tSchedulerRepo.findTSchedulerByOwner(
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
			String owner, List<TTableBean> tableBeans, List<TSchedulerBean> schedulerBeans) throws UnsupportedEncodingException {
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
		if (ObjectUtils.isEmpty(timing)) {
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

	private Integer buildSchedulerCount(String owner) {
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
}
