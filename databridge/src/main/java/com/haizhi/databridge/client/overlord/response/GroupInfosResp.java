package com.haizhi.databridge.client.overlord.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GroupInfosResp {
	@JsonProperty("enterprise_id")
	private String enterpriseId;

	@JsonProperty("group_id")
	private String groupId;

	@JsonProperty("belong_id")
	private String belongId;

	@JsonProperty("group_name")
	private String groupName;

}
