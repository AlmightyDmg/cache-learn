package com.haizhi.databridge.web.controller.form;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


public class ExportJobForm {

	@Data
	public static final class ConfigForm {
		// mysql/postgreSQL/es需要的参数
		private String name;
		// datahub的entPoint绑定在url上
		private String url;
		// datahub的accessId绑定在user上
		private String user;
		// datahub的accessKey绑定在password上
		private String password;
		// datahub的project绑定在dbName上
		@JsonProperty("db_name")
		private String dbName;
		// es需要额外配置的参数
		@JsonProperty("is_security")
		private Integer isSecurity;
		@JsonProperty("is_net_ssl")
		private Integer isNetSSL;
	}

	@Data
	public static final class ExportJobCreateForm {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("tb_id")
		private String tbId;
		@ApiModelProperty(value = "导入表id")
		@JsonProperty("xtb_id")
		private String xtbId;
		@ApiModelProperty(value = "目的表名称，xtb_id为空的时候要根据xtb_name去创建")
		@JsonProperty("xtb_name")
		private String xtbName;
		@ApiModelProperty(value = "导出策略")
		@JsonProperty("export_mode")
		private ExportModeForm exportMode;
		@ApiModelProperty(value = "定时策略")
		@JsonProperty("scheduler_conf")
		private SchedulerConfForm schedulerConf;
		@ApiModelProperty(value = "字段映射配置")
		@JsonProperty("field_mapping_config")
		private FieldMappingConfigForm fieldMappingConfig;
		@ApiModelProperty(value = "1：导出校验通过的数据，2：停止导出数据")
		@JsonProperty("export_failure_strategy")
		private Integer exportFailureStrategy;
	}

	@Data
	public static final class ExportJobModifyForm {
		@ApiModelProperty(value = "导出任务id")
		@JsonProperty("job_id")
		private String jobId;
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("tb_id")
		private String tbId;
		@ApiModelProperty(value = "导入表id")
		@JsonProperty("xtb_id")
		private String xtbId;
		@ApiModelProperty(value = "目的表名称")
		@JsonProperty("xtb_name")
		private String xtbName;
		@ApiModelProperty(value = "导出策略")
		@JsonProperty("export_mode")
		private ExportModeForm exportMode;
		@ApiModelProperty(value = "定时策略")
		@JsonProperty("scheduler_conf")
		private SchedulerConfForm schedulerConf;
		@ApiModelProperty(value = "字段映射配置")
		@JsonProperty("field_mapping_config")
		private FieldMappingConfigForm fieldMappingConfig;
		@ApiModelProperty(value = "1：导出校验通过的数据，2：停止导出数据")
		@JsonProperty("export_failure_strategy")
		private Integer exportFailureStrategy;
	}

	@Data
	public static final class FieldMappingConfigForm {
		@ApiModelProperty(value = "字段映射配置")
		@JsonProperty("fieldsMapping")
		private List<FieldMappingForm> fieldsMapping;
		@ApiModelProperty(value = "主键字段")
		@JsonProperty("idFields")
		private List<String> idFields;
		@ApiModelProperty(value = "勾选中的字段")
		@JsonProperty("selectFields")
		private List<String> selectFields;
	}

	@Data
	public static final class FieldMappingForm {
		@ApiModelProperty(value = "导出表字段名称")
		@JsonProperty("originalName")
		private String originalName;
		@ApiModelProperty(value = "导入表字段名称")
		@JsonProperty("mappingName")
		private String mappingName;
		@JsonProperty("mappingValue")
		private String mappingValue = "string";
	}

	@Data
	public static final class SchedulerConfForm {
		@ApiModelProperty(value = "0 自定义时间,1 相对时间,2 自动更新,3 暂停更新")
		private Integer mode;
		@ApiModelProperty(value = "crontab命令，eg: 1 * * 11 *")
		@JsonProperty("sync_config")
		private String syncConfig;
	}

	@Data
	public static final class ExportModeForm {
		@ApiModelProperty(value = "overwrite全量更新，increment增量更新")
		private String mode;
		@JsonProperty("increate_mode")
		private String increateMode;
		@JsonProperty("increate_field")
		private String increateField;
		@JsonProperty("increate_value")
		private String increateValue;
		@ApiModelProperty(value = "是否清空数据")
		@JsonProperty("is_truncate")
		private String isTruncate;
	}
}
