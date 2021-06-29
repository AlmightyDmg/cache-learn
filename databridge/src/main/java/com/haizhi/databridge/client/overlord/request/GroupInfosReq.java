package com.haizhi.databridge.client.overlord.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@ToString(callSuper = true)
public class GroupInfosReq {
	@NotBlank
	@Field("user_id")
	@JsonProperty("user_id")
	private String userId;
	@Field("group_id_list")
	@JsonProperty("group_id_list")
	private String groupIdList;

}
