package com.haizhi.databridge.client.overlord.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder.Default;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;


@Data
@SuperBuilder
@ToString(callSuper = true)
public class InfoReq {
	@NotBlank
	@Field("user_id")
	@JsonProperty("user_id")
	private String userId;
	@Default
	@Field("sub_id")
	@JsonProperty("sub_id")
	private String subId = "";
	@Default
	@Field("details")
	@JsonProperty("details")
	private Integer details = 0;

}
