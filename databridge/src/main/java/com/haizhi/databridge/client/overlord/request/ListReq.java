// CHECKSTYLE:OFF
package com.haizhi.databridge.client.overlord.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder.Default;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@ToString(callSuper = true)
public class ListReq {
	@NotBlank
	@Field("user_id")
	@JsonProperty("user_id")
	private String userId;
	@Default
	@Field("offset")
	@JsonProperty("offset")
	private Integer offset = 0;
	@Default
	@Field("limit")
	@JsonProperty("limit")
	private Integer limit = 1000;
	@Default
	@Field("anonymous")
	@JsonProperty("anonymous")
	private Integer anonymous = 0;
	@Default
	@Field("filters")
	@JsonProperty("filters")
	private String filters = null;

}
