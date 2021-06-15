package com.haizhi.databridge.web.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public class RuleForm {

	@Data
	public static final class AddForm {
		@ApiModelProperty(value = "规则名称")
		@JsonProperty("rule_name")
		private String ruleName;

		@ApiModelProperty(value = "规则内容")
		private String cond;

//		@ApiModelProperty(value = "用户id")
//		private String userId;
//
//		@ApiModelProperty(value = "企业域")
//		private String entId;
	}

	@Data
	public static final class DelForm {

		@ApiModelProperty(value = "规则id")
		private String ruleId;

	}

	@Data
	public static final class CheckRelyForm {

		@ApiModelProperty(value = "规则id")
		private String ruleId;

	}

	@Data
	public static final class ModifyForm {

		@ApiModelProperty(value = "规则名称")
		private String ruleName;

		@ApiModelProperty(value = "规则内容")
		private String cond;

		@ApiModelProperty(value = "规则id")
		private String ruleId;
	}

	@Data
	public static final class CheckForm {

//		@ApiModelProperty(value = "字段名称")
//		private String fieldName;
//
//		@ApiModelProperty(value = "字段类型")
//		private String fieldType;

		@ApiModelProperty(value = "配置的规则")
		private String cond;
	}
}
