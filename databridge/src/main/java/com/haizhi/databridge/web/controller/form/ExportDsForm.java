package com.haizhi.databridge.web.controller.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

public class ExportDsForm {

	@Data
	public static final class ExportDsCreateForm {
		@ApiModelProperty(value = "导出数据源创建")
		@JsonProperty("ds_name")
		@Valid
		@NotNull(message = "ds_name不能为空")
		private String dsName;
		@JsonProperty("ds_desc")
		private String dsDesc;
		@NonNull
		private Integer type;
		@NonNull
		private ConfigForm config;
		@JsonProperty("tb_name_list")
		private List<String> tbNameList;
	}

	@Data
	public static final class ExportDsListForm {
		@ApiModelProperty(value = "导出数据源展示")
		@JsonProperty("only_owner")
		private Integer onlyOwner = 1;
		private Integer type;
	}

	@Data
	public static final class ExportDsModifyForm {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@JsonProperty("ds_name")
		private String dsName;
		@JsonProperty("ds_desc")
		private String dsDesc;
		private ConfigForm config;
	}

	@Data
	public static final class ExportDsTableListForm {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@ApiModelProperty(value = "数据源类型，当获取某一类型下的所有数据源的时候用")
		@JsonProperty("type")
		private Integer type;
		@JsonProperty("get_mapping_status")
		private List<String> getMappingStatus;
		@JsonProperty("get_inspection_rules")
		private List<String> getInspectionRules;
	}

	@Data
	public static final class ExportDsTableUpdateForm {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		private Integer type;
		@JsonProperty("add_table_list")
		private List<String> addTableList;
		@JsonProperty("del_table_list")
		private List<String> delTableList;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class ConfigForm {
		private Integer type;
		// datahub的entPoint绑定在url上
		@NonNull
		private String url;
		private Integer port;
		// datahub的accessId绑定在user上
		@NonNull
		private String user;
		// datahub的accessKey绑定在password上
		@NonNull
		private String password;
		// datahub的project绑定在dbName上
		@NonNull
		@JsonProperty("db_name")
		private String dbName;
		@JsonProperty("tb_name")
		private String tbName;
		// es需要额外配置的参数
		@JsonProperty("is_security")
		private Integer isSecurity;
		@JsonProperty("is_net_ssl")
		private Integer isNetSSL;
		// datahub版本，0：2.0以前版本，1：2.0以后的版本
		private String version = "1";
	}

	@Data
	public static final class ExportDsPmsCreateForm {
		@JsonProperty("xtb_id")
		private String xtbId;
		@JsonProperty("user_list")
		private PmsChangeInfoForm userList;
		@JsonProperty("role_list")
		private PmsChangeInfoForm roleList;
		@JsonProperty("chat_list")
		private PmsChangeInfoForm chatList;
		@JsonProperty("group_list")
		private PmsChangeInfoForm groupList;
	}

	@Data
	public static final class PmsChangeInfoForm {
		@ApiModelProperty(value = "所有授权用户")
		private List<String> all;
		@ApiModelProperty(value = "所有取消授权用户")
		private List<String> del;
	}

	@Data
	public static final class TbRuleModifyForm {
		@JsonProperty("xtb_id")
		private String xtbId;
		@JsonProperty("add_rule")
		private List<TbRuleAddForm> addRuleList;
		@JsonProperty("modify_rule")
		private List<TbRuleUpdateForm> modifyRuleList;
		@JsonProperty("del_rule")
		private List<String> delRuleList;
	}

	@Data
	public static final class TbRuleAddForm {
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("xtb_id")
		private String xtbId;
		@ApiModelProperty(value = "导出表字段名称")
		@JsonProperty("field_name")
		private String fieldName;
		@ApiModelProperty(value = "规则id")
		@JsonProperty("rule_id")
		private String ruleId;
	}

	@Data
	public static final class TbRuleListForm {
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("xtb_id")
		private String xtbId;
	}

	@Data
	public static final class TbRuleUpdateForm {
		@ApiModelProperty(value = "导出表和规则关联关系id")
		@JsonProperty("rela_id")
		private String relaId;
		@ApiModelProperty(value = "导出表字段名称")
		@JsonProperty("field_name")
		private String fieldName;
		@ApiModelProperty(value = "规则id")
		@JsonProperty("rule_id")
		private String ruleId;
	}

	@Data
	public static final class ExportDsJobCreateForm {
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("tb_id")
		private String tbId;
		@ApiModelProperty(value = "导入表id")
		@JsonProperty("xtb_id")
		private String xtbId;
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
//		@JsonProperty("fields_mapping")
		private List<FieldMappingForm> fieldsMapping;
		@ApiModelProperty(value = "主键字段")
//		@JsonProperty("id_field_list")
		private List<String> idFields;
		@ApiModelProperty(value = "勾选中的字段")
//		@JsonProperty("select_field_list")
		private List<String> selectFields;
	}

	@Data
	public static final class FieldMappingForm {
		@ApiModelProperty(value = "导出表字段名称")
//		@JsonProperty("original_name")
		private String originalName;
		@ApiModelProperty(value = "导入表字段名称")
//		@JsonProperty("field_name")
		private String mappingName;
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
		@ApiModelProperty(value = "是否清空数据")
		@JsonProperty("is_truncate")
		private String isTruncate;
	}

	@Data
	public static final class ExportDsPmsRoleModifyForm {
		@JsonProperty("ds_id")
		private String dsId;
		@JsonProperty("role_id")
		private String roleId;
		@JsonProperty("role_type")
		private Integer roleType;
		@JsonProperty("add_table_list")
		private List<String> addTableList;
		@JsonProperty("del_table_list")
		private List<String> delTableList;
	}

}
