package com.haizhi.databridge.service.impl;

import static com.haizhi.databridge.util.DLock.IMPORT_DB_CREATE;
import static com.haizhi.databridge.util.IdUtils.genKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;
import com.haizhi.databridge.bean.dto.DataSourceObjDto;
import com.haizhi.databridge.bean.vo.DataBaseSourceVo;
import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.constants.DataSourceConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.importdata.TTableRepository;
import com.haizhi.databridge.repository.importdata.TdataBaseSourceRepository;
import com.haizhi.databridge.service.DataSourceService;
import com.haizhi.databridge.util.CrypterUtils;
import com.haizhi.databridge.util.DLock;
import com.haizhi.databridge.util.GzipUtils;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.web.controller.form.DataSourceForm;
import com.haizhi.databridge.web.result.StatusCode;

@Service
@Log4j2
public class DataSourceServiceImpl extends RequestCommonData implements DataSourceService {

	@Autowired
	private DataTableServiceImpl tableServiceImpl;

	@Autowired
	private TdataBaseSourceRepository tdbsRepo;

	@Autowired
	private TTableRepository tTableRepo;

	@Autowired
	private DLock dLock;

	/**
	 * @Description //数据源创建接口
	 * @Date 2021/6/2 4:11 下午
	 * @param dataSourceCreateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
	 **/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public DataBaseSourceVo.CreateVo create(DataSourceForm.DataSourceCreateForm dataSourceCreateForm) throws IOException {

		String lockKey = String.format(IMPORT_DB_CREATE, dataSourceCreateForm.getDsName());
		String lockVal = String.valueOf(System.currentTimeMillis());
		try {
			String dbExistMsg = String.format("数据源名称%s已存在.", dataSourceCreateForm.getDsName());
			if (!dLock.tryLock(lockKey, lockVal)) {
				throw new DatabridgeException(StatusCode.DB_EXISTS, dbExistMsg);
			}

			if (Boolean.TRUE.equals(checkDsSourceExists(dataSourceCreateForm.getDsName(), getUserId()))) {
				throw new DatabridgeException(StatusCode.SOURCE_EXISTS, String.format("数据源名称%s已存在",
						dataSourceCreateForm.getDsName()));
			}
			// 这两个参数增加通过外部传参的方式，方便将来可能与第三方服务做对接
			String dbId = ObjectUtils.isEmpty(dataSourceCreateForm.getDbId()) ? genKey("db") : dataSourceCreateForm.getDbId();
			String owner = ObjectUtils.isEmpty(dataSourceCreateForm.getOwner()) ? getOwner() : dataSourceCreateForm.getOwner();
			Integer sourceType = dataSourceCreateForm.getSourceType();
			// 针对api数据源做出特殊的设置，详情问周同生
			if (sourceType.equals(DataSourceConstants.SourceType.SOURCE_FROM_API)) {
				Map<String, Object> connInfo = new HashMap<>();
				connInfo.put("ds_name", dataSourceCreateForm.getDsName());
				final int onlyForRandom = 1000;
				connInfo.put("random", Integer.valueOf((int) (System.currentTimeMillis() * onlyForRandom)).toString());
				connInfo.put("type", DataSourceConstants.SourceType.SOURCE_FROM_API);
				connInfo.put("role", DataSourceConstants.RoleType.DB_ROLE_READER);
				dataSourceCreateForm.setConnectId(encodeConnectId(JsonUtils.toJson(connInfo)));
			}

			String connStr = GzipUtils.uncompress2Str(dataSourceCreateForm.getConnectId(), "+-");
			//		DataSourceObjDto.SetUp setup = JsonUtils.toObject(connStr, DataSourceObjDto.SetUp.class);
			// 前传过来的密码是明文，只不过用base64压缩了而已
			//		setup.setPwd(CrypterUtils.encryptData(setup.getPwd(), DataSourceConstants.DataBaseCrypterKey.KEY));
			Map<String, Object> setup = encryptConn(connStr);
			DataSourceObjDto.Options options = new DataSourceObjDto.Options();
			//		DataSourceObjDto.Output output = new DataSourceObjDto.Output();
			options.setFieldComments(dataSourceCreateForm.getFieldComments());
			options.setTableComments(dataSourceCreateForm.getTableComments());
			options.setRealUser(dataSourceCreateForm.getRealUser());
			options.setIsDmc(dataSourceCreateForm.getIsDmc());
			options.setLabels(dataSourceCreateForm.getLabels());
			options.setDsId(dataSourceCreateForm.getDsId());

			TDataBaseSourceBean dbBean = new TDataBaseSourceBean();
			dbBean.setDbType((String) setup.get("type"));
			dbBean.setDsName(dataSourceCreateForm.getDsName());
			dbBean.setRemark(dataSourceCreateForm.getRemark());
			dbBean.setOptions(JsonUtils.toJson(options));
			dbBean.setOwner(owner);
			dbBean.setDbId(dbId);
			dbBean.setSetup(JsonUtils.toJson(setup));
			tdbsRepo.save(dbBean);

			return DataBaseSourceVo.CreateVo.builder()
					.dbId(dbId)
					.connectId(dataSourceCreateForm.getConnectId())
					.build();
			// TODO 流式数据还没写呢
		} finally {
			dLock.unlock(lockKey, lockVal);
		}

	}

	public Map<String, Object> encryptConn(String josnConn) throws IOException {
		Map<String, Object> setup = (Map<String, Object>) JsonUtils.toObject(josnConn, Map.class);
		if (ObjectUtils.isEmpty(setup.get("pwd"))) {
			setup.put("pwd", CrypterUtils.encryptData((String) setup.get("pwd"), DataSourceConstants.DataBaseCrypterKey.KEY));
		}
		return setup;
	}



	/**
	 * @Description //数据源删除接口
	 * @Date 2021/6/2 4:10 下午
	 * @param dbId
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.DeleteVo
	 **/
	public DataBaseSourceVo.DeleteVo delete(String dbId) {

		if (Boolean.TRUE.equals(checkDsSourceExistsByDsId(dbId, getUserId()))) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "不存在的数据源");
		}
		// TODO ds相关删除怎么办
		Integer count = 0;
		Optional<List<TTableBean>> optionalTTableBeans = tTableRepo.findAllByDbIdAndOwner(dbId, getUserId());
		if (!optionalTTableBeans.isPresent()) {
			return DataBaseSourceVo.DeleteVo.builder().dbId(dbId).tbCount(count).build();
		}
		for (TTableBean tTableBean: optionalTTableBeans.get()) {
			// TODO 表删除相关，以及scheduler相关
			System.out.printf("待写");
		}
		tdbsRepo.logicDeleteByDbId(dbId);
		return DataBaseSourceVo.DeleteVo.builder().dbId(dbId).tbCount(count).build();
	}

	/**
	 * @Description //修改数据源信息
	 * @Date 2021/6/2 4:25 下午
	 * @param dataSourceUpdateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.UpdateVo
	 **/
	public DataBaseSourceVo.UpdateVo update(DataSourceForm.DataSourceUpdateForm dataSourceUpdateForm) throws IOException {
		String dbId = dataSourceUpdateForm.getDbId();
		String userId = ObjectUtils.isEmpty(dataSourceUpdateForm.getOwner()) ? dataSourceUpdateForm.getOwner() : getUserId();
		Optional<TDataBaseSourceBean> optionalTdataBaseSourceBean = tdbsRepo.findByDbIdAndOwner(dbId, userId);
		if (!optionalTdataBaseSourceBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "不存在的数据源");
		}
		String connStr = GzipUtils.uncompress2Str(dataSourceUpdateForm.getConnectId(), "+-");
		TDataBaseSourceBean tDataBaseSourceBean = optionalTdataBaseSourceBean.get();
		tDataBaseSourceBean.setSetup(connStr);
		tdbsRepo.save(tDataBaseSourceBean);
		return DataBaseSourceVo.UpdateVo.builder()
				.dbId(dbId)
				.build();
	}

	public DataBaseSourceVo.RetrieveVo retrieve(DataSourceForm.DataSourceRetrieveForm dataSourceRetrieveForm
	) throws UnsupportedEncodingException {
		List<String> dbIds = new ArrayList<>();
		dbIds.add(dataSourceRetrieveForm.getDbId());
		List<DataBaseSourceVo.RetrieveVo> retrieveVos = null;
		try {
			retrieveVos = retrieves(dataSourceRetrieveForm.getOwner(), dbIds);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ObjectUtils.isEmpty(retrieveVos)) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "不存在的数据源");
		}
		return retrieveVos.get(0);
	}

	//TODO 这个接口有用吗？暂时还是先放在noah吧
//	public String labels() {
//
//	}

	// 这个接口数源没用到
	//	public String list() {
//
//	}

	public Boolean checkDsSourceExists(String dsName, String userId) {
		Optional<List<TDataBaseSourceBean>> optionalDataBaseSourceBeans = tdbsRepo.findByDsNameAndOwner(dsName, userId);
		return optionalDataBaseSourceBeans.isPresent();
	}

	public Boolean checkDsSourceExistsByDsId(String dbId, String userId) {
		Optional<TDataBaseSourceBean> optionalTDataBaseSourceBean  = tdbsRepo.findByDbIdAndOwner(dbId, userId);
		return optionalTDataBaseSourceBean.isPresent();
	}

	public List<DataBaseSourceVo.RetrieveVo> retrieves(String owner, List<String> dbIds) throws IOException {

		List<DataBaseSourceVo.RetrieveVo> result = new ArrayList<>();
		Optional<List<TDataBaseSourceBean>> optionalTDataBaseSourceBeans = tdbsRepo.findByOwnerAndDbIdIn(
				owner, dbIds);
		if (!optionalTDataBaseSourceBeans.isPresent()) {
			return result;
		}
		for (TDataBaseSourceBean tDataBaseSourceBean: optionalTDataBaseSourceBeans.get()) {
			DataSourceObjDto.SetUp setup = JsonUtils.toObject(tDataBaseSourceBean.getSetup(), DataSourceObjDto.SetUp.class);
			setup.setPwd(CrypterUtils.decryptData(setup.getPwd(), DataSourceConstants.DataBaseCrypterKey.KEY));
			String connectId = encodeConnectId(JsonUtils.toJson(setup));
			DataSourceObjDto.Options options = JsonUtils.toObject(tDataBaseSourceBean.getOptions(), DataSourceObjDto.Options.class);
			result.add(DataBaseSourceVo.RetrieveVo.builder()
					.connectId(connectId)
					.name(tDataBaseSourceBean.getDsName())
					.tableComments(options.getTableComments())
					.fieldComments(options.getFieldComments())
					.labels(options.getLabels())
					.dbId(tDataBaseSourceBean.getDbId())
					.dbType(tDataBaseSourceBean.getDbType())
					.build());
		}
		return result;

	}

	public Map<String, DataBaseSourceVo.RetrieveVo> buildDbId2DbRetrieveVo(String owner, List<String> dbIds) throws IOException {
		Map<String, DataBaseSourceVo.RetrieveVo> retrieveVoMap = new HashMap<>();
		List<DataBaseSourceVo.RetrieveVo> dbRetrieveVos = retrieves(owner, dbIds);
		for (DataBaseSourceVo.RetrieveVo retrieveVo: dbRetrieveVos) {
			retrieveVoMap.put(retrieveVo.getDbId(), retrieveVo);
		}
		return retrieveVoMap;
	}

	public Integer countDatabases(String owner) {
		Map<String, BigInteger> databaseCountMap = tdbsRepo.countTDataBaseSourceBeanByOwner(owner);
		return Integer.parseInt(String.valueOf(databaseCountMap.get("count")));
	}

	public DataBaseSourceVo.DataSourceStatusVo status(DataSourceForm.DataSourceStatusForm dataSourceStatusForm
	) throws IOException {
		List<TDataBaseSourceBean> queryDbBeanList = new ArrayList<>();
		if (ObjectUtils.isEmpty(dataSourceStatusForm.getDbId())) {
			Optional<List<TDataBaseSourceBean>> optionalDbsBeans = tdbsRepo.findByOwnerAndSourceType(
					dataSourceStatusForm.getUserId(), dataSourceStatusForm.getSourceType());
			if (!ObjectUtils.isEmpty(optionalDbsBeans)) {
				queryDbBeanList = optionalDbsBeans.get();
			}

		} else {
			Optional<TDataBaseSourceBean> optionalDbsBean = tdbsRepo.findByDbIdAndOwner(
					dataSourceStatusForm.getDbId(), dataSourceStatusForm.getUserId());
			if (!ObjectUtils.isEmpty(optionalDbsBean)) {
				queryDbBeanList.add(optionalDbsBean.get());
			}
		}

		List<DataBaseSourceVo.DataSourceVo> dataSourceVos = buildDataSourceVo(dataSourceStatusForm.getUserId(), queryDbBeanList);
		// 根据dbId查出所有表
		List<String> dbIds = queryDbBeanList.stream().map(TDataBaseSourceBean::getDbId).collect(Collectors.toList());

		List<DataTableVo.TableVo> tableVoList = tableServiceImpl.getTableVosByDbIds(
				dataSourceStatusForm.getUserId(), dbIds, dataSourceStatusForm.getStatus(), dataSourceStatusForm.getTbName());
		DataTableVo.StatusVo countStatusByDbIds = tableServiceImpl.countStatusByDbIds(
				dataSourceStatusForm.getUserId(), dbIds, dataSourceStatusForm.getStatus(), dataSourceStatusForm.getTbName());
		Integer pageCount = (int) Math.ceil((double) tableVoList.size() / dataSourceStatusForm.getLimit());

		return DataBaseSourceVo.DataSourceStatusVo.builder()
				.datasource(dataSourceVos)
				.pagecount(pageCount)
				.query(dataSourceStatusForm)
				.status(countStatusByDbIds)
				.tables(tableVoList)
				.totalitems(tableVoList.size())
				.build();

	}

	private List<DataBaseSourceVo.DataSourceVo> buildDataSourceVo(String owner, List<TDataBaseSourceBean> dataBaseSourceBeans
	) throws IOException {
		List<String> dbIds = dataBaseSourceBeans.stream().map(TDataBaseSourceBean::getDbId).collect(Collectors.toList());
		List<DataBaseSourceVo.DataSourceVo> dataSourceVos = new ArrayList<>();
		Map<String, Integer> dbId2TableNumMap = tableServiceImpl.getDbId2TablesNumMap(owner, dbIds);
		for (TDataBaseSourceBean dataBaseSourceBean: dataBaseSourceBeans) {
//			DataSourceObjDto.SetUp setUp = JsonUtils.toObject(dataBaseSourceBean.getSetup(), DataSourceObjDto.SetUp.class);
//			setUp.setPwd(CrypterUtils.decryptData(setUp.getPwd(), DataSourceConstants.DataBaseCrypterKey.KEY));
			Map<String, Object> setUp = decryptConn(dataBaseSourceBean.getSetup());
			DataSourceObjDto.Options options = JsonUtils.toObject(dataBaseSourceBean.getOptions(), DataSourceObjDto.Options.class);
			dataSourceVos.add(DataBaseSourceVo.DataSourceVo.builder()
					.connectId(encodeConnectId(JsonUtils.toJson(setUp)))
					.connector(String.format("%s@%s", setUp.get("uid"),
							!ObjectUtils.isEmpty(setUp.get("connStr")) ? setUp.get("connStr") : setUp.get("server")))
					.dbId(dataBaseSourceBean.getDbId())
					.dbType(dataBaseSourceBean.getDbType())
					.dsName(dataBaseSourceBean.getDsName())
					.labels(options.getLabels())
					.remark(dataBaseSourceBean.getRemark())
					.tbCount(dbId2TableNumMap.getOrDefault(dataBaseSourceBean.getDbId(), 0))
					.build());
		}
		return dataSourceVos;
	}

	public Map<String, Object> decryptConn(String josnConn) throws IOException {
		Map<String, Object> setUp = (Map<String, Object>) JsonUtils.toObject(josnConn, Map.class);
		if (!ObjectUtils.isEmpty(setUp.get("pwd"))
				&& "true".equalsIgnoreCase(setUp.getOrDefault("crypter", "false").toString())) {
			setUp.put("pwd", CrypterUtils.decryptData((String) setUp.get("pwd"), DataSourceConstants.DataBaseCrypterKey.KEY));
		}
		setUp.remove("crypter");

		Map<String, Object> orderedSetup = new TreeMap<>();
		setUp.forEach(orderedSetup::put);
		return orderedSetup;
	}

	public static String encodeConnectId(String s) throws UnsupportedEncodingException {
		return GzipUtils.compress2Str(s, "+-");
	}

	public static String decodeConnectId(String s) throws IOException {
		return GzipUtils.uncompress2Str(s, "+-");
	}

	public static void main(String[] args) {
		String test = "";
	}
}
