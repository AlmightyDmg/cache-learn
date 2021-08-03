package com.haizhi.databridge.service.export;

import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerType.CRON;
import static com.haizhi.databridge.constants.DataSourceConstants.SchedulerType.NORMAL;
import static com.haizhi.databridge.constants.DataSourceConstants.TaskType.EXPORT;
import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_CREATE;
import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_STOP;
import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_SYNC;
import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_TABLE_CREATE;
import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_TABLE_QUEUE;
import static com.haizhi.databridge.constants.DatabridgeConstants.M_PER_S;
import static com.haizhi.databridge.constants.MetaConstants.DsType.DATAHUB;
import static com.haizhi.databridge.constants.MetaConstants.DsType.GREENPLUM;
import static com.haizhi.databridge.constants.MetaConstants.DsType.MYSQL;
import static com.haizhi.databridge.constants.MetaConstants.DsType.POSTGRESQL;
import static com.haizhi.databridge.constants.MetaConstants.Job.JOB_SOURCE_FROM_DMC;
import static com.haizhi.databridge.util.CronUtils.toQuartsCron;
import static com.haizhi.databridge.util.IdUtils.genKey;
import static com.haizhi.databridge.util.JsonUtils.toJson;
import static com.haizhi.databridge.util.JsonUtils.toObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.exportdata.DsBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportDsTbBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportLogBean;
import com.haizhi.databridge.bean.domain.exportdata.JobBean;
import com.haizhi.databridge.bean.domain.importdata.TblTransTaskRelBean;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.bean.vo.ExportDsVo;
import com.haizhi.databridge.bean.vo.ExportJobVo;
import com.haizhi.databridge.client.xxljob.JobClientApi;
import com.haizhi.databridge.client.xxljob.request.DataTransJobParam;
import com.haizhi.databridge.config.DmcClientProperties;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.exportdata.DsRepository;
import com.haizhi.databridge.repository.exportdata.ExportDsTbRepository;
import com.haizhi.databridge.repository.exportdata.ExportLogRepository;
import com.haizhi.databridge.repository.exportdata.JobRepository;
import com.haizhi.databridge.repository.importdata.TblTransTaskRelRepository;
import com.haizhi.databridge.util.CronUtils;
import com.haizhi.databridge.util.JsonUtils;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.util.SpringUtils;
import com.haizhi.databridge.web.controller.form.ExportJobForm;
import com.haizhi.databridge.web.controller.form.JobStateForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;

/**
 * @author zhaohuanhuan
 * @version 1.0
 * @create 12/18/20 4:00 下午
 **/

@Component
@Service
@Log4j2
public class ExportJobService extends RequestCommonData {
	private static final int FAILED = 3;
	private static final int SUCCESS = 2;

	@Autowired
	private DsRepository dsRepository;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private ExportLogRepository exportLogRepo;

	@Autowired
	private ExportDsService exportDsService;

	@Autowired
	private JobClientApi jobClientApi;

	@Autowired
	private DmcClientProperties dmcProp;

	@Autowired
	private TblTransTaskRelRepository tblTransTaskRelRepo;

	@Autowired
	private ExportDsTbRepository exportTbRepo;

	@Autowired
	private RuleService ruleService;

	/**
	* @Description //导出任务列表展示
	* @Date 2021/1/12 6:03 下午
	* @param
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportJobVo.JobInfoVo>
	**/
	public List<ExportJobVo.JobInfoVo> jobList() {

		List<ExportJobVo.JobInfoVo> result = new ArrayList<>();

		Optional<List<JobBean>> jobBeanListOptional = jobRepository.findAllByUserIdAndJobSource(getUserId(), JOB_SOURCE_FROM_DMC);
		if (!jobBeanListOptional.isPresent()) {
			return result;
		}
		List<JobBean> jobBeanList = jobBeanListOptional.get();

		if (!ObjectUtils.isEmpty(jobBeanList)) {
			// 批量获取导出表的信息、数据源信息、模型信息
			List<String> tbIdList = jobBeanList.stream().map(JobBean::getRelaId).collect(Collectors.toList());
			List<String> jobIdList = jobBeanList.stream().map(JobBean::getJobId).collect(Collectors.toList());
			List<String> dsIdList = jobBeanList.stream().map(JobBean::getDsId).collect(Collectors.toList());
			// 获取tb信息并构建tbIdToTbInfoBeanMap
//			Map<String, TbBean> tbIdToTbInfoBeanMap = tbId2TbInfoBeanMap(tbIdList);

			// 获取ds信息并构建dsIdToDsInfoBeanMap
			Map<String, DsBean> dsId2DsInfoBeanMap = getDsId2DsInfoBeanMap(dsIdList);
			// 获取模型信息并构建tbIdToflowInfoBeanMap
//			Map<String, DataflowBean> tbId2DataflowBeanMap = getTbId2DataflowBeanMap(tbIdList);

			//构建返回内容
			// 降序
			Collections.reverse(jobBeanList);
			for (JobBean jobBean: jobBeanList) {
				ExportJobVo.DsInfoVo dsInfoVo = ExportJobVo.DsInfoVo.builder()
					.dsId(dsId2DsInfoBeanMap.getOrDefault(jobBean.getDsId(), new DsBean()).getDsId())
					.type(dsId2DsInfoBeanMap.getOrDefault(jobBean.getDsId(), new DsBean()).getType())
					.dsName(dsId2DsInfoBeanMap.getOrDefault(jobBean.getDsId(), new DsBean()).getName()).build();
//				ExportJobVo.FlowInfoVo flowInfoVo = Optional.ofNullable(tbId2DataflowBeanMap.get(jobBean.getRelaId()))
//					.map(dataflowBean -> ExportJobVo.FlowInfoVo.builder()
//						.flowId(dataflowBean.getFlowId()).flowName(dataflowBean.getName()).build()).orElse(null);

				ExportJobVo.ExportModeVo exportModeVo = ObjectUtils.isArray(jobBean.getExportMode()) ? null : toObject(
					jobBean.getExportMode(), ExportJobVo.ExportModeVo.class);

				ExportJobVo.ExportDataCountVo exportDataCountVo = ObjectUtils.isEmpty(
					jobBean.getCount()) ? null : toObject(jobBean.getCount(), ExportJobVo.ExportDataCountVo.class);
				ExportJobVo.SchedulerConfVo schedulerConfVo = getSyncConfig(jobBean);
				ExportDsVo.XtbInfoVo xtbInfoVo = exportDsService.getXtbInfo(jobBean.getXtbId());
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				String xtbId = !ObjectUtils.isEmpty(xtbInfoVo) ? xtbInfoVo.getXtbId() : null;
				String xtbName = !ObjectUtils.isEmpty(xtbInfoVo) ? xtbInfoVo.getXtbName() : null;
				result.add(ExportJobVo.JobInfoVo.builder()
								.jobId(jobBean.getJobId())
								.tbVo(ExportJobVo.TbVo.builder().tbId(jobBean.getRelaId()).build())
								.dsInfo(dsInfoVo)
								.xTbVo(ExportDsVo.XtbVo.builder().xtbId(xtbId).xtbName(xtbName).build())
								.fieldsMappingInfoVo(JsonUtils.toObject(jobBean.getConfig(),
										ExportJobVo.FieldsMappingInfoVo.class))
								.exportMode(exportModeVo)
								.exportTime(ft.format(jobBean.getEtime()))
								.schedulerConf(schedulerConfVo)
								.status(jobBean.getStatus())
								.count(exportDataCountVo)
								.exportFailureStrategy(jobBean.getExportFailureStrategy())
								.errorMsg(jobBean.getErrorMsg())
								.build());
			}
		}
		return result;
	}

	public ExportJobVo.SchedulerConfVo getSyncConfig(JobBean jobBean) {
		ExportJobVo.SchedulerConfVo schedulerConfVo = null;
		if (ObjectUtils.isEmpty(jobBean.getSyncConfigBack())) {
			schedulerConfVo = ExportJobVo.SchedulerConfVo.builder()
				.mode(jobBean.getExecuteMode())
				.syncConfig(jobBean.getCrontab()).build();
		} else {
			schedulerConfVo = toObject(jobBean.getSyncConfigBack(), ExportJobVo.SchedulerConfVo.class);
		}
		return schedulerConfVo;
	}

//	/**
//	* @Description // 构建工作表的tb_id与工作表的bean的映射关系
//	* @Date 2021/1/12 6:03 下午
//	* @param tbIdList
//	* @return java.util.Map<java.lang.String,com.haizhi.hora.bean.domain.TbBean>
//	**/
//	private Map<String, TbBean> tbId2TbInfoBeanMap(List<String> tbIdList) {
//		// 获取tb信息并构建tbIdToTbInfoBeanMap
//		Map<String, TbBean> tbId2TbInfoBeanMap = new HashMap<>();
//		List<TbBean> tbBeanList = tbRepo.findByTbIdIn(tbIdList, TbBean.class);
//		if (!ObjectUtils.isEmpty(tbBeanList)) {
//			for (TbBean tbBean: tbBeanList) {
//				tbId2TbInfoBeanMap.put(tbBean.getTbId(), tbBean);
//			}
//		}
//		return tbId2TbInfoBeanMap;
//	}

//	/**
//	* @Description //构建工作表tb_id与模型bean的映射关系
//	* @Date 2021/1/12 6:04 下午
//	* @param tbIdList
//	* @return java.util.Map<java.lang.String,com.haizhi.hora.bean.domain.DataflowBean>
//	**/
//	private Map<String, DataflowBean> getTbId2DataflowBeanMap(List<String> tbIdList) {
//		List<AdvViewRelationBean> advViewRelationBeanList = advViewRelationRepo.findByOwnerAndTbIdInAndRelaType(
//			getOwner(), tbIdList, MetaConstants.AdvViewRelation.RELA_TYPE_OUTPUT);
//		Map<String, String> tbIdToDataflowIdMap = new HashMap<>();
//		for (AdvViewRelationBean advViewRelationBean: advViewRelationBeanList) {
//			tbIdToDataflowIdMap.put(advViewRelationBean.getTbId(), advViewRelationBean.getAdvId());
//		}
//		List<String> flowIdList = new ArrayList<>();
//		for (AdvViewRelationBean advViewRelationBean: advViewRelationBeanList) {
//			flowIdList.add(advViewRelationBean.getAdvId());
//		}
//		Optional<List<DataflowBean>> dataflowBeanListOptional = dataflowRepository.findAllByOwnerAndFlowIdIn(
//			getOwner(), flowIdList, DataflowBean.class);
//		Map<String, DataflowBean> flowIdToDataflowBeanMap = new HashMap<>();
//		if (dataflowBeanListOptional.isPresent()) {
//			for (DataflowBean dataflowBean: dataflowBeanListOptional.get()) {
//				flowIdToDataflowBeanMap.put(dataflowBean.getFlowId(), dataflowBean);
//			}
//		}
//		Map<String, DataflowBean> tbId2DataflowBeanMap = new HashMap<>();
//		for (String tbId: tbIdList) {
//			String flowId = tbIdToDataflowIdMap.get(tbId);
//			if (!ObjectUtils.isEmpty(flowId)) {
//				tbId2DataflowBeanMap.put(tbId, flowIdToDataflowBeanMap.get(flowId));
//			}
//		}
//		return tbId2DataflowBeanMap;
//	}

	/**
	* @Description //构建数据源ds_id与数据源bean的映射关系
	* @Date 2021/1/12 6:05 下午
	* @param dsIdList
	* @return java.util.Map<java.lang.String,com.haizhi.hora.bean.domain.DsBean>
	**/
	private Map<String, DsBean> getDsId2DsInfoBeanMap(List<String> dsIdList) {
		Map<String, DsBean> dsId2DsInfoBeanMap = new HashMap<>();
		List<DsBean> dsBeanList = dsRepository.findByDsIdInAndSourceType(dsIdList, 1);
		if (!ObjectUtils.isEmpty(dsBeanList)) {
			for (DsBean dsBean: dsBeanList) {
				dsId2DsInfoBeanMap.put(dsBean.getDsId(), dsBean);
			}
		}
		return dsId2DsInfoBeanMap;
	}

	/**
	* @Description //任务创建
	* @Date 2021/1/12 6:06 下午
	* @param form
	* @return void
	**/
	@Transactional
	public void jobCreate(ExportJobForm.ExportJobCreateForm form) {
		// 获取数据源信息
		DsBean dsBean = dsRepository.findByDsId(form.getDsId())
				.orElseThrow(() -> new DatabridgeException("ds_id不存在"));

		if (!ObjectUtils.isEmpty(form.getSchedulerConf().getSyncConfig())) {
			checkCrontabValid(toQuartsCron(form.getSchedulerConf().getSyncConfig()));
		}
		ExportJobForm.SchedulerConfForm schedulerConfForm = form.getSchedulerConf();
		String jobId = genKey("job");
		JobBean jobBean = new JobBean();
		jobBean.setJobId(jobId);
		jobBean.setDsId(form.getDsId());
		jobBean.setUserId(getUserId());
		jobBean.setEntId(getEnterpriseId());
		jobBean.setConfig(toJson((form.getFieldMappingConfig())));
		jobBean.setType(dsBean.getType());
		jobBean.setRelaId(form.getTbId());
		jobBean.setSyncConfigBack(toJson(form.getSchedulerConf()));
		jobBean.setExecuteMode(form.getSchedulerConf().getMode());
		jobBean.setExportMode(toJson(form.getExportMode()));
		jobBean.setExportFailureStrategy(form.getExportFailureStrategy());
		jobBean.setDsId(dsBean.getDsId());
		jobBean.setEtime(Timestamp.valueOf(LocalDateTime.now()));
		jobBean.setXtbId(getXtbId(form));
		jobBean.setJobSource(JOB_SOURCE_FROM_DMC);
		jobBean.setStatus(EXPORT_TABLE_CREATE);
		jobRepository.save(jobBean);

		// create xxljob
		String cronType = getExecuteMode(form.getSchedulerConf().getMode());
		jobClientApi.add(jobId, toQuartsCron(schedulerConfForm.getSyncConfig()),
				cronType,
				DataTransJobParam.builder().jobType(EXPORT).jobId(jobId).build());

		if ("CRON".equalsIgnoreCase(cronType) && !ObjectUtils.nullSafeEquals(jobBean.getStatus(), EXPORT_STATUS_STOP)) {
			jobClientApi.start(jobId);
		}

		// 创建完默认执行一次
		jobClientApi.trigger(jobId, DataTransJobParam.builder().jobId(jobId).jobType(EXPORT).build());

		jobRepository.updateJob(jobId, EXPORT_TABLE_QUEUE);
	}

	private String getXtbId(ExportJobForm.ExportJobCreateForm form) {
		String xtbId = "";
		if (ObjectUtils.isEmpty(form.getXtbId())) {
			xtbId = exportDsService.xtbCreate(form.getXtbName(), form.getDsId(), getOwner(), getEnterpriseId());
		} else {
			xtbId = form.getXtbId();
		}
		return xtbId;
	}

	private String getExecuteMode(Integer schedulerMode) {
		String executeMode = "NONE";
		if (schedulerMode.equals(0) || schedulerMode.equals(1)) {
			executeMode = "CRON";
		}
		return executeMode;
	}

	public void checkCrontabValid(String crontab) {
		if (!CronUtils.isValidExpression(crontab)) {
			throw new DatabridgeException("cron invalid");
		}
	}

	/**
	* @Description //任务修改
	* @Date 2021/1/12 6:06 下午
	* @param form
	* @return void
	**/
	@Transactional
	public void jobModify(ExportJobForm.ExportJobModifyForm form) {
		String jobId = form.getJobId();
		Optional<JobBean> jobBeanOptional = jobRepository.findByJobId(jobId);
		JobBean jobBean = jobBeanOptional.orElseThrow(() -> new DatabridgeException("未找到job信息"));
		// 导出任务编辑逻辑
		String dsId = form.getDsId();
		ExportJobForm.ExportModeForm modeForm = form.getExportMode();
		ExportJobForm.FieldMappingConfigForm fieldMappingConfigForm = form.getFieldMappingConfig();
		if (!ObjectUtils.isEmpty(jobId)
			&& !ObjectUtils.isEmpty(dsId)
			&& !ObjectUtils.isEmpty(modeForm)
			&& !ObjectUtils.isEmpty(fieldMappingConfigForm)
		) {
			jobBean.setDsId(dsId);
			jobBean.setExportMode(toJson(modeForm));
			jobBean.setConfig(toJson(fieldMappingConfigForm));
			if (ObjectUtils.isEmpty(form.getXtbId())) {
				String xtbId = exportDsService.xtbCreate(form.getXtbName(), form.getDsId(), getOwner(), getEnterpriseId());
				jobBean.setXtbId(xtbId);
			} else {
				jobBean.setXtbId(form.getXtbId());
			}
			if (!ObjectUtils.isEmpty(form.getSchedulerConf())) {
				jobBean.setExecuteMode(form.getSchedulerConf().getMode());
			}
		}

		// 更新设置修改
		ExportJobForm.SchedulerConfForm schedulerConfForm = form.getSchedulerConf();
		if (!ObjectUtils.isEmpty(schedulerConfForm)) {
			jobBean.setExecuteMode(form.getSchedulerConf().getMode());
			if (!ObjectUtils.isEmpty(form.getSchedulerConf().getSyncConfig())) {
				checkCrontabValid(toQuartsCron(form.getSchedulerConf().getSyncConfig()));
			}

			changeDistJob(jobBean, form);

			jobBean.setSyncConfigBack(toJson(form.getSchedulerConf()));
		}


		// 更改导出策略
		Integer expFailureStrategy = form.getExportFailureStrategy();
		if (!ObjectUtils.isEmpty(expFailureStrategy)) {
			jobBean.setExportFailureStrategy(expFailureStrategy);
		}
		jobRepository.save(jobBean);
	}

	private void changeDistJob(JobBean jobBean, ExportJobForm.ExportJobModifyForm form) {
		ExportJobVo.SchedulerConfVo oldSync = toObject(jobBean.getSyncConfigBack(), ExportJobVo.SchedulerConfVo.class);

		String jobId = jobBean.getJobId();
		if (!Objects.equals(oldSync.getMode(), form.getSchedulerConf().getMode())
				|| !Objects.equals(oldSync.getSyncConfig(), form.getSchedulerConf().getSyncConfig())) {
			String scheduleType = "";
			String cron = "";
			if (form.getSchedulerConf().getMode() == 1 || form.getSchedulerConf().getMode() == 0) {
				scheduleType = CRON;
				cron = toQuartsCron(form.getSchedulerConf().getSyncConfig());
			} else {
				scheduleType = NORMAL;
				cron = "";
			}

			if (NORMAL.equalsIgnoreCase(scheduleType)) {
				jobClientApi.stop(jobId);
			}

			jobClientApi.update(jobId, cron, scheduleType, DataTransJobParam.builder().jobType(EXPORT).jobId(jobId).build());
			if (CRON.equalsIgnoreCase(scheduleType)) {
				jobClientApi.start(jobId);
			}
		}
	}

	/**
	* @Description //获取历史任务
	* @Date 2021/1/12 6:06 下午
	* @param jobId
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportJobVo.HistoryVo>
	**/
	public List<ExportJobVo.HistoryVo> jobHistory(String jobId) {
		List<ExportJobVo.HistoryVo> result = new ArrayList<>();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Optional<List<ExportLogBean>> optionalExportLogBeanList = exportLogRepo.findAllByJobId(jobId);
		if (!optionalExportLogBeanList.isPresent()) {
			return result;
		}
		List<ExportLogBean> exportLogBeanList = optionalExportLogBeanList.get();
		// 降序
		Collections.reverse(exportLogBeanList);
		for (ExportLogBean exportLogBean: optionalExportLogBeanList.get()) {

			ExportJobVo.HistoryExportDataCountVo exportDataCountVo = null;
			if (!ObjectUtils.isEmpty(exportLogBean.getCount())) {
				exportDataCountVo = exportLogBean.getCount().isEmpty() ? null : toObject(exportLogBean.getCount(),
					ExportJobVo.HistoryExportDataCountVo.class);
			}
			result.add(ExportJobVo.HistoryVo
				.builder()
				.jobId(exportLogBean.getJobId())
				.status(exportLogBean.getStatus())
				.startTime(ObjectUtils.isEmpty(exportLogBean.getStartTime()) ? "" : ft.format(exportLogBean.getStartTime()))
				.endTime(ObjectUtils.isEmpty(exportLogBean.getEndTime()) ? "" : ft.format(exportLogBean.getEndTime()))
				.costTime(exportLogBean.getCostTime()).count(exportDataCountVo).errorMsg(exportLogBean.getErrorMsg()).build()
			);
		}
		return result;
	}

	/**
	* @Description //删除任务
	* @Date 2021/1/12 6:06 下午
	* @param jobId
	* @return void
	**/
	@Transactional
	public void jobDelete(String jobId) {
		Optional<JobBean> jobBeanOptional = jobRepository.findByJobId(jobId);
		if (jobBeanOptional.isPresent()) {
			jobRepository.logicDeleteByJobId(jobBeanOptional.get().getJobId());
			tblTransTaskRelRepo.logicDeleteByJobId(jobId);
			jobClientApi.remove(jobId);
		} else {
			throw new DatabridgeException("job 不存在");
		}
	}

	/**
	* @Description //启动任务，这里是激活调度，不是仅仅让当前任务跑起来
	* @Date 2021/1/12 6:07 下午
	* @param jobId
	* @return void
	**/
	@Transactional
	public void jobStart(String jobId) {
		jobClientApi.start(jobId);
		jobRepository.updateJob(jobId, EXPORT_STATUS_CREATE);
	}

	@Transactional
	public void jobExec(String jobId) {
		JobBean jobBean = jobRepository.findByJobId(jobId).orElseThrow(() -> new DatabridgeException("任务不存在"));
		if (jobBean.getStatus() == EXPORT_STATUS_SYNC) {
			throw new DatabridgeException("任务已在运行中，请勿重复触发");
		}

		jobBean.setStatus(EXPORT_STATUS_SYNC);
		jobRepository.update(jobBean);

		// if xxl-job not exist, then create it.
		if (!jobClientApi.isXxljobExist(jobId)) {
			ExportJobVo.SchedulerConfVo schedulerConf = toObject(jobBean.getSyncConfigBack(), ExportJobVo.SchedulerConfVo.class);
			String cronType = getExecuteMode(schedulerConf.getMode());
			jobClientApi.add(jobId, toQuartsCron(schedulerConf.getSyncConfig()), cronType,
					DataTransJobParam.builder().jobType(EXPORT).jobId(jobId).build());
			if ("CRON".equalsIgnoreCase(cronType) && !ObjectUtils.nullSafeEquals(jobBean.getStatus(), EXPORT_STATUS_STOP)) {
				jobClientApi.start(jobId);
			}
		}

		jobClientApi.trigger(jobId, DataTransJobParam.builder().jobId(jobId).jobType(EXPORT).build());
	}

	/**
	* @Description //暂停任务，这里是停掉更新策略，不是把当前运行的任务cancel掉
	* @Date 2021/1/12 6:07 下午
	* @param jobId
	* @return void
	**/
	@Transactional
	public void jobStop(String jobId) {
		jobRepository.updateJob(jobId, EXPORT_STATUS_STOP);
		jobClientApi.stop(jobId);
	}

	/**
	* @Description //通过job_id拿到任务的bean对象
	* @Date 2021/1/12 6:10 下午
	* @param jobId
	* @return com.haizhi.hora.bean.domain.JobBean
	**/
	public JobBean getJobBean(String jobId) {
		Optional<JobBean> jobBeanOptional = jobRepository.findByJobId(jobId);
		if (jobBeanOptional.isPresent()) {
			return jobBeanOptional.get();
		} else {
			throw new DatabridgeException("job 不存在");
		}
	}


	public boolean checkJobStatus(Map<String, Integer> jobStatusMap) {
		Optional<List<JobBean>> optionalJobBeans = jobRepository.findAllByUserIdAndJobSource(getUserId(), JOB_SOURCE_FROM_DMC);
		Map<String, Integer> webJobStatusMap = new HashMap<>();
		Map<String, Integer> dbJobStatusMap = new HashMap<>();
		if (optionalJobBeans.isPresent()) {
			for (JobBean jobBean: optionalJobBeans.get()) {
				dbJobStatusMap.put(jobBean.getJobId(), jobBean.getStatus());
			}
		}
		if (!ObjectUtils.isEmpty(jobStatusMap)) {
			for (String k: jobStatusMap.keySet()) {
				webJobStatusMap.put(k, jobStatusMap.get(k));
			}
		}
		return !webJobStatusMap.equals(dbJobStatusMap);
	}

	public Map<Integer, String> genTypeToDbNameMap() {
		Map<Integer, String> typeToDbNameMap = new HashMap<>();
		typeToDbNameMap.put(MYSQL, "mysql");
		typeToDbNameMap.put(POSTGRESQL, "postgresql");
		typeToDbNameMap.put(DATAHUB, "datahub");
		typeToDbNameMap.put(GREENPLUM, "greenplum");
		return typeToDbNameMap;
	}

	private DataTransJobVo.Sync.SyncCondition getSyncCond(ExportJobForm.ExportModeForm exportMode, List<InfoTbResp.TbField> fields) {
		DataTransJobVo.Sync.SyncCondition syncCon = null;
		if ("increment".equalsIgnoreCase(exportMode.getMode())) {
			for (InfoTbResp.TbField field : fields) {
				if (!ObjectUtils.nullSafeEquals(field.getName(), exportMode.getIncreateField())) {
					continue;
				}
				String synFieldType = DmcTableApi.fieldTypeMap.getOrDefault(field.getType(), "string");
				syncCon = DataTransJobVo.Sync.SyncCondition.builder().field(field.getFieldId()).fieldType(synFieldType)
						.start(DataTransJobVo.Sync.SyncCondition.Conditon.builder()
								.enable(1).operator(">").value(exportMode.getIncreateValue()).build())
						.end(DataTransJobVo.Sync.SyncCondition.Conditon.builder().enable(0).build()).build();
			}
		}

		return syncCon;
	}

	private DataTransJobVo.Reader getReader(ExportJobVo.JobConfigInnerVo jobConf, JobBean jobBean) {
		ExportJobForm.ExportModeForm exportMode =
				JsonUtils.toObject(jobBean.getExportMode(), ExportJobForm.ExportModeForm.class);

		DmcTableApi dmcTableApi = SpringUtils.getBean(DmcTableApi.class);
		InfoTbResp dmcTbInfo = dmcTableApi.getDmcTbInfo(jobBean.getRelaId());

		Map<String, InfoTbResp.TbField> fieldMap = dmcTbInfo.getFields().stream()
				.collect(Collectors.toMap(InfoTbResp.TbField::getName, x -> x));

		List<ExportJobVo.RuleFilterVo> ruleList = ruleService.getRuleInfosByXtbId(jobBean.getXtbId());
		DataTransJobVo.CheckRule checkRule = DataTransJobVo.CheckRule.builder()
				.failureStrategy(Optional.ofNullable(jobBean.getExportFailureStrategy()).orElse(0))
				.rules(Optional.ofNullable(ruleList).orElse(new ArrayList<>()).stream()
						.filter(x -> fieldMap.containsKey(x.getMappingName()))
						.map(x -> x.getCond().replace("${field}", fieldMap.get(x.getMappingName()).getFieldId()))
						.collect(Collectors.toList())).build();

		DataTransJobVo.Sync.SyncCondition syncCon = getSyncCond(exportMode, dmcTbInfo.getFields());

		int isTruncate = 0;
		if ("overwrite".equalsIgnoreCase(exportMode.getMode())) {
			isTruncate = 1;
		} else if (exportMode.getIsTruncate() != null) {
			isTruncate = Integer.parseInt(exportMode.getIsTruncate());
		}
		DataTransJobVo.Sync sync = DataTransJobVo.Sync.builder()
				.isTruncate(isTruncate).type(exportMode.getMode()).checkRule(checkRule).syncCondition(syncCon).build();

		List<DataTransJobVo.Column> fromCols = jobConf.getFieldsMapping().stream()
				.map(x -> DataTransJobVo.Column.builder()
						.name(x.getOriginalName())
						.realName(fieldMap.get(x.getOriginalName()).getFieldId())
						.type(DmcTableApi.fieldTypeMap.getOrDefault(fieldMap.get(x.getOriginalName()).getType(), "string"))
						.build())
				.collect(Collectors.toList());

		DataTransJobVo.Reader reader = DataTransJobVo.Reader.builder().tableId(jobBean.getRelaId())
				.tableName(jobBean.getRelaId()).realName(dmcTbInfo.getStorageId()).sync(sync).columns(fromCols).build();

		return reader;
	}
	/**
	 * @Description // 获取任务执行需要的信息
	 * @Date 2021/5/276 7:10 下午
	 * @param jobId
	 * @return JobParam
	 **/
	public DataTransJobVo getJobExecInfo(String jobId) {
		JobBean jobBean = jobRepository.findByJobId(jobId).orElseThrow(() -> new DatabridgeException("job not exist"));
		ExportJobVo.JobConfigInnerVo jobConf = JsonUtils.toObject(jobBean.getConfig(), ExportJobVo.JobConfigInnerVo.class);


		String taskId = null;
		if (jobBean.getStatus() == 1) {
			taskId = tblTransTaskRelRepo.findTransTask(jobBean.getJobId(), jobBean.getRelaId(), jobBean.getXtbId())
					.orElse(TblTransTaskRelBean.builder().build()).getTransTaskId();
		}


		DsBean dsBean = dsRepository.findByDsId(jobBean.getDsId()).orElseThrow(() -> new DatabridgeException("ds not exist"));
		ExportDsTbBean tbBean =
				exportTbRepo.findByXtbId(jobBean.getXtbId()).orElseThrow(() -> new DatabridgeException("xtable not exist"));

		String dmcUrl = JsonUtils.toJson(DmcConfig.builder().pentagonProp(dmcProp.getPentagon()).noahProp(dmcProp.getNoah())
				.mobiusProp(dmcProp.getMobius()).tassadarProp(dmcProp.getTassadar()).pandoraProp(dmcProp.getPandora()).build());

		String toType = Optional.ofNullable(genTypeToDbNameMap().get(dsBean.getType())).orElse("").toLowerCase();

		DataTransJobVo.Sink fromSink = DataTransJobVo.Sink.builder().url(dmcUrl).type("dmc").build();
		DataTransJobVo.Sink toSink = DataTransJobVo.Sink.builder().url(dsBean.getHost() + ":" + dsBean.getPort())
				.type(toType).username(dsBean.getUsername()).password(dsBean.getPassword()).catalog(dsBean.getDatabase())
				.build();
		List<DataTransJobVo.Column> toCols = jobConf.getFieldsMapping().stream()
				.map(x -> DataTransJobVo.Column.builder().name(x.getMappingName()).type(x.getMappingValue()).build())
				.collect(Collectors.toList());


		DataTransJobVo.Writer writer = DataTransJobVo.Writer.builder().tableId(jobBean.getXtbId())
				.tableName(tbBean.getName()).columns(toCols).build();
		DataTransJobVo.SyncUnit syncUnit = DataTransJobVo.SyncUnit.builder()
				.taskId(taskId).fromSink(fromSink).toSink(toSink).reader(getReader(jobConf, jobBean)).writer(writer).build();
		return DataTransJobVo.builder().jobType(EXPORT).jobId(jobId).userId(jobBean.getUserId())
				.exportFailureStrategy(jobBean.getExportFailureStrategy()).syncUnits(Arrays.asList(syncUnit)).build();
	}

	@Transactional
	public String updateJobStatus(JobStateForm jobStateForm) {
		JobBean jobBean = jobRepository.findByJobId(jobStateForm.getJobId())
				.orElseThrow(() -> new DatabridgeException("job not exist"));
		int status = 2;
		switch (jobStateForm.getJobStatus()) {
			case 0: status = 1; break;
			case 1: status = FAILED; break;
			case 2: status = SUCCESS; break;
			default: status = 2; break;
		}

		ExportJobVo.ExportDataCountVo countVo = ExportJobVo.ExportDataCountVo.builder()
				.allCount(jobStateForm.getAllCount())
				.appendCount(jobStateForm.getAppendCount())
				.deleteCount(jobStateForm.getDeleteCount())
				.failedCount(jobStateForm.getFailedCount())
				.updateCount(jobStateForm.getUpdateCount())
				.filterCount(jobStateForm.getFilterCount()).build();
		jobBean.setStatus(status);
		jobBean.setCount(JsonUtils.toJson(countVo));
		jobBean.setErrorMsg(jobStateForm.getErrmsg() == null ? "" : jobStateForm.getErrmsg());
		jobRepository.update(jobBean);

		if (jobStateForm.getEndTime() != null) {
			exportLogRepo.save(ExportLogBean.builder()
					.jobId(jobStateForm.getJobId())
					.startTime(new Timestamp(jobStateForm.getStartTime()))
					.status(jobStateForm.getJobStatus() == 1 ? 0 : 1)
					.endTime(new Timestamp(jobStateForm.getEndTime()))
					.costTime((int) (jobStateForm.getStartTime() - jobStateForm.getEndTime()) / M_PER_S)
					.errorMsg(jobStateForm.getErrmsg() == null ? "" : jobStateForm.getErrmsg())
					.count(JsonUtils.toJson(countVo))
					.isDel(0)
					.build());
		}

		return "success";
	}

	@Transactional
	public String updateJobTask(JobUnitStateForm form) {
		// 执行完成清空与flinktask的关系
		TblTransTaskRelBean tblTransTaskRelBean = tblTransTaskRelRepo.findTransTask(form.getJobId(),
				form.getFromTableId(), form.getToTableId()).orElse(TblTransTaskRelBean.builder()
				.jobId(form.getJobId()).fromTableId(form.getFromTableId()).toTableId(form.getToTableId())
				.transTaskId("")
				.owner(form.getUserId())
				.build());
		tblTransTaskRelRepo.save(tblTransTaskRelBean);

		// 若是导入，更新table的状态， 同步开始和结束的位置，以及tablehistory
		JobBean jobBean = jobRepository.findByJobId(form.getJobId()).orElseThrow(() -> new DatabridgeException("job not exist"));
		ExportJobVo.ExportModeVo exportModeVo = ObjectUtils.isArray(jobBean.getExportMode()) ? null : toObject(
				jobBean.getExportMode(), ExportJobVo.ExportModeVo.class);
		if (exportModeVo != null && "increment".equalsIgnoreCase(exportModeVo.getMode()) && !ObjectUtils.isEmpty(form.getIncreateValue())) {
			exportModeVo.setIncreateValue(form.getIncreateValue());
			jobBean.setExportMode(JsonUtils.toJson(exportModeVo));
			log.info(String.format("jobId: %s, increase to: %s", form.getJobId(), form.getIncreateValue()));
			jobRepository.update(jobBean);
		}

		return "";
	}

	public Boolean jobFinished(String jobId) {
		JobBean jobBean = jobRepository.findByJobId(jobId).orElseThrow(() -> new DatabridgeException("job not exist"));
		return jobBean.getStatus() != EXPORT_STATUS_SYNC && jobBean.getStatus() != EXPORT_STATUS_SYNC;
	}
}
