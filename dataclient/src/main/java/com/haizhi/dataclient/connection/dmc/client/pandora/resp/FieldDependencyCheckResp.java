package com.haizhi.dataclient.connection.dmc.client.pandora.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldDependencyCheckResp {
	private String fid;
	private String title;
	@JsonProperty("vFields")
	private List<String> vFields;
	private List<ChartInfo> chart;
	private List<UserInfo> users;
	private List<TbInfo> tbs;
	private List<GroupInfo> groups;
	private List<MlInfo> ml;
	private List<RuleInfo> rule;
	@JsonProperty("dsh_filter")
	private List<DshInfo> dshFilter;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class ChartInfo {
		@JsonProperty("ct_id")
		private String ctId;
		@JsonProperty("ct_name")
		private String ctName;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class UserInfo {
		@JsonProperty("user_id")
		private String userId;
		private String name;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class TbInfo {
		@JsonProperty("tb_id")
		private String tbId;
		@JsonProperty("tb_name")
		private String tbName;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class GroupInfo {
		@JsonProperty("group_id")
		private String groupId;
		@JsonProperty("group_name")
		private String groupName;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class MlInfo {
		@JsonProperty("ml_id")
		private String mlId;
		private String name;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class RuleInfo {
		@JsonProperty("rule_id")
		private String ruleId;
		@JsonProperty("rule_name")
		private String ruleName;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class DshInfo {
		@JsonProperty("dsh_id")
		private String dshId;
		@JsonProperty("dsh_name")
		private String dshName;
	}

}
