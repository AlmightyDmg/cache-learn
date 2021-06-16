package com.haizhi.databridge.service.impl;

import static com.haizhi.databridge.constants.DataSourceConstants.SyncType.SYNC_TYPE_FULL;
import static com.haizhi.databridge.constants.DataSourceConstants.SyncType.SYNC_TYPE_INCREASE;
import static com.haizhi.databridge.util.IdUtils.genKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;
import com.haizhi.databridge.bean.dto.DataTableDto;
import com.haizhi.databridge.bean.vo.DataBaseSourceVo;
import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.constants.DataSourceConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.importdata.TTableRepository;
import com.haizhi.databridge.repository.importdata.TdataBaseSourceRepository;
import com.haizhi.databridge.service.DataTableService;
import com.haizhi.databridge.util.Base64Utils;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.web.controller.form.DataTableForm;
import com.haizhi.databridge.web.result.StatusCode;

@Service
@Log4j2
public class DataTableServiceImpl extends RequestCommonData implements DataTableService {

	@Autowired
	private DataSourceServiceImpl dataSourceServiceImpl;

	@Autowired
	private DataSchedulerServiceImpl dataSchedulerServiceImpl;

	@Autowired
	private TdataBaseSourceRepository tdataBaseSourceRepo;

	@Autowired
	private TTableRepository tTableRepo;

	/**
	 * @Description //数据源创建接口
	 * @Date 2021/6/2 4:11 下午
	 * @param dataTableCreateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
	 **/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public DataTableVo.CreateVo create(DataTableForm.DataTableCreateForm dataTableCreateForm) throws IOException {
		String dbId = dataTableCreateForm.getDbId();
		String userId = dataTableCreateForm.getUserId();
		return createTable(dbId, userId, dataTableCreateForm);
	}

	private DataTableVo.CreateVo createTable(String dbId, String userId, DataTableForm.DataTableCreateBaseForm createBaseForm
	) throws UnsupportedEncodingException {
		// 判断数据源是否存在
		Optional<TDataBaseSourceBean> optionalDBBean =  tdataBaseSourceRepo.findByDbIdAndOwner(dbId, userId);
		if (!optionalDBBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "不存在的数据源");
		}
		// 判断类型
		TDataBaseSourceBean dbBean = optionalDBBean.get();
		if (DataSourceConstants.DataBaseType.BINLOG.equals(createBaseForm.getType())
				&& !DataSourceConstants.DataBaseType.MYSQL.equals(dbBean.getDbType())) {
			throw new DatabridgeException(StatusCode.TYPE_NOT_ALLOWED, "BINLOG同步方式仅支持MYSQL");
		}
		// 判断表是否存在
		Optional<TTableBean> optionalTableBean = tTableRepo.findByTbNameAndDbIdAndOwner(
				createBaseForm.getTbName(), dbBean.getDbId(), userId);
		if (optionalTableBean.isPresent()) {
			throw new DatabridgeException(StatusCode.DATA_TABLE_EXISTS,
					String.format("数据表%s已存在", optionalTableBean.get().getTbName()));
		}

		// 生成api数据源的ref
		if (DataSourceConstants.DataBaseType.API.equals(dbBean.getDbType())) {
			List<String> refInfo = new ArrayList<>();
			refInfo.add(dbBean.getDsName());
			refInfo.add(null);
			refInfo.add(createBaseForm.getTbName());
			createBaseForm.setRef(refEncode(JsonUtils.list2Json(refInfo)));
		}

		// 构建syncConfig字段的内容
		DataTableDto.SyncConfigDto syncConfig = new DataTableDto.SyncConfigDto();
		syncConfig.setType(createBaseForm.getType());
		syncConfig.setModel(createBaseForm.getModel());
		syncConfig.setRef(createBaseForm.getRef());

		// 保存到t_table表
		TTableBean tTableBean = new TTableBean();
		String tableId = genKey("table");
		tTableBean.setTableId(tableId);
		tTableBean.setTbName(createBaseForm.getTbName());
		tTableBean.setDbId(dbId);
		tTableBean.setOwner(userId);
		tTableBean.setSyncConfig(JsonUtils.toJson(syncConfig));
		tTableBean.setRemark(createBaseForm.getRemark());
		tTableRepo.save(tTableBean);

		return DataTableVo.CreateVo.builder()
				.tableId(tableId)
				.name(createBaseForm.getTbName())
				.build();
	}

	public DataTableVo.RetrieveVo retrieve(DataTableForm.DataTableRetrieveForm tableRetrieveForm) throws UnsupportedEncodingException {
		DataTableVo.RetrieveVo retrieveVo = retrieveTable(tableRetrieveForm.getTableId(), tableRetrieveForm.getUserId());
		if (ObjectUtils.isEmpty(retrieveVo)) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("数据表(table:%s)不存在", tableRetrieveForm.getTableId()));
		}

		return retrieveVo;
	}

	public DataTableVo.RetrieveVo retrieveTable(String tableId, String userId) throws UnsupportedEncodingException {

		Optional<TTableBean> optionalTableBean = tTableRepo.findByTableIdAndOwner(tableId, userId);
		if (!optionalTableBean.isPresent()) {
			return null;
		}

		TTableBean tableBean = optionalTableBean.get();

		return buildRetrieve(tableBean);
	}


	public void update(DataTableForm.DataTableUpdateForm updateForm) {
		Optional<TTableBean> optionalTableBean = tTableRepo.findByTableIdAndOwner(updateForm.getTableId(), updateForm.getUserId());
		if (!optionalTableBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("数据表(table:%s)不存在", updateForm.getTableId()));
		}
		updateTable(updateForm.getTableId(), updateForm.getUserId(), updateForm);
	}

	public void updateTable(String tableId, String userId,  DataTableForm.DataTableUpdateBaseForm updateBaseForm) {
		Optional<TTableBean> optionalTableBean = tTableRepo.findByTableIdAndOwner(tableId, userId);
		if (!optionalTableBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS,
					String.format("数据表(table:%s)不存在", tableId));
		}
		TTableBean tTableBean = optionalTableBean.get();
		DataTableDto.SyncConfigDto syncConfigDto = JsonUtils.toObject(tTableBean.getSyncConfig(), DataTableDto.SyncConfigDto.class);

		tTableBean.setSyncConfig(JsonUtils.toJson(
				DataTableDto.SyncConfigDto.builder()
						.type(updateBaseForm.getType())
						.ref(updateBaseForm.getRef())
						.model(syncConfigDto.getModel())
						.outputRef(syncConfigDto.getOutputRef())
						.rows(updateBaseForm.getRows())
						.keys(updateBaseForm.getKeys())
						.fields(updateBaseForm.getFields())
						.autoFields(updateBaseForm.getAutoFields())
						.dereplication(updateBaseForm.getDereplication())
						.clean(updateBaseForm.getClean())
						.sql(updateBaseForm.getSql())
						.blobfield(updateBaseForm.getBlobfield())
						.increase(updateBaseForm.getIncrease())
						.filter(updateBaseForm.getFilter())
						.formatter(updateBaseForm.getFormatter())
						.build())
		);
		tTableRepo.save(tTableBean);
	}

	public List<DataTableVo.RetrieveVo> listRetrieve(DataTableForm.DataTableListRetrieveForm listRetrieveForm)
			throws UnsupportedEncodingException {
		List<DataTableVo.RetrieveVo> result = new ArrayList();
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findAllByDbIdAndOwner(
				listRetrieveForm.getDbId(), listRetrieveForm.getUserId());
		if (!optionalTTableBeans.isPresent()) {
			return result;
		}
		List<TTableBean> tTableBeans = optionalTTableBeans.get();
		for (TTableBean tTableBean: tTableBeans) {
			result.add(buildRetrieve(tTableBean));
		}
		return result;
	}

	private DataTableVo.RetrieveVo buildRetrieve(TTableBean tTableBean) throws UnsupportedEncodingException {
		DataTableDto.SyncConfigDto syncConfig = new DataTableDto.SyncConfigDto();
		if (!ObjectUtils.isEmpty(tTableBean.getSyncConfig())) {
			syncConfig = JsonUtils.toObject(tTableBean.getSyncConfig(), DataTableDto.SyncConfigDto.class);
		}
		return DataTableVo.RetrieveVo.builder()
				.schema(getSchemafromRef(syncConfig.getRef()))
				.tableId(tTableBean.getTableId())
				.tbName(tTableBean.getTbName())
				.dbId(tTableBean.getDbId())
				.synced(Boolean.TRUE.equals(ObjectUtils.isEmpty(syncConfig.getOutputRef())))
				.filter(syncConfig.getFilter())
				.blobfield(syncConfig.getBlobfield())
				.keys(syncConfig.getKeys())
				.increase(syncConfig.getIncrease())
				.ref(syncConfig.getRef())
				.dereplication(syncConfig.getDereplication())
				.clean(syncConfig.getClean())
				.sql(syncConfig.getSql())
				.rows(syncConfig.getRows())
				.model(syncConfig.getModel())
				.formatter(syncConfig.getFormatter())
				.type(syncConfig.getType())
				.isView(0)
				.build();
	}

	/**
	 * @Description // 校验依赖并删除表
	 * @Date 2021/6/7 7:58 下午
	 * @param dependencyForm
	 * @return void
	 **/
	public void dependency(DataTableForm.DataTableDependencyForm dependencyForm) {
		delete(dependencyForm.getTableId(), dependencyForm.getUserId());
	}

	/**
	 * @Description //删除表
	 * @Date 2021/6/7 7:59 下午
	 * @param tableId
	 * @param userId
	 * @return void
	 **/
	public void delete(String  tableId, String userId) {
		Optional<TTableBean> optionalTTableBean = tTableRepo.findByTableIdAndOwner(
				tableId, userId);
		if (optionalTTableBean.isPresent()) {
			tTableRepo.logicDeleteByTableId(tableId);
		}
	}

	public void listUpdate(DataTableForm.DataTableListUpdateForm listUpdateForm) throws UnsupportedEncodingException {

		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findAllByDbIdAndOwner(
				listUpdateForm.getDbId(), listUpdateForm.getUserId());
		if (!optionalTTableBeans.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "该源下没有表");
		}
		Map<String, String> tableName2TableId = optionalTTableBeans.get().stream()
				.collect(Collectors.toMap(TTableBean::getTbName, TTableBean::getTableId));

		if (!ObjectUtils.isEmpty(listUpdateForm.getDeleteTables())) {
			for (String tbName: listUpdateForm.getDeleteTables()) {
				Optional<TTableBean> optionalTTableBean = tTableRepo.findByTbNameAndDbIdAndOwner(
						tbName, listUpdateForm.getDbId(), listUpdateForm.getUserId());
				if (optionalTTableBean.isPresent()) {
					delete(optionalTTableBean.get().getTableId(), listUpdateForm.getUserId());
				}
			}
		}

		if (!ObjectUtils.isEmpty(listUpdateForm.getCreateTables())) {
			for (DataTableForm.DataTableCreateBaseForm createBaseForm: listUpdateForm.getCreateTables()) {
				createTable(listUpdateForm.getDbId(), listUpdateForm.getUserId(), createBaseForm);
			}
		}

		if (!ObjectUtils.isEmpty(listUpdateForm.getUpdateTables())) {
			for (DataTableForm.DataTableUpdateBaseForm updateBaseForm: listUpdateForm.getUpdateTables()) {
				updateTable(tableName2TableId.get(updateBaseForm.getTbName()), listUpdateForm.getUserId(), updateBaseForm);
			}

		}
	}

	public DataTableVo.StatisticsVo statistics(DataTableForm.DataTableStatisticsForm statisticsForm) {
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findByOwner(statisticsForm.getUserId());

		int error = 0;
		int finished = 0;
		int syncing = 0;
		int terminated = 0;
		int total = 0;
		int totalDatabase = 0;
		int totalScheduler = 0;

		List<TTableBean> tTableBeans = optionalTTableBeans.get();
		for (TTableBean tTableBean: tTableBeans) {
			if (DataSourceConstants.DataTableStatus.ERROR.equals(tTableBean.getStatus())) {
				error += 1;
			} else if (DataSourceConstants.DataTableStatus.FINISHED.equals(tTableBean.getStatus())) {
				finished += 1;
			} else if (DataSourceConstants.DataTableStatus.SYNCING.equals(tTableBean.getStatus())) {
				syncing += 1;
			} else if (DataSourceConstants.DataTableStatus.TERMINATED.equals(tTableBean.getStatus())) {
				terminated += 1;
			}
		}

		return DataTableVo.StatisticsVo.builder()
				.error(error)
				.finished(finished)
				.syncing(syncing)
				.terminated(terminated)
				.total(total)
				.totalDatabase(dataSourceServiceImpl.countDatabases(statisticsForm.getUserId()))
				.totalScheduler(dataSchedulerServiceImpl.buildSchedulerCount(statisticsForm.getUserId()))
				.build();
	}


	public String refEncode(String ref) throws UnsupportedEncodingException {
		return Base64Utils.encodeBase64(ref);
	}

	public String refDecode(String ref) throws UnsupportedEncodingException {
		return Base64Utils.decodeBase64(ref);
	}

	public List<String> getSchemafromRef(String ref) throws UnsupportedEncodingException {
		if (ObjectUtils.isEmpty(ref)) {
			return null;
		}
		List<String> schemaList = new ArrayList<>();
		List refList = JsonUtils.toObject(refDecode(ref), List.class);
		if (ObjectUtils.isEmpty(refList)) {
			return schemaList;
		}
		for (int i = 0; i < refList.size() - 1; i++) {
			if (!ObjectUtils.isEmpty(refList.get(i))) {
				schemaList.add((String) refList.get(i));
			}
		}
		return schemaList;
	}

	public void updateSchedulerId(String tableId, String userId, String schedulerId) {
		Optional<TTableBean> optionalTTableBean = tTableRepo.findByTableIdAndOwner(tableId, userId);
		if (optionalTTableBean.isPresent()) {
			TTableBean tTableBean = optionalTTableBean.get();
			tTableBean.setSchedulerId(schedulerId);
			tTableRepo.save(tTableBean);
		}
	}

	public void cleanSchedulerId(String schedulerId, String userId) {
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findBySchedulerIdAndOwner(schedulerId, userId);
		if (optionalTTableBeans.isPresent()) {
			for (TTableBean tTableBean: optionalTTableBeans.get()) {
				tTableBean.setSchedulerId(null);
				tTableRepo.save(tTableBean);
			}
		}
	}

	public List<TTableBean> getTableBeanListBySchedulerIds(String owner, List<String> schedulerIds) {
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findTableBeanByOwnerAndSchedulerIds(owner, schedulerIds);
		return optionalTTableBeans.orElse(null);
	}
	public List<TTableBean> getTableListByOwnerAndTbNameLike(String owner, String searchKey) {
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findTableByOwnerAndTbNameLike(owner, searchKey);
		return optionalTTableBeans.orElse(null);
	}

	public String getSyncType(DataTableDto.SyncConfigDto syncConfigDto) {
		if (!ObjectUtils.isEmpty(syncConfigDto.getIncrease())) {
			return SYNC_TYPE_INCREASE;
		} else {
			return SYNC_TYPE_FULL;
		}
	}

	public Map<String, DataTableVo.TableVo> buildTableId2TableVoMap(String owner, List<TTableBean> tableBeans
	) throws UnsupportedEncodingException {
		List<String> dbIds = tableBeans.stream().distinct().map(TTableBean::getDbId).collect(Collectors.toList());
		Map<String, DataBaseSourceVo.RetrieveVo> buildDbId2DbRetrieveVo = dataSourceServiceImpl.buildDbId2DbRetrieveVo(owner, dbIds);
		Map<String, DataTableVo.TableVo> tableVoMap = new HashMap<>();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (TTableBean tTableBean: tableBeans) {
			DataBaseSourceVo.RetrieveVo dbRetrieveVo = buildDbId2DbRetrieveVo.get(tTableBean.getDbId());
			DataTableDto.SyncConfigDto syncConfigDto = JsonUtils.toObject(tTableBean.getSyncConfig(), DataTableDto.SyncConfigDto.class);
			tableVoMap.put(tTableBean.getTableId(), DataTableVo.TableVo.builder()
					.connectId(dbRetrieveVo.getConnectId())
					.dbType(dbRetrieveVo.getDbType())
					.dbId(dbRetrieveVo.getDbId())
					.dsName(dbRetrieveVo.getName())
					.exception(tTableBean.getException())
					.fetched(tTableBean.getFetched())
					.posted(tTableBean.getPosted())
					.ref(syncConfigDto.getRef())
					.remark(tTableBean.getRemark())
					.schema(getSchemafromRef(syncConfigDto.getRef()))
					.startAt(ft.format(tTableBean.getStartAt()))
					.status(tTableBean.getStatus())
					.syncType(getSyncType(syncConfigDto))
					.tableId(tTableBean.getTableId())
					.tbName(tTableBean.getTbName())
					.build()
			);
		}
		return tableVoMap;
	}
}
