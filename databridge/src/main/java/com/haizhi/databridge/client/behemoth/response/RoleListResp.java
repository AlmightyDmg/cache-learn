package com.haizhi.databridge.client.behemoth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoleListResp {

	@JsonProperty("role_id")
	private String roleId;

	@JsonProperty("role_name")
	private String roleName;

	@JsonProperty("ent_id")
	private String entId;

}
