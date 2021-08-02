package com.haizhi.dataclient.connection.dmc.client.pandora.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartGetListByTbResp {
	private List<CtInfo> chart;

	@Builder
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CtInfo {
		private Integer category;

		@JsonProperty("proj_id")
		private String projId;

		@JsonProperty("user_id")
		private String userId;

		@JsonProperty("dsh_name")
		private String dshNname;

		@JsonProperty("ct_name")
		private String ctName;

		@JsonProperty("ct_id")
		private String ctId;

		private List<String> parents;

		@JsonProperty("ct_type")
		private Integer ctType;

		@JsonProperty("dsh_id")
		private String dshId;

		@JsonProperty("proj_name")
		private String projName;
	}
}
