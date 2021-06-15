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

public class ExportDsVo  {


	@Data
	@ApiModel
	@Builder
	public static final class ExportDsListVo {
//		@NotNull
//		@ApiModelProperty(value = "数据源类型名称，mysql，pg等等")
//		private String name;
		@NotNull
		@ApiModelProperty(value = "数据源类型")
		private Integer type;

		@NotNull
		@ApiModelProperty(value = "数据源列表")
		@JsonProperty("ds_list")
		private List<DsInfoVo> dsList;

	}


	@Data
	@ApiModel
	@Builder
	public static final class DsInfoVo {
		@NotNull
		@ApiModelProperty(value = "数据源名称")
		@JsonProperty("ds_name")
		private String dsName;

		@NotNull
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;

		@NotNull
		@ApiModelProperty(value = "创建者")
		private String owner;

		@NotNull
		@ApiModelProperty(value = "数据源类型")
		private Integer type;

		@NotNull
		@ApiModelProperty(value = "数据源配置")
		private ConfigVo config;

		@NotNull
		@ApiModelProperty(value = "数据源描述")
		@JsonProperty("ds_desc")
		private String dsDesc;

		@ApiModelProperty(value = "导出表数量")
		@JsonProperty("number")
		private Integer number;

		@ApiModelProperty(value = "导出表")
		private List<XtbInfoVo> xtbInfoList;
	}

	@Data
	@ApiModel
	@Builder
	public static final class TableVo {
		@NotNull
		@ApiModelProperty(value = "导出目的表id，export")
		@JsonProperty("xtb_id")
		private String xtbId;

		@NotNull
		@ApiModelProperty(value = "导出目的表名称，export")
		@JsonProperty("xtb_name")
		private String name;

		@NotNull
		@ApiModelProperty(value = "导出目的表名称，export")
		@JsonProperty("is_mapped")
		private Integer isMapped;

		@NotNull
		@ApiModelProperty(value = "导出目的表名称，export")
		@JsonProperty("has_inspection_rules")
		private Integer hasInspectionRules;
	}

	@Data
	@ApiModel
	@Builder
	public static final class DsNumberInfoVo {
		@NotNull
		@ApiModelProperty(value = "数据源类型")
		private Integer type;

		@NotNull
		@ApiModelProperty(value = "数据源配置")
		private Integer number;

		@NotNull
		@ApiModelProperty(value = "数据库名称")
		private String name;
	}

	@Data
	@ApiModel
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class ConfigVo {

		@JsonProperty("name")
		private String name;

		private String url;

		private Integer port;

		private String user;

		private String password;

		private String dbName;

		// es需要额外配置的参数
		private Integer isSecurity;

		private Integer isNetSSL;

		private String version;

	}

	@Data
	@ApiModel
	@Builder
	public static final class ExportTbPmsListVo {
		@ApiModelProperty(value = "数据源id")
		@JsonProperty("ds_id")
		private String dsId;
		@ApiModelProperty(value = "数据源名称")
		@JsonProperty("ds_name")
		private String dsName;
		@ApiModelProperty(value = "数据源描述")
		@JsonProperty("ds_desc")
		private String dsDesc;
		@ApiModelProperty(value = "用户列表")
		@JsonProperty("user")
		private List<UserVo> user;

		@ApiModelProperty(value = "分组列表")
		@JsonProperty("user_group")
		private List<GroupVo> userGroup;

		@ApiModelProperty(value = "角色列表")
		@JsonProperty("role_account")
		private List<RoleAccountVo> roleAccount;

		@ApiModelProperty(value = "临时组列表")
		private List<ChatVo> chat;
	}

	@Data
	@ApiModel
	@Builder
	public static final class UserVo {
		@ApiModelProperty(value = "用户列表")
		private String name;
		@JsonProperty("user_id")
		private String userId;
		@JsonProperty("tb_list")
		private List<String> tbList;
	}

	@Data
	@ApiModel
	@Builder
	public static final class GroupVo {
		@ApiModelProperty(value = "分组列表")
		@JsonProperty("group_name")
		private String groupName;
		@JsonProperty("group_id")
		private String groupId;
		@JsonProperty("tb_list")
		private List<String> tbList;
	}

	@Data
	@ApiModel
	@Builder
	public static final class RoleAccountVo {
		@ApiModelProperty(value = "角色列表")
		@JsonProperty("role_name")
		private String roleName;
		@JsonProperty("role_account_id")
		private String roleAccountId;
		@JsonProperty("tb_list")
		private List<String> tbList;
	}

	@Data
	@ApiModel
	@Builder
	public static final class ChatVo {
		@ApiModelProperty(value = "临时组列表")
		@JsonProperty("chat_name")
		private String chatName;
		@JsonProperty("chat_id")
		private String chatId;
		@JsonProperty("tb_list")
		private List<String> tbList;
	}

	@Data
	@ApiModel
	@Builder
	public static final class XtbVo {
		@ApiModelProperty(value = "导出表信息")
		@JsonProperty("xtb_id")
		private String xtbId;
		@JsonProperty("xtb_name")
		private String xtbName;
	}

	@Data
	@ApiModel
	@Builder
	public static final class ExportTbPmsRoleListVo {
		@ApiModelProperty(value = "用户列表")
		@JsonProperty("user")
		private List<String> user;

		@ApiModelProperty(value = "分组列表")
		@JsonProperty("user_group")
		private List<String> userGroup;

		@ApiModelProperty(value = "角色列表")
		@JsonProperty("role_account")
		private List<String> roleAccount;

		@ApiModelProperty(value = "临时组列表")
		@JsonProperty("role_chat")
		private List<String> chat;
	}

	@Data
	@ApiModel
	@Builder
	public static final class FieldNameRelRuleVo {
		@ApiModelProperty(value = "数据源信息")
		@JsonProperty("ds_info")
		private DsInfoVo dsInfo;

		@ApiModelProperty(value = "导出表信息")
		@JsonProperty("xtb_info")
		private XtbInfoVo xtbInfo;

		@ApiModelProperty(value = "导出表和规则包关联关系")
		@JsonProperty("xtb_rule_list")
		private List<FieldNameRelRuleInfoVo> xtbRuleList;

	}

	@Data
	@ApiModel
	@Builder
	public static final class FieldNameRelRuleInfoVo {
		@ApiModelProperty(value = "关联关系id")
		@JsonProperty("rela_id")
		private String relaId;
		@ApiModelProperty(value = "字段名称")
		@JsonProperty("field_name")
		private String fieldName;
		@ApiModelProperty(value = "规则id")
		@JsonProperty("rule_id")
		private String ruleId;
	}

	@Data
	@ApiModel
	@Builder
	public static final class XtbInfoVo {
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("xtb_id")
		private String xtbId;
		@ApiModelProperty(value = "导出表名称")
		@JsonProperty("xtb_name")
		private String xtbName;
	}

	@Data
	@ApiModel
	@Builder
	public static final class DsCreateVo {
		@ApiModelProperty(value = "导出表id")
		@JsonProperty("ds_id")
		private String dsId;
	}
}
