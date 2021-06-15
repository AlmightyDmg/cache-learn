package com.haizhi.databridge.bean.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhaohuanhuan
 */

public class ExportJobVo {

	@Data
	@ApiModel
	@Builder
	public static final class JobInfoVo {
		@ApiModelProperty(value = "任务id")
		@JsonProperty("job_id")
		private String jobId;

		@ApiModelProperty(value = "导出表信息")
		@JsonProperty("xtb_info")
		private ExportDsVo.XtbVo xTbVo;

		@ApiModelProperty(value = "自主建模tb表信息")
		@JsonProperty("tb_info")
		private TbVo tbVo;

		@NotNull
		@ApiModelProperty(value = "模型信息")
		@JsonProperty("flow_info")
		private FlowInfoVo flowInfo;

		@NotNull
		@ApiModelProperty(value = "数据源信息")
		@JsonProperty("ds_info")
		private DsInfoVo dsInfo;

		@NotNull
		@ApiModelProperty(value = "数据源信息")
		@JsonProperty("fields_mapping")
		private FieldsMappingInfoVo fieldsMappingInfoVo;

		@ApiModelProperty(value = "导出方式mode: overwrite(全量)/increment(增量)")
		@JsonProperty("export_mode")
		private ExportModeVo exportMode;

		@ApiModelProperty(value = "更新方式")
		@JsonProperty("scheduler_conf")
		private SchedulerConfVo schedulerConf;

		// CREATE = 0; SYNCING = 1; NORMAL = 2; ERROR = 3; QUEUE = 4; STOP = 5
		@ApiModelProperty(value = "导出状态")
		private Integer status;

		@ApiModelProperty(value = "日志信息")
		@JsonProperty("error_msg")
		private String errorMsg;

		@ApiModelProperty(value = "导出数据统计")
		private ExportDataCountVo count;

		@ApiModelProperty(value = "导出时间")
		@JsonProperty("export_time")
		private String exportTime;

		@ApiModelProperty(value = "导出状态")
		@JsonProperty("export_failure_strategy")
		private Integer exportFailureStrategy;

		// TODO fieldsMapping, config
	}

	@Data
	@ApiModel
	@Builder
	public static final class FlowInfoVo {
		@ApiModelProperty(value = "模型id")
		@JsonProperty("flow_id")
		private String flowId;
		@ApiModelProperty(value = "模型名称")
		@JsonProperty("flow_name")
		private String flowName;
	}

	@Data
	@ApiModel
	@Builder
	public static final class DsInfoVo {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@ApiModelProperty(value = "数据源名称")
		@JsonProperty("ds_name")
		private String dsName;
		@ApiModelProperty(value = "数据源类型")
		@JsonProperty("type")
		private Integer type;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class ExportModeVo {
		@ApiModelProperty(value = "导出方式，overwrite全量，increment增量")
		private String mode;
		@ApiModelProperty(value = "增量时要传，目前就是append，即追加")
		@JsonProperty("increate_mode")
		private String increateMode;
		@ApiModelProperty(value = "是否清空数据，mode为increment时需要传，0：不清空，1：清空")
		@JsonProperty("is_truncate")
		private Integer isTruncate = 0;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class SchedulerConfVo {
		@ApiModelProperty(value = "0 自定义时间，1 相对时间，2 自动更新，3 暂停更新")
		private Integer mode;
		@ApiModelProperty(value = "crontab命令，eg: 1 * * 11 *")
		@JsonProperty("sync_config")
		private String syncConfig;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class ExportDataCountVo {
		private Integer appendCount;
		private Integer updateCount;
		private Integer deleteCount;
		private Integer failedCount;
		private Integer allCount;
		private Integer filterCount;
	}

	@Data
	@ApiModel
	@Builder
	public static final class InnerJobInfoVo {
		private String jobId;
		private String userId;
		private String entId;
		private String config;
//		private Integer type;
		@ApiModelProperty(value = "0 自定义时间，1 相对时间，2 自动更新，3 暂停更新,与crontab配合使用")
		@JsonProperty("executeMode")
		private Integer executeMode = 2;
		@ApiModelProperty(value = "crontab命令，eg: 1 * * 11 *")
		private String crontab;
		@ApiModelProperty(value = "TB表中的tb_id")
		private String relaId;
		private Integer status;
		@JsonProperty("errorMsg")
		private String errorMsg;
		private String count;
		private String ctime;
		private String utime;
		private String etime;
	}

	@Data
	@ApiModel
	@Builder
	public static final class JobConfigInnerVo {

		@ApiModelProperty(value = "映射字段")
		@JsonProperty("fieldsMapping")
		private List<FieldMappingInnerVo> fieldsMapping;
		@JsonProperty("selectFields")
		private List<String> selectFields;
		@JsonProperty("idFields")
		private List<String> idFields;
		private SinkInnerVo sink;
		@JsonProperty("isTruncate")
		private boolean isTruncate;
		@ApiModelProperty(value = "1：导出校验通过的数据，2：停止导出数据")
		@JsonProperty("exportFailureStrategy")
		private Integer exportFailureStrategy;
		@ApiModelProperty(value = "规则过滤")
		@JsonProperty("ruleFilters")
		private List<RuleFilterVo> ruleFilters;

	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class RuleFilterVo {

		@JsonProperty("mappingName")
		private String mappingName;
		@ApiModelProperty(value = "规则")
		private String cond;

	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class FieldMappingInnerVo {

		@ApiModelProperty(value = "原始字段")
		@JsonProperty("originalName")
		private String originalName;
		@ApiModelProperty(value = "映射字段")
		@JsonProperty("mappingName")
		private String mappingName;
		@ApiModelProperty(value = "映射字段类型")
		@JsonProperty("mappingValue")
		private String mappingValue = "";

	}

	@Data
	@ApiModel
	@Builder
	public static final class SinkInnerVo {

		@ApiModelProperty(value = "数据库类型名称, eg: mysql/postgresql/datahub")
		@JsonProperty("dbType")
		private String dbType;
		@ApiModelProperty(value = "数据库地址")
		private String url;
		@ApiModelProperty(value = "用户名")
		private String user;
		@ApiModelProperty(value = "密码")
		private String password;
		@ApiModelProperty(value = "数据库名称")
		@JsonProperty("dbName")
		private String dbName;
		@ApiModelProperty(value = "数据表名称")
		@JsonProperty("tbName")
		private String tbName;
		// es时需要
		@JsonProperty("isSecurity")
		private boolean isSecurity;
		@JsonProperty("isNetSSL")
		private boolean isNetSSL;

		@ApiModelProperty(value = "增量还是全量，overwrite(全量)/append(增量)")
		private String mode;
		@ApiModelProperty(value = "增量策略，目前固定填append")
		@JsonProperty("increateMode")
		private String increateMode;
		@JsonProperty("version")
		private Integer version;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class FieldsMappingInfoInnerVo {

		@ApiModelProperty(value = "映射字段")
		@JsonProperty("fieldsMapping")
		private List<FieldMappingInnerVo> fieldsMapping;
		@JsonProperty("selectFields")
		private List<String> selectFields;
		@JsonProperty("idFields")
		private List<String> idFields;
	}

	@Data
	@ApiModel
	@Builder
	public static final class JobConfigVo {

		@ApiModelProperty(value = "映射字段")
		private List<FieldMappingVo> fieldsMapping;
		private List<String> selectFields;
		private List<String> idFields;
		private SinkVo sink;
		private boolean isTruncate;
		@ApiModelProperty(value = "1：导出校验通过的数据，2：停止导出数据")
		private Integer exportFailureStrategy;

	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class FieldsMappingInfoVo {

		@ApiModelProperty(value = "映射字段")
		private List<FieldMappingVo> fieldsMapping;
		private List<String> selectFields;
		private List<String> idFields;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class FieldMappingVo {

		@ApiModelProperty(value = "原始字段")
		private String originalName;
		@ApiModelProperty(value = "映射字段")
		private String mappingName;
		@ApiModelProperty(value = "映射字段类型")
		private String mappingValue = "";

	}

	@Data
	@ApiModel
	@Builder
	public static final class SinkVo {

		@ApiModelProperty(value = "数据库类型名称, eg: mysql/postgresql/datahub")
		private String dbType;
		@ApiModelProperty(value = "数据库地址")
		private String url;
		@ApiModelProperty(value = "用户名")
		private String user;
		@ApiModelProperty(value = "密码")
		private String password;
		@ApiModelProperty(value = "数据库名称")
		private String dbName;
		@ApiModelProperty(value = "数据表名称")
		private String tbName;
		// es时需要
		private boolean isSecurity;
		private boolean isNetSSL;

		@ApiModelProperty(value = "增量还是全量，overwrite(全量)/append(增量)")
		private String mode;
		@ApiModelProperty(value = "增量策略，目前固定填append")
		private String increateMode;
	}

	@Data
	@ApiModel
	@Builder
	public static final class TbVo {
		@ApiModelProperty(value = "自主建模tb表信息")
		@JsonProperty("tb_id")
		private String tbId;
		@JsonProperty("tb_name")
		private String tbName;
		private String type;
	}

	@Data
	@ApiModel
	@Builder
	public static final class HistoryVo {
		@JsonProperty("jobId")
		private String jobId;
		@JsonProperty("start_time")
		private String startTime;
		@JsonProperty("end_time")
		private String endTime;
		@JsonProperty("cost_time")
		private Integer costTime;
		private Integer status;
		private HistoryExportDataCountVo count;
		@JsonProperty("error_msg")
		private String errorMsg;
	}

	@Data
	@ApiModel
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static final class HistoryExportDataCountVo {
		private Integer appendCount;
		private Integer updateCount;
		private Integer deleteCount;
		private Integer failedCount;
		private Integer allCount;
		private Integer filterCount;
	}
}
