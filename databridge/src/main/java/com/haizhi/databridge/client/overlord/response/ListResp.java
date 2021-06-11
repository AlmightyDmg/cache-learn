package com.haizhi.databridge.client.overlord.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListResp {
	private String username;
	private Integer status;

	@JsonProperty("user_id")
	private String userId;

	private String name;
	@JsonProperty("image_name")
	private String imageName;

	@JsonProperty("dms_permission")
	private Integer dmsPermission;

	private Integer role;

	@JsonProperty("is_frozen")
	private Integer isFrozen;

	@JsonProperty("enterprise_id")
	private String enterpriseId;

	private String email;

	private String ctime;
}
