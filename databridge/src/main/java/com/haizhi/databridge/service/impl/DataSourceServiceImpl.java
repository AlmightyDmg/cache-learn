package com.haizhi.databridge.service.impl;

import static com.haizhi.databridge.util.IdUtils.genKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;
import com.haizhi.databridge.bean.dto.DataSourceObjDto;
import com.haizhi.databridge.bean.vo.DataBaseSourceVo;
import com.haizhi.databridge.constants.DataSourceConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.importdata.TTableRepository;
import com.haizhi.databridge.repository.importdata.TdataBaseSourceRepository;
import com.haizhi.databridge.service.DataSourceService;
import com.haizhi.databridge.util.GzipUtils;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.web.controller.form.DataSourceForm;
import com.haizhi.databridge.web.result.StatusCode;

@Service
@Log4j2
public class DataSourceServiceImpl extends RequestCommonData implements DataSourceService {

	@Autowired
	private TdataBaseSourceRepository tdataBaseSourceRepository;

	@Autowired
	private TTableRepository tTableRepo;

	/**
	* @Description //数据源创建接口
	* @Date 2021/6/2 4:11 下午
	* @param dataSourceCreateForm
	* @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
	**/
	@Override
	@Transactional(rollbackFor = Exception.class)
	public DataBaseSourceVo.CreateVo create(DataSourceForm.DataSourceCreateForm dataSourceCreateForm) throws IOException {

		if (Boolean.TRUE.equals(checkDsSourceExists(dataSourceCreateForm.getDsName(), getUserId()))) {
			throw new DatabridgeException(StatusCode.SOURCE_EXISTS, String.format("数据源名称%s已存在", dataSourceCreateForm.getDsName()));
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
			dataSourceCreateForm.setConnectId(Arrays.toString(GzipUtils.compress(JsonUtils.toJson(connInfo))));
			// TODO 添加connectID
			System.out.printf("添加connectId");
		}

		String connStr = GzipUtils.uncompress(dataSourceCreateForm.getConnectId());
		DataSourceObjDto.SetUp setup = JsonUtils.toObject(connStr, DataSourceObjDto.SetUp.class);
		DataSourceObjDto.Options options = new DataSourceObjDto.Options();
//		DataSourceObjDto.Output output = new DataSourceObjDto.Output();
		options.setFieldComments(dataSourceCreateForm.getFieldComments());
		options.setTableComments(dataSourceCreateForm.getTableComments());

		TDataBaseSourceBean dbBean = new TDataBaseSourceBean();
		dbBean.setDbType(setup.getType().toUpperCase());
		dbBean.setDsName(dataSourceCreateForm.getDsName());
		dbBean.setRemark(dataSourceCreateForm.getRemark());
		dbBean.setOptions(JsonUtils.toJson(options));
		dbBean.setOwner(owner);
		dbBean.setDbId(dbId);
		dbBean.setSetup(JsonUtils.toJson(setup));
		tdataBaseSourceRepository.save(dbBean);

		return DataBaseSourceVo.CreateVo.builder()
				.dbId(dbId)
				.connectId(dataSourceCreateForm.getConnectId())
				.build();
		// TODO 流式数据还没写呢

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
		tdataBaseSourceRepository.logicDeleteByDbId(dbId);
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
		Optional<TDataBaseSourceBean> optionalTdataBaseSourceBean = tdataBaseSourceRepository.findByDbIdAndOwner(dbId, userId);
		if (!optionalTdataBaseSourceBean.isPresent()) {
			throw new DatabridgeException(StatusCode.SOURCE_NOT_EXISTS, "不存在的数据源");
		}
		String connStr = GzipUtils.uncompress(dataSourceUpdateForm.getConnectId());
		TDataBaseSourceBean tDataBaseSourceBean = optionalTdataBaseSourceBean.get();
		tDataBaseSourceBean.setSetup(connStr);
		tdataBaseSourceRepository.save(tDataBaseSourceBean);
		return DataBaseSourceVo.UpdateVo.builder()
				.dbId(dbId)
				.build();
	}

	public DataBaseSourceVo.RetrieveVo retrieve(DataSourceForm.DataSourceRetrieveForm dataSourceRetrieveForm
	) throws UnsupportedEncodingException {
		List<String> dbIds = new ArrayList<>();
		dbIds.add(dataSourceRetrieveForm.getDbId());
		List<DataBaseSourceVo.RetrieveVo> retrieveVos = retrieves(dataSourceRetrieveForm.getOwner(), dbIds);
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
		Optional<List<TDataBaseSourceBean>> optionalDataBaseSourceBeans = tdataBaseSourceRepository.findByDsNameAndOwner(dsName, userId);
		return optionalDataBaseSourceBeans.isPresent();
	}

	public Boolean checkDsSourceExistsByDsId(String dbId, String userId) {
		Optional<TDataBaseSourceBean> optionalTDataBaseSourceBean  = tdataBaseSourceRepository.findByDbIdAndOwner(dbId, userId);
		return optionalTDataBaseSourceBean.isPresent();
	}

	public List<DataBaseSourceVo.RetrieveVo> retrieves(String owner, List<String> dbIds) throws UnsupportedEncodingException {

		List<DataBaseSourceVo.RetrieveVo> result = new ArrayList<>();
		Optional<List<TDataBaseSourceBean>> optionalTDataBaseSourceBeans = tdataBaseSourceRepository.findByOwnerAndDbIdIn(
				owner, dbIds);
		if (!optionalTDataBaseSourceBeans.isPresent()) {
			return result;
		}
		for (TDataBaseSourceBean tDataBaseSourceBean: optionalTDataBaseSourceBeans.get()) {
			String connectId = GzipUtils.compress2String(JsonUtils.toJson(tDataBaseSourceBean.getSetup()), GzipUtils.GZIP_ENCODE_UTF_8);
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

	public Map<String, DataBaseSourceVo.RetrieveVo> buildDbId2DbRetrieveVo(String owner, List<String> dbIds) throws UnsupportedEncodingException {
		Map<String, DataBaseSourceVo.RetrieveVo> retrieveVoMap = new HashMap<>();
		List<DataBaseSourceVo.RetrieveVo> dbRetrieveVos = retrieves(owner, dbIds);
		for (DataBaseSourceVo.RetrieveVo retrieveVo: dbRetrieveVos) {
			retrieveVoMap.put(retrieveVo.getConnectId(), retrieveVo);
		}
		return retrieveVoMap;
	}




}
