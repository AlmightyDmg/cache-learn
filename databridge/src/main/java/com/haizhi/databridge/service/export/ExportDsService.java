package com.haizhi.databridge.service.export;

import static com.haizhi.databridge.bean.constants.BeanConstants.TbPermissionConstants.ROLE_TYPE_CHAT;
import static com.haizhi.databridge.bean.constants.BeanConstants.TbPermissionConstants.ROLE_TYPE_GROUP;
import static com.haizhi.databridge.bean.constants.BeanConstants.TbPermissionConstants.ROLE_TYPE_ROLEACCOUNT;
import static com.haizhi.databridge.bean.constants.BeanConstants.TbPermissionConstants.ROLE_TYPE_USER;
import static com.haizhi.databridge.constants.MetaConstants.DsType.DATAHUB;
import static com.haizhi.databridge.constants.MetaConstants.DsType.GREENPLUM;
import static com.haizhi.databridge.constants.MetaConstants.DsType.MYSQL;
import static com.haizhi.databridge.constants.MetaConstants.DsType.POSTGRESQL;
import static com.haizhi.databridge.constants.MetaConstants.Job.JOB_SOURCE_FROM_DMC;
import static com.haizhi.databridge.util.IdUtils.genKey;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.exportdata.DsBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportDsTbBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbPermissionBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbRelRuleBean;
import com.haizhi.databridge.bean.domain.exportdata.JobBean;
import com.haizhi.databridge.bean.dto.UserBelongDto;
import com.haizhi.databridge.bean.vo.ExportDsVo;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.exportdata.DsRepository;
import com.haizhi.databridge.repository.exportdata.ExportDsTbRepository;
import com.haizhi.databridge.repository.exportdata.ExportTbPermissionRepository;
import com.haizhi.databridge.repository.exportdata.ExportTbRelRuleRepository;
import com.haizhi.databridge.repository.exportdata.JobRepository;
import com.haizhi.databridge.service.common.PermissionService;
import com.haizhi.databridge.util.PasswordUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.web.controller.form.ExportDsForm;
import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;

/**
 * @author zhaohuanhuan
 * @version 1.0
 * @create 12/18/20 4:00 下午
 **/

@Component
@Service
@Log4j2
public class ExportDsService extends RequestCommonData {

	@Autowired
	private DsRepository dsRepository;
	@Autowired
	private ExportDsTbRepository exportDsTbRepository;
	@Autowired
	private ExportTbPermissionRepository expTbPmsRepository;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private PermissionService tbPermissionService;
	@Autowired
	private ExportTbRelRuleRepository exportTbRelRuleRepository;
	@Autowired
	private DmcTableApi dmcTableApi;

	/**
	* @Description //数据源创建
	* @Date 2021/1/12 6:13 下午
	* @param form
	* @return String dsId
	**/
	public ExportDsVo.DsCreateVo create(ExportDsForm.ExportDsCreateForm form) throws UnsupportedEncodingException {
		// 数据来源方式，0：导入，1：导出
		final  Integer sourceType = 1;
		String dsId = genKey("ds");
		// 检测数据源是否重复，重名或者已接入
		checkDuplicateDs(form);
		// 获取数据源配置信息
		DsBean dsBean = new DsBean();
		dsBean.setDsId(dsId);
		dsBean.setType(form.getType());
		dsBean.setDsDesc(form.getDsDesc());
		dsBean.setUsername(form.getConfig().getUser());
		dsBean.setHost(form.getConfig().getUrl());
		dsBean.setPassword(PasswordUtils.decodeBase64(form.getConfig().getPassword()));
		dsBean.setDatabase(form.getConfig().getDbName());
		dsBean.setName(form.getDsName());
		dsBean.setOwner(getOwner());
		dsBean.setSourceType(sourceType);
		dsBean.setVersionInfo(form.getConfig().getVersion());
		if (form.getType().equals(MYSQL) || form.getType().equals(POSTGRESQL)
				|| form.getType().equals(GREENPLUM)) {
			dsBean.setPort(form.getConfig().getPort());
		}
		try {
			dmcTableApi.jobGetTables(genTypeToDbNameMap().get(form.getType()),
				getDsUrl(form.getConfig().getUrl(), form.getConfig().getPort(), form.getType()), form.getConfig().getUser(),
				PasswordUtils.decodeBase64(form.getConfig().getPassword()), form.getConfig().getDbName(),
				genTypeToDbNameMap().get(form.getType()), false, false,
				Integer.valueOf(!ObjectUtils.isEmpty(form.getConfig().getVersion()) ? form.getConfig().getVersion() : "0"));
		} catch (IOException e) {
			throw new DatabridgeException(StatusCode.CONN_ERROR, e.getMessage());
		}
		//获取选中的表并创建
		List<String> tbNameList = form.getTbNameList();
		for (String tbName: tbNameList) {
			xtbCreate(tbName, dsId, getOwner(), getEnterpriseId());
		}
		dsRepository.save(dsBean);
		return ExportDsVo.DsCreateVo.builder().dsId(dsId).build();
	}

	public void checkDuplicateDs(ExportDsForm.ExportDsCreateForm form) {
		Optional<List<DsBean>> dsBeanListOptional = dsRepository.findByOwnerAndTypeAndSourceType(getOwner(), form.getType(), 1);
		if (dsBeanListOptional.isPresent()) {
			for (DsBean dsBean: dsBeanListOptional.get()) {
				if (form.getDsName().equals(dsBean.getName())) {
					throw new DatabridgeException(StatusCode.DS_NAME_REPEAT, "该名称已存在");
				} else if (form.getConfig().getUrl().equals(dsBean.getHost())
					&& form.getConfig().getUser().equals(dsBean.getUsername())
					&& form.getConfig().getDbName().equals(dsBean.getDatabase())) {
					throw new DatabridgeException(StatusCode.DB_EXISTS, "该数据源已存在，请勿重复接入");
				}
			}
		}
	}

	/**
	* @Description //数据源列表展示
	* @Date 2020/12/21 3:03 下午
	* @param form
	* @return com.haizhi.hora.bean.vo.ExportDsVo.DsListRespVo
	**/
	public List<ExportDsVo.ExportDsListVo> list(ExportDsForm.ExportDsListForm form) throws UnsupportedEncodingException {
		List<ExportDsVo.ExportDsListVo> result = new ArrayList<>();

		UserBelongDto userBelongDto = getUserBelong();
		Map<String, List<ExportDsVo.XtbInfoVo>> dsId2XtbIdListMap = getDsId2XtbInfoVoListMap(userBelongDto);
		List<DsBean> dsBeanList = getDsBeanList(form.getOnlyOwner(), dsId2XtbIdListMap);
		Map<Integer, List<ExportDsVo.DsInfoVo>> dsTypeToDsListVoMap = new HashMap();
		List<String> dsIds = dsBeanList.stream().map(DsBean :: getDsId).distinct().collect(Collectors.toList());
		Map<String, Integer> dsId2TbNumMap = getExpDsTbNumGroupByDsId(dsIds);
		for (DsBean dsBean: dsBeanList) {
			ExportDsVo.ConfigVo configVo = ExportDsVo.ConfigVo.builder().name(genTypeToDbNameMap().get(dsBean.getType()))
				.url(dsBean.getHost()).port(dsBean.getPort()).user(dsBean.getUsername())
				.password(PasswordUtils.encodeBase64(dsBean.getPassword())).dbName(dsBean.getDatabase())
				.isNetSSL(dsBean.getIsNetSSL()).isSecurity(dsBean.getIsSecurity()).version(dsBean.getVersionInfo()).build();
			ExportDsVo.DsInfoVo dsInfoVo = ExportDsVo.DsInfoVo.builder().dsId(dsBean.getDsId()).dsName(dsBean.getName())
				.owner(dsBean.getOwner()).type(dsBean.getType()).dsDesc(dsBean.getDsDesc()).config(configVo)
				.number(dsId2TbNumMap.get(dsBean.getDsId())).xtbInfoList(dsId2XtbIdListMap.get(dsBean.getDsId())).build();
			if (dsTypeToDsListVoMap.containsKey(dsBean.getType())) {
				dsTypeToDsListVoMap.get(dsBean.getType()).add(dsInfoVo);
			} else {
				List<ExportDsVo.DsInfoVo> dsListVos = new ArrayList<>();
				dsListVos.add(dsInfoVo);
				dsTypeToDsListVoMap.put(dsBean.getType(), dsListVos);
			}
		}

		for (Map.Entry<Integer, List<ExportDsVo.DsInfoVo>> vo : dsTypeToDsListVoMap.entrySet()) {
			result.add(ExportDsVo.ExportDsListVo.builder().type(vo.getKey()).dsList(vo.getValue()).build());
		}

		return result;
	}

	/**
	* @Description //获取数据源bean对象列表
	* @Date  6:14 下午
	* @param onlyOwner
* @param dsId2XtbIdListMap
	* @return java.util.List<com.haizhi.hora.bean.domain.DsBean>
	**/
	public List<DsBean> getDsBeanList(Integer onlyOwner, Map<String, List<ExportDsVo.XtbInfoVo>> dsId2XtbIdListMap) {
		List<DsBean> result = new ArrayList<>();
		if (onlyOwner.equals(1)) {
			Optional<List<DsBean>> dsBeanListOptional = dsRepository.findByOwnerAndSourceType(getOwner(), 1);
			if (!dsBeanListOptional.isPresent()) {
				return result;
			}
			result = dsBeanListOptional.get();
		} else {
			List<String> dsIdList = dsId2XtbIdListMap.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
			Optional<List<DsBean>> dsBeanListOptional = dsRepository.findAllByDsIdIn(dsIdList);
			if (dsBeanListOptional.isPresent()) {
				result = dsBeanListOptional.get();
			}
		}
		return result;
	}

	/**
	* @Description //获取授权用户下的数据源与数据源下的目的表信息映射关系
	* @Date 2021/1/12 6:15 下午
	* @param userBelongDto
	* @return java.util.Map<java.lang.String,java.util.List<com.haizhi.hora.bean.vo.ExportDsVo.XtbInfoVo>>
	**/
	public Map<String, List<ExportDsVo.XtbInfoVo>> getDsId2XtbInfoVoListMap(UserBelongDto userBelongDto) {
		Map<String, List<ExportDsVo.XtbInfoVo>> result = new HashMap<>();


		// 获取授权的
		if (ObjectUtils.isEmpty(userBelongDto)) {
			userBelongDto = getUserBelong();
		}
		List<String> roleList = new ArrayList<>();
		roleList.addAll(userBelongDto.getRoles());
		roleList.addAll(userBelongDto.getChats());
		roleList.addAll(userBelongDto.getGroups());
		roleList.add(getOwner());
		Optional<List<ExportTbPermissionBean>> exportTbPermissionBeanListOptional = expTbPmsRepository.findAllByRoleIdIn(roleList);

		if (!exportTbPermissionBeanListOptional.isPresent()) {
			return result;
		}
		List<ExportTbPermissionBean> exportTbPermissionBeanList = exportTbPermissionBeanListOptional.get();
		List<String> xtbIdList = exportTbPermissionBeanList.stream().map(ExportTbPermissionBean::getXtbId).collect(Collectors.toList());
		Optional<List<ExportDsTbBean>> optionalExportDsTbBeanList = exportDsTbRepository.findXtbInfos(xtbIdList);
		Map<String, ExportDsVo.XtbInfoVo> xtbInfoVoMap = new HashMap<>();
		List<ExportDsTbBean> exportDsTbBeanList = optionalExportDsTbBeanList.orElseThrow(() -> new DatabridgeException("目的表未接入"));
		for (ExportDsTbBean exportDsTbBean: exportDsTbBeanList) {
			xtbInfoVoMap.put(
				exportDsTbBean.getXtbId(), ExportDsVo.XtbInfoVo
					.builder()
					.xtbId(exportDsTbBean.getXtbId())
					.xtbName(exportDsTbBean.getName())
					.build()
			);
		}
		Map<String, List<ExportDsVo.XtbInfoVo>> dsId2XtbInfoVoListMap = new HashMap<>();
		for (ExportTbPermissionBean exportTbPermissionBean: exportTbPermissionBeanList) {
			if (dsId2XtbInfoVoListMap.containsKey(exportTbPermissionBean.getDsId())) {
				dsId2XtbInfoVoListMap.get(exportTbPermissionBean.getDsId()).add(xtbInfoVoMap.get(exportTbPermissionBean.getXtbId()));
			} else {
				List<ExportDsVo.XtbInfoVo> xtbInfoVoList = new ArrayList<>();
				xtbInfoVoList.add(xtbInfoVoMap.get(exportTbPermissionBean.getXtbId()));
				dsId2XtbInfoVoListMap.put(exportTbPermissionBean.getDsId(), xtbInfoVoList);
			}
		}

		for (Map.Entry<String, List<ExportDsVo.XtbInfoVo>> entry : dsId2XtbInfoVoListMap.entrySet()) {
			List<ExportDsVo.XtbInfoVo> newXtbInfoVoList = new ArrayList<>();
			List<ExportDsVo.XtbInfoVo> xtbInfoVoList = entry.getValue();
			for (ExportDsVo.XtbInfoVo xtbInfoVo: xtbInfoVoList) {
				if (!newXtbInfoVoList.contains(xtbInfoVo)) {
					newXtbInfoVoList.add(xtbInfoVo);
				}
			}
			result.put(entry.getKey(), newXtbInfoVoList);
		}
		return result;
	}



	public Map<String, Integer> getExpDsTbNumGroupByDsId(List<String> dsIds) {
		Map<String, Integer> dsId2TbNumMap = new HashMap<>();
		List<Map<String, Object>> dsTbNumList = exportDsTbRepository.countExportDsTbBeanByDsIdIn(dsIds);
		for (Map<String, Object> ds2Tbnum : dsTbNumList) {
			dsId2TbNumMap.put(ds2Tbnum.get("ds_id").toString(), Integer.parseInt(ds2Tbnum.get("count(ds_id)").toString()));
		}
		return dsId2TbNumMap;
	}

	/**
	* @Description //修改数据源连接信息
	* @Date 2020/12/21 3:26 下午
	* @param form
	* @return void
	**/
	public void modify(ExportDsForm.ExportDsModifyForm form) throws UnsupportedEncodingException {
		// 数据来源方式，0：导入，1：导出
		final  Integer sourceType = 1;

		// 获取数据源配置信息
		Optional<DsBean> dsBeanOptional = dsRepository.findByDsId(form.getDsId());
		if (dsBeanOptional.isPresent()) {
			DsBean dsBean = dsBeanOptional.get();
			// 校验名称和配置是否重复
			Optional<List<DsBean>> dsBeanListOptional = dsRepository.findByOwnerAndTypeAndSourceType(getOwner(), dsBean.getType(), 1);
			if (dsBeanListOptional.isPresent()) {
				for (DsBean bean: dsBeanListOptional.get()) {
					if (!ObjectUtils.isEmpty(form.getDsName()) && form.getDsName().equals(bean.getName())
						&& !form.getDsId().equals(bean.getDsId())) {
						throw new DatabridgeException(StatusCode.DS_NAME_REPEAT, "该名称已存在");
					} else if (form.getConfig().getUrl().equals(bean.getHost())
						&& form.getConfig().getUser().equals(bean.getUsername())
						&& form.getConfig().getDbName().equals(bean.getDatabase())
						&& !form.getDsId().equals(bean.getDsId())) {
						throw new DatabridgeException(StatusCode.DB_EXISTS, "该数据源已存在，请勿重复接入");
					}
				}
				form.getConfig().setType(dsBean.getType());
				dsSave(form, dsBean);
			}
		}
	}

	private void dsSave(ExportDsForm.ExportDsModifyForm form, DsBean dsBean) throws UnsupportedEncodingException {
		dsBean.setDsId(form.getDsId());
		dsBean.setDsDesc(form.getDsDesc());
		dsBean.setUsername(form.getConfig().getUser());
		dsBean.setHost(form.getConfig().getUrl());
		dsBean.setPassword(PasswordUtils.decodeBase64(form.getConfig().getPassword()));
		dsBean.setDatabase(form.getConfig().getDbName());
		dsBean.setVersionInfo(form.getConfig().getVersion());
		dsBean.setName(form.getDsName());
		dsBean.setSourceType(1);

		try {
			dmcTableApi.jobGetTables(genTypeToDbNameMap().get(form.getConfig().getType()),
				getDsUrl(form.getConfig().getUrl(), form.getConfig().getPort(), form.getConfig().getType()),
				form.getConfig().getUser(),
				PasswordUtils.decodeBase64(form.getConfig().getPassword()), form.getConfig().getDbName(),
				genTypeToDbNameMap().get(form.getConfig().getType()), false, false,
				Integer.valueOf(!ObjectUtils.isEmpty(form.getConfig().getVersion()) ? form.getConfig().getVersion() : "0"));
		} catch (Exception e) {
			throw new DatabridgeException(StatusCode.CONN_ERROR, e.getMessage());
		}

		dsRepository.save(dsBean);
	}

	public void delete(String dsId) {
		Optional<List<JobBean>> optionalJobBeans = jobRepository.findAllByDsId(dsId);
		Optional<List<ExportDsTbBean>> optionalExportDsTbBeanList = exportDsTbRepository.findAllByDsId(dsId);
		// 被依赖的xtbId
		if (optionalJobBeans.isPresent()) {
			List<String> xtbIdRelyList = optionalJobBeans.get().stream().map(JobBean::getXtbId).collect(Collectors.toList());
			if (optionalExportDsTbBeanList.isPresent()) {
				List<String> canDelXtbIdList = new ArrayList<>();
				for (ExportDsTbBean exportDsTbBean: optionalExportDsTbBeanList.get()) {
					if (!xtbIdRelyList.contains(exportDsTbBean.getXtbId())) {
						canDelXtbIdList.add(exportDsTbBean.getXtbId());
					}
				}
				if (!canDelXtbIdList.isEmpty()) {
					exportDsTbRepository.logicDeleteByXtbIdIn(canDelXtbIdList);
					expTbPmsRepository.logicDeleteByXtbIdIn(canDelXtbIdList);
					exportTbRelRuleRepository.logicDeleteByXtbIdIn(canDelXtbIdList);
				}
			}

			throw new DatabridgeException(StatusCode.DS_DELETE_ERROR, "存在部分表已被导出任务使用，仅删除未映射的表");

		} else {
			if (optionalExportDsTbBeanList.isPresent()) {
				List<String> xtbIdList = optionalExportDsTbBeanList.get().stream()
					.map(ExportDsTbBean::getXtbId).collect(Collectors.toList());
				if (!xtbIdList.isEmpty()) {
					exportDsTbRepository.logicDeleteByXtbIdIn(xtbIdList);
					expTbPmsRepository.logicDeleteByXtbIdIn(xtbIdList);
					exportTbRelRuleRepository.logicDeleteByXtbIdIn(xtbIdList);
				}
			}
		}
		dsRepository.deleteByDsId(dsId);
	}

	public List<ExportDsVo.TableVo> tableList(ExportDsForm.ExportDsTableListForm form) {
		List<ExportDsVo.TableVo> result = new ArrayList<>();
		List<ExportDsVo.XtbInfoVo> xtbVoList = new ArrayList<>();
		Map<String, List<ExportDsVo.XtbInfoVo>> dsId2XtbIdListMap = getDsId2XtbInfoVoListMap(getUserBelong());

		if (ObjectUtils.isEmpty(form.getDsId())) {

			Optional<List<DsBean>> optionalDsBeanList = dsRepository.findByOwnerAndTypeAndSourceType(getOwner(), form.getType(), 1);
			if (!optionalDsBeanList.isPresent()) {
				return result;
			}
			List<String> dsIdList = optionalDsBeanList.get().stream().map(DsBean::getDsId).collect(Collectors.toList());
			for (String dsId: dsIdList) {
				xtbVoList.addAll(dsId2XtbIdListMap.getOrDefault(dsId, new ArrayList<>()));
			}
		} else {
			String dsId = form.getDsId();
			xtbVoList.addAll(dsId2XtbIdListMap.getOrDefault(dsId, new ArrayList<>()));
		}

		// 获取被应用到job中的xtbIdList，获取含有检验规则的xtbIdList
		// 获取被映射过的xtbIdList
		List<String> hasRuleXtbIdList = new ArrayList<>();
		List<String> xtbIdList = xtbVoList.stream().map(ExportDsVo.XtbInfoVo::getXtbId).collect(Collectors.toList());
		List<String> mappedXtbIdList = getMappedXtbIdList(xtbIdList);
		// 获取含有校验规则的表
		Optional<List<ExportTbRelRuleBean>> exportTbRelRuleBeanListOptional = exportTbRelRuleRepository.findAllByXtbIdIn(xtbIdList);
		if (exportTbRelRuleBeanListOptional.isPresent()) {
			hasRuleXtbIdList = exportTbRelRuleBeanListOptional.get()
				.stream().map(ExportTbRelRuleBean::getXtbId).collect(Collectors.toList());
		}
		for (ExportDsVo.XtbInfoVo xtbInfoVo: xtbVoList) {
			result.add(ExportDsVo.TableVo.builder().xtbId(xtbInfoVo.getXtbId()).name(xtbInfoVo.getXtbName())
				.isMapped(mappedXtbIdList.contains(xtbInfoVo.getXtbId()) ? 1 : 0)
				.hasInspectionRules(hasRuleXtbIdList.contains(xtbInfoVo.getXtbId()) ? 1 : 0).build());
		}
		return result;
	}

	public List<String> getMappedXtbIdList(List<String> xtbIdList) {
		List<String> result = new ArrayList<>();
		Optional<List<JobBean>> jobBeanListOptional = jobRepository.findAllByXtbIdInAndJobSource(xtbIdList, JOB_SOURCE_FROM_DMC);
		if (jobBeanListOptional.isPresent()) {
			result = jobBeanListOptional.get().stream().map(JobBean::getXtbId).collect(Collectors.toList());
		}
		return result;
	}

	public List<String> tableUpdate(ExportDsForm.ExportDsTableUpdateForm form) {
		List<String> createIds = new ArrayList<>();

		if (!ObjectUtils.isEmpty(form.getDelTableList())) {
			boolean hasRely = false;
			Optional<List<JobBean>> optionalJobBeanList = jobRepository.findAllByXtbIdInAndJobSource(
				form.getDelTableList(), JOB_SOURCE_FROM_DMC);
			if (optionalJobBeanList.isPresent()) {
				List<String> connectorTables = getConnectorTables(form.getDsId());
				Map<String, String> delIdNameMap = new HashMap<>();
				exportDsTbRepository.findXtbInfos(form.getDelTableList()).ifPresent(tbs -> delIdNameMap.putAll(tbs.stream()
						.collect(Collectors.toMap(ExportDsTbBean::getXtbId, ExportDsTbBean::getName))));
				List<String> xtbIdList = optionalJobBeanList.get().stream().map(JobBean::getXtbId).collect(Collectors.toList());
				for (String tableId: form.getDelTableList()) {
					if (!xtbIdList.contains(tableId)) {
						xtbDelete(tableId);
					} else {
						if (connectorTables.contains(delIdNameMap.get(tableId))) {
							hasRely = true;
						}
					}
				}
				if (hasRely) {
					throw new DatabridgeException(StatusCode.TB_USING, "存在被导出任务使用的表,此次删除不能执行");
				}
			} else {
				for (String tableId: form.getDelTableList()) {
					xtbDelete(tableId);
				}
			}
		}

		String dsId = form.getDsId();
		if (!ObjectUtils.isEmpty(form.getAddTableList())) {
			for (String table: form.getAddTableList()) {
				String xtbId = xtbCreate(table, dsId, getOwner(), getEnterpriseId());
				createIds.add(xtbId);
			}
		}
		return createIds;

	}

	public void pmsModify(ExportDsForm.ExportDsPmsCreateForm form) {

		// 获取已授权的id，用户去重
		Set<String> roleSet = getXtbRoleSet(form.getXtbId());
		String owner = getOwner();

		// 授权和取消用户操作
		String xtbId = form.getXtbId();
		Optional<ExportDsTbBean> exportDsTbBeanOptional = exportDsTbRepository.findByXtbId(xtbId);
		ExportDsTbBean exportDsTbBean = exportDsTbBeanOptional.orElseThrow(() -> new DatabridgeException("导出表已不存在"));
		// 修改用户组目的表授权信息
		for (String userId: form.getUserList().getAll()) {
			if (!roleSet.contains(userId)) {
				addPms(exportDsTbBean.getDsId(), form.getXtbId(), userId, ROLE_TYPE_USER, owner);
			}
		}
		delPms(xtbId, form.getUserList().getDel());

		// 授权和取消组操作
		for (String groupId: form.getGroupList().getAll()) {
			if (!roleSet.contains(groupId)) {
				addPms(exportDsTbBean.getDsId(), form.getXtbId(), groupId, ROLE_TYPE_GROUP, owner);
			}
		}
		delPms(xtbId, form.getGroupList().getDel());

		// 授权和取消角色操作
		for (String role: form.getRoleList().getAll()) {
			if (!roleSet.contains(role)) {
				addPms(exportDsTbBean.getDsId(), form.getXtbId(), role, ROLE_TYPE_ROLEACCOUNT, owner);
			}
		}
		delPms(xtbId, form.getRoleList().getDel());

		// 授权和取消临时组操作
		for (String chat: form.getChatList().getAll()) {
			if (!roleSet.contains(chat)) {
				addPms(exportDsTbBean.getDsId(), form.getXtbId(), chat, ROLE_TYPE_CHAT, owner);
			}
		}
		delPms(xtbId, form.getChatList().getDel());
	}

	public void delPms(String xtbId, List<String> roleIds) {
		for (String chat: roleIds) {
			expTbPmsRepository.logicDeleteByXtbIdAndRoleId(xtbId, chat);
		}
	}

	// 获取表授权给了哪些角色的集合
	public Set<String> getXtbRoleSet(String xtbId) {
		Set<String> roleSet = new HashSet<>();
		Optional<List<ExportTbPermissionBean>> expTbPmsListOptional = expTbPmsRepository.findAllByXtbId(xtbId);
		if (expTbPmsListOptional.isPresent()) {
			for (ExportTbPermissionBean expTbPmsBean: expTbPmsListOptional.get()) {
				roleSet.add(expTbPmsBean.getRoleId());
			}
		}
		return roleSet;
	}

	public ExportDsVo.ExportTbPmsListVo pmsListByDsId(String dsId) {

		ExportDsVo.ExportTbPmsRoleListVo pmsRoleListVo = pmsRoleList(dsId);
		Map<String, List<String>> roleIdToXtbIdList = getRoleIdToXtbIdList(dsId);
		Map<String, String> xtbIdToName = getXtbId2Name(dsId);

		// 获取ds数据信息
		Optional<DsBean> dsBeanOptional = dsRepository.findByDsId(dsId);
		dsBeanOptional.orElseThrow(() -> new DatabridgeException("数据源不存在"));
		return ExportDsVo.ExportTbPmsListVo
			.builder()
			.dsName(dsBeanOptional.get().getName())
			.dsId(dsBeanOptional.get().getDsId())
			.dsDesc(dsBeanOptional.get().getDsDesc())
			.user(getUserPmsList(pmsRoleListVo, roleIdToXtbIdList, xtbIdToName))
			.userGroup(getUserGroupPmsList(pmsRoleListVo, roleIdToXtbIdList, xtbIdToName))
			.roleAccount(getRoleAccountPmsList(pmsRoleListVo, roleIdToXtbIdList, xtbIdToName))
			.chat(getChatPmsList(pmsRoleListVo, roleIdToXtbIdList, xtbIdToName))
			.build();
	}

	public List<ExportDsVo.UserVo> getUserPmsList(ExportDsVo.ExportTbPmsRoleListVo pmsRoleListVo,
												  Map<String, List<String>> roleIdToXtbIdList,
												  Map<String, String> xtbIdToName
	) {
		Map<String, String> roleId2Name = new HashMap<>();
		List<ExportDsVo.UserVo> userVoList = new ArrayList<>();
		tbPermissionService.getUserInfoByIds(roleId2Name, pmsRoleListVo.getUser(), getOwner());
		for (String userId: pmsRoleListVo.getUser()) {
			// 构造tbList
			List<String> xtbVoList = new ArrayList<>();
			List<String> xtbIdList = roleIdToXtbIdList.get(userId);
			for (String xtbId: xtbIdList) {
				if (!xtbIdToName.get(xtbId).isEmpty()) {
					xtbVoList.add(xtbIdToName.get(xtbId));
				}
			}
			userVoList.add(ExportDsVo.UserVo.builder().name(roleId2Name.get(userId)).userId(userId).tbList(xtbVoList).build());
		}
		return userVoList;
	}

	public List<ExportDsVo.GroupVo> getUserGroupPmsList(ExportDsVo.ExportTbPmsRoleListVo pmsRoleListVo,
												  Map<String, List<String>> roleIdToXtbIdList,
												  Map<String, String> xtbIdToName
	) {
		List<ExportDsVo.GroupVo> groupVoList = new ArrayList<>();
		Map<String, String> roleId2Name = new HashMap<>();
		tbPermissionService.getGroupInfoByIds(roleId2Name, pmsRoleListVo.getUserGroup(), getOwner());
		for (String groupId: pmsRoleListVo.getUserGroup()) {

			// 构造tbList
			List<String> xtbVoList = new ArrayList<>();
			List<String> xtbIdList = roleIdToXtbIdList.get(groupId);
			for (String xtbId: xtbIdList) {
				if (!xtbIdToName.get(xtbId).isEmpty()) {
					xtbVoList.add(xtbIdToName.get(xtbId));
				}
			}

			groupVoList.add(ExportDsVo.GroupVo.builder().groupName(roleId2Name.get(groupId)).groupId(groupId).tbList(xtbVoList).build());
		}
		return groupVoList;
	}

	public List<ExportDsVo.RoleAccountVo> getRoleAccountPmsList(
		ExportDsVo.ExportTbPmsRoleListVo pmsRoleListVo,
		Map<String, List<String>> roleIdToXtbIdList,
		Map<String, String> xtbIdToName) {
		List<ExportDsVo.RoleAccountVo> roleAccountVoList = new ArrayList<>();
		Map<String, String> roleId2Name = new HashMap<>();
		tbPermissionService.getRoleInfoByIds(roleId2Name, pmsRoleListVo.getRoleAccount());
		for (String roleAccountId: pmsRoleListVo.getRoleAccount()) {

			// 构造tbList
			List<String> xtbVoList = new ArrayList<>();
			List<String> xtbIdList = roleIdToXtbIdList.get(roleAccountId);
			for (String xtbId: xtbIdList) {
				if (!xtbIdToName.get(xtbId).isEmpty()) {
					xtbVoList.add(xtbIdToName.get(xtbId));
				}
			}
			roleAccountVoList.add(
				ExportDsVo.RoleAccountVo
					.builder()
					.roleName(roleId2Name.get(roleAccountId))
					.roleAccountId(roleAccountId)
					.tbList(xtbVoList).build()
			);
		}
		return roleAccountVoList;
	}

	public List<ExportDsVo.ChatVo> getChatPmsList(
		ExportDsVo.ExportTbPmsRoleListVo pmsRoleListVo,
		Map<String, List<String>> roleIdToXtbIdList,
		Map<String, String> xtbIdToName) {
		List<ExportDsVo.ChatVo> chatVoList = new ArrayList<>();
		Map<String, String> roleId2Name = new HashMap<>();
		tbPermissionService.getChatInfoByIds(roleId2Name, pmsRoleListVo.getChat());
		for (String chatId: pmsRoleListVo.getChat()) {

			// 构造tbList
			List<String> xtbVoList = new ArrayList<>();
			List<String> xtbIdList = roleIdToXtbIdList.get(chatId);
			for (String xtbId: xtbIdList) {
				if (!xtbIdToName.get(xtbId).isEmpty()) {
					xtbVoList.add(xtbIdToName.get(xtbId));
				}
			}

			chatVoList.add(ExportDsVo.ChatVo.builder().chatName(roleId2Name.get(chatId)).chatId(chatId).tbList(xtbVoList).build());
		}
		return chatVoList;
	}

	public Map<String, List<String>> getRoleIdToXtbIdList(String dsId) {
		// 构建roleId和xtbId关系
		Optional<List<ExportTbPermissionBean>> expTbPmsBeanListOptional = expTbPmsRepository.findAllByDsId(dsId);
		expTbPmsBeanListOptional.orElseThrow(() -> new DatabridgeException("数据源下没有目的表"));
		Map<String, List<String>> roleIdToXtbIdList = new HashMap<>();
		for (ExportTbPermissionBean expTbPmsBean: expTbPmsBeanListOptional.get()) {
			if (!ObjectUtils.isEmpty(roleIdToXtbIdList) && roleIdToXtbIdList.containsKey(expTbPmsBean.getRoleId())) {
				roleIdToXtbIdList.get(expTbPmsBean.getRoleId()).add(expTbPmsBean.getXtbId());
			} else {
				List<String> expTbPmsList = new ArrayList<>();
				expTbPmsList.add(expTbPmsBean.getXtbId());
				roleIdToXtbIdList.put(expTbPmsBean.getRoleId(), expTbPmsList);
			}
		}
		return roleIdToXtbIdList;
	}

	public Map<String, String> getXtbId2Name(String dsId) {
		Optional<List<ExportTbPermissionBean>> expTbPmsBeanListOptional = expTbPmsRepository.findAllByDsId(dsId);
		expTbPmsBeanListOptional.orElseThrow(() -> new DatabridgeException("数据源下没有目的表"));
		// 获取数据下所有xtbId
		List<String> xtbList = new ArrayList<>();
		for (ExportTbPermissionBean expTbPmsBean: expTbPmsBeanListOptional.get()) {
			xtbList.add(expTbPmsBean.getXtbId());
		}
		// 构建xtbId和xtbName关系
		Optional<List<ExportDsTbBean>> exportDsTbBeanListOptional = exportDsTbRepository.findXtbInfos(xtbList);
		Map<String, String> xtbIdToName = new HashMap<>();
		if (exportDsTbBeanListOptional.isPresent()) {
			for (ExportDsTbBean exportDsTbBean: exportDsTbBeanListOptional.get()) {
				xtbIdToName.put(exportDsTbBean.getXtbId(), exportDsTbBean.getName());
			}
		}
		return xtbIdToName;
	}


	/**
	* @Description //获取导出数据表被授权给了哪些用户
	* @Date 2020/12/22 9:09 下午
	* @param dsId
	* @return com.haizhi.hora.bean.vo.ExportDsVo.ExportTbPmsRoleListVo
	**/
	public ExportDsVo.ExportTbPmsRoleListVo pmsRoleList(String dsId) {
		// 获取各自类型下的roleId
		HashSet<String> userSet =  new HashSet<>();
		HashSet<String> groupSet = new HashSet<>();
		HashSet<String> roleSet = new HashSet<>();
		HashSet<String> chatSet = new HashSet<>();

		Optional<List<ExportTbPermissionBean>> expTbPmsBeanListOptional = expTbPmsRepository.findAllByDsId(dsId);
		if (expTbPmsBeanListOptional.isPresent()) {
			for (ExportTbPermissionBean expTbPmsBean: expTbPmsBeanListOptional.get()) {
				if (expTbPmsBean.getRoleType().equals(ROLE_TYPE_USER)) {
					userSet.add(expTbPmsBean.getRoleId());
				} else if (expTbPmsBean.getRoleType().equals(ROLE_TYPE_GROUP)) {
					groupSet.add(expTbPmsBean.getRoleId());
				} else if (expTbPmsBean.getRoleType().equals(ROLE_TYPE_ROLEACCOUNT)) {
					roleSet.add(expTbPmsBean.getRoleId());
				} else if (expTbPmsBean.getRoleType().equals(ROLE_TYPE_CHAT)) {
					chatSet.add(expTbPmsBean.getRoleId());
				}
			}
		}
		return ExportDsVo.ExportTbPmsRoleListVo
			.builder()
			.user(new ArrayList<>(userSet))
			.userGroup(new ArrayList<>(groupSet))
			.roleAccount(new ArrayList<>(roleSet))
			.chat(new ArrayList<>(chatSet))
			.build();
	}

	public List<ExportDsVo.XtbVo> pmsTbList(String dsId, String roleId) {
		List<ExportDsVo.XtbVo> result = new ArrayList<>();
		Optional<List<ExportTbPermissionBean>> optionalExportTbPermissionBeanList = expTbPmsRepository.findAllByDsIdAndRoleId(dsId, roleId);
		List<String> xtbIdList = optionalExportTbPermissionBeanList
			.map(
				exportTbPermissionBeanList -> exportTbPermissionBeanList.stream()
					.map(ExportTbPermissionBean::getXtbId)
					.collect(Collectors.toList()))
			.orElseGet(ArrayList::new);
		if (!xtbIdList.isEmpty()) {
			Optional<List<ExportDsTbBean>> optionalExportDsTbBeanList = exportDsTbRepository.findXtbInfos(xtbIdList);
			result = optionalExportDsTbBeanList
				.map(
					exportDsTbBeans -> exportDsTbBeans
						.stream()
						.map(exportDsTbBean -> ExportDsVo.XtbVo
							.builder()
							.xtbId(exportDsTbBean.getXtbId())
							.xtbName(exportDsTbBean.getName())
							.build()
				).collect(Collectors.toList())).orElseGet(ArrayList::new);
		}
		return  result;
	}

	public void pmsRoleDelete(String dsId, String roleId) {
		expTbPmsRepository.logicDeleteByDsIdAndRoleId(dsId, roleId);
	}

	public void pmsRoleModify(ExportDsForm.ExportDsPmsRoleModifyForm form) {
		String roleId = form.getRoleId();
		Integer roleType = form.getRoleType();
		String dsId = form.getDsId();
		if (!ObjectUtils.isEmpty(form.getAddTableList())) {
			for (String xtbId: form.getAddTableList()) {
				addPms(dsId, xtbId, roleId, roleType, getOwner());
			}
		}
		if (!ObjectUtils.isEmpty(form.getDelTableList())) {
			for (String xtbId: form.getDelTableList()) {
				expTbPmsRepository.logicDeleteByXtbIdAndRoleId(xtbId, roleId);
			}
		}
	}

	private void addPms(String dsId, String xtbId, String roleId, Integer roleType, String owner) {
		ExportTbPermissionBean exportTbPermissionBean = new ExportTbPermissionBean();
		exportTbPermissionBean.setDsId(dsId);
		exportTbPermissionBean.setRoleId(roleId);
		exportTbPermissionBean.setOperator(owner);
		exportTbPermissionBean.setXtbId(xtbId);
		exportTbPermissionBean.setRoleType(roleType);
		expTbPmsRepository.save(exportTbPermissionBean);
	}

	public List<String> getConnectorTableList(ExportDsForm.ConfigForm form) throws UnsupportedEncodingException {
		List<String> tableList = new ArrayList<>();
		boolean isSecurity = false;
		boolean isNetSSL = false;
		if (!ObjectUtils.isEmpty(form.getIsSecurity()) && !ObjectUtils.isEmpty(form.getIsNetSSL())) {
			if (form.getIsSecurity() == 1) {
				isSecurity = true;
			}
			if (form.getIsNetSSL() == 1) {
				isNetSSL = true;

			}
		}
		String dbType = genTypeToDbNameMap().get(form.getType());

		try {
			tableList = dmcTableApi.jobGetTables(genTypeToDbNameMap().get(form.getType()), getDsUrl(form.getUrl(),
				form.getPort(), form.getType()), form.getUser(), PasswordUtils.decodeBase64(form.getPassword()),
				form.getDbName(), dbType, isSecurity, isNetSSL,
				Integer.valueOf(!ObjectUtils.isEmpty(form.getVersion()) ? form.getVersion() : "0"));
		} catch (Exception e) {
			throw new DatabridgeException(StatusCode.CONN_ERROR, "连接失败.");
		}
		return tableList;
	}

	public List<String> getConnectorTables(String dsId) {
		Optional<DsBean> dsBeanOptional = dsRepository.findByDsId(dsId);
		dsBeanOptional.orElseThrow(() -> new DatabridgeException("导出表所依赖的数据源已不存在"));
		DsBean dsBean = dsBeanOptional.get();
		boolean isSecurity = false;
		boolean isNetSSL = false;
		if (!ObjectUtils.isEmpty(dsBean.getIsSecurity()) && !ObjectUtils.isEmpty(dsBean.getIsNetSSL())) {
			if (dsBean.getIsSecurity() == 1) {
				isSecurity = true;
			}
			if (dsBean.getIsNetSSL() == 1) {
				isNetSSL = true;

			}
		}
		String dbType = genTypeToDbNameMap().get(dsBean.getType());

		return dmcTableApi.jobGetTables(genTypeToDbNameMap().get(dsBean.getType()), getDsUrl(dsBean.getHost(),
			dsBean.getPort(), dsBean.getType()), dsBean.getUsername(), dsBean.getPassword(),
			dsBean.getDatabase(), dbType, isSecurity, isNetSSL,
			Integer.valueOf(!ObjectUtils.isEmpty(dsBean.getVersionInfo()) ? dsBean.getVersionInfo() : "0"));
	}

	public Object getConnectorTableSchemaList(ExportDsForm.ConfigForm form) throws UnsupportedEncodingException {
		boolean isSecurity = false;
		boolean isNetSSL = false;
		if (!ObjectUtils.isEmpty(form.getIsSecurity()) && !ObjectUtils.isEmpty(form.getIsNetSSL())) {
			if (form.getIsSecurity() == 1) {
				isSecurity = true;
			}
			if (form.getIsNetSSL() == 1) {
				isNetSSL = true;

			}
		}
		String dbType = genTypeToDbNameMap().get(form.getType());
		return dmcTableApi.jobGetTableSchema(genTypeToDbNameMap().get(form.getType()),
			getDsUrl(form.getUrl(), form.getPort(), form.getType()), form.getUser(),
			PasswordUtils.decodeBase64(form.getPassword()),
			form.getDbName(), form.getTbName(), dbType, isSecurity, isNetSSL,
			Integer.valueOf(!ObjectUtils.isEmpty(form.getVersion()) ? form.getVersion() : "0"));
	}

	public GetTableSchemaResp fieldList(String xtbId) {
		Optional<ExportDsTbBean> exportDsTbBeanOptional = exportDsTbRepository.findByXtbId(xtbId);
		ExportDsTbBean exportDsTbBean = exportDsTbBeanOptional.orElseThrow(() -> new DatabridgeException("导出表已不存在"));
		Optional<DsBean> dsBeanOptional = dsRepository.findByDsId(exportDsTbBean.getDsId());
		DsBean dsBean = dsBeanOptional.orElseThrow(() -> new DatabridgeException("导出表所依赖的数据源已不存在"));
		boolean isSecurity = false;
		boolean isNetSSL = false;
		if (!ObjectUtils.isEmpty(dsBean.getIsSecurity()) && !ObjectUtils.isEmpty(dsBean.getIsNetSSL())) {
			if (dsBean.getIsSecurity() == 1) {
				isSecurity = true;
			}
			if (dsBean.getIsNetSSL() == 1) {
				isNetSSL = true;

			}
		}
		String dbType = genTypeToDbNameMap().get(dsBean.getType());
		GetTableSchemaResp getTableSchemaResp = dmcTableApi.jobGetTableSchema(genTypeToDbNameMap().get(dsBean.getType()),
			getDsUrl(dsBean.getHost(), dsBean.getPort(), dsBean.getType()), dsBean.getUsername(), dsBean.getPassword(),
			dsBean.getDatabase(), exportDsTbBean.getName(), dbType, isSecurity, isNetSSL,
			Integer.valueOf(!ObjectUtils.isEmpty(dsBean.getVersionInfo()) ? dsBean.getVersionInfo() : "0"));
		Optional<List<ExportTbRelRuleBean>> optionalExportTbRelRuleBeanList = exportTbRelRuleRepository.findAllByXtbId(xtbId);
		List<String> hasRuleFieldList = new ArrayList<>();
		if (optionalExportTbRelRuleBeanList.isPresent()) {
			List<ExportTbRelRuleBean> exportTbRelRuleBeanList = optionalExportTbRelRuleBeanList.get();
			hasRuleFieldList = exportTbRelRuleBeanList.stream().map(ExportTbRelRuleBean::getFieldName).collect(Collectors.toList());
		}
		for (Map<String, Object> fieldInfo: getTableSchemaResp.getFields()) {
			if (hasRuleFieldList.contains(fieldInfo.getOrDefault("fieldName", ""))) {
				fieldInfo.put("hasRule", true);
			} else {
				fieldInfo.put("hasRule", false);
			}
		}
		return getTableSchemaResp;
	}

	public String getDsUrl(String host, Integer port, Integer dbType) {
		String url = "";
		if (dbType.equals(MYSQL) || dbType.equals(POSTGRESQL) || dbType.equals(GREENPLUM)) {
			url = host + ":" +  port.toString();
		} else if (dbType.equals(DATAHUB)) {
			url = host;
		} else {
			throw new DatabridgeException("此数据源类型暂不支持");
		}
		return url;
	}

	public String xtbCreate(String tbName, String dsId, String owner, String entId) {
		String xtbId = genKey("xtb");
		ExportDsTbBean exportDsTbBean = new ExportDsTbBean();
		exportDsTbBean.setXtbId(xtbId);
		exportDsTbBean.setName(tbName);
		exportDsTbBean.setTitle(tbName);
		exportDsTbBean.setOwner(getOwner());
		exportDsTbBean.setDsId(dsId);
		exportDsTbBean.setEntId(entId);
		exportDsTbBean.setOwner(owner);
		exportDsTbRepository.save(exportDsTbBean);
		addPms(dsId, xtbId, owner, ROLE_TYPE_USER, owner);
		return xtbId;

	}

	public void xtbDelete(String tbId) {
		exportDsTbRepository.logicDeleteByXtbId(tbId);
		expTbPmsRepository.logicDeleteByXtbId(tbId);
	}

	public Map<Integer, String> genTypeToDbNameMap() {
		Map<Integer, String> typeToDbNameMap = new HashMap<>();
		typeToDbNameMap.put(MYSQL, "mysql");
		typeToDbNameMap.put(POSTGRESQL, "postgresql");
		typeToDbNameMap.put(DATAHUB, "datahub");
		typeToDbNameMap.put(GREENPLUM, "greenplum");
		return typeToDbNameMap;
	}

	public void tbRuleCreate(ExportDsForm.TbRuleAddForm form) {
		ExportTbRelRuleBean exportTbRelRuleBean = new ExportTbRelRuleBean();
		exportTbRelRuleBean.setFieldName(form.getFieldName());
		exportTbRelRuleBean.setRuleId(form.getRuleId());
		exportTbRelRuleBean.setOperator(getOwner());
		exportTbRelRuleBean.setXtbId(form.getXtbId());
		exportTbRelRuleBean.setRelaId(genKey("rela"));
		exportTbRelRuleRepository.save(exportTbRelRuleBean);
	}

	public ExportDsVo.FieldNameRelRuleVo tbRuleList(ExportDsForm.TbRuleListForm form) {
		Optional<List<ExportTbRelRuleBean>> exportTbRelRuleBeanListOptional = exportTbRelRuleRepository.findAllByXtbId(form.getXtbId());

		List<ExportDsVo.FieldNameRelRuleInfoVo> xtbRelRuleList = new ArrayList<>();
		if (exportTbRelRuleBeanListOptional.isPresent()) {
			for (ExportTbRelRuleBean exportTbRelRuleBean: exportTbRelRuleBeanListOptional.get()) {
				xtbRelRuleList.add(
					ExportDsVo.FieldNameRelRuleInfoVo
						.builder()
						.relaId(exportTbRelRuleBean.getRelaId())
						.fieldName(exportTbRelRuleBean.getFieldName())
						.ruleId(exportTbRelRuleBean.getRuleId())
						.build()
				);
			}
		}
		ExportDsVo.DsInfoVo dsInfoVo = null;
		ExportDsVo.XtbInfoVo xtbInfoVo = null;
		Optional<ExportDsTbBean> exportDsTbBeanOptional = exportDsTbRepository.findByXtbId(form.getXtbId());
		if (exportDsTbBeanOptional.isPresent()) {
			String dsId = exportDsTbBeanOptional.get().getDsId();
			Optional<DsBean> dsBeanOptional = dsRepository.findByDsId(dsId);
			if (dsBeanOptional.isPresent()) {
				dsInfoVo = ExportDsVo.DsInfoVo.builder().dsId(dsId).dsName(dsBeanOptional.get().getName()).build();
			}
			xtbInfoVo = ExportDsVo.XtbInfoVo.builder().xtbId(form.getXtbId()).xtbName(exportDsTbBeanOptional.get().getName()).build();
		}
		return ExportDsVo.FieldNameRelRuleVo.builder().dsInfo(dsInfoVo).xtbInfo(xtbInfoVo).xtbRuleList(xtbRelRuleList).build();
	}


	public void tbRuleModify(ExportDsForm.TbRuleModifyForm form) {
		if (!ObjectUtils.isEmpty(form.getAddRuleList())) {
			for (ExportDsForm.TbRuleAddForm addForm: form.getAddRuleList()) {
				addForm.setXtbId(form.getXtbId());
				tbRuleCreate(addForm);
			}
		}
		if (!ObjectUtils.isEmpty(form.getModifyRuleList())) {
			for (ExportDsForm.TbRuleUpdateForm updateForm: form.getModifyRuleList()) {
				Optional<ExportTbRelRuleBean> exportTbRelRuleBeanOptional = exportTbRelRuleRepository.findByRelaId(
					updateForm.getRelaId()
				);
				if (exportTbRelRuleBeanOptional.isPresent()) {
					ExportTbRelRuleBean exportTbRelRuleBean = exportTbRelRuleBeanOptional.get();
					exportTbRelRuleBean.setXtbId(form.getXtbId());
					exportTbRelRuleBean.setRelaId(updateForm.getRelaId());
					exportTbRelRuleBean.setRuleId(updateForm.getRuleId());
					exportTbRelRuleBean.setFieldName(updateForm.getFieldName());
					exportTbRelRuleRepository.save(exportTbRelRuleBean);
				}
			}
		}

		if (!ObjectUtils.isEmpty(form.getDelRuleList())) {
			for (String relaId: form.getDelRuleList()) {
				tbRuleDelete(relaId);
			}
		}

	}

	public void tbRuleDelete(String relId) {
		exportTbRelRuleRepository.logicDeleteByRelId(relId);
	}

	public ExportDsVo.XtbInfoVo getXtbInfo(String xtbId) {
		Optional<ExportDsTbBean> exportDsTbBeanOptional = exportDsTbRepository.findByXtbId(xtbId);
		if (exportDsTbBeanOptional.isPresent()) {
			ExportDsTbBean exportDsTbBean = exportDsTbBeanOptional.get();
			return ExportDsVo.XtbInfoVo.builder().xtbId(exportDsTbBean.getXtbId()).xtbName(exportDsTbBean.getName()).build();
		} else {
			return null;
		}
	}
}
