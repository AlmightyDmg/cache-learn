package com.haizhi.databridge.client.behemoth.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoleListReq extends BehemothReqBase {
	@NotBlank
	@Field("role_list")
	@JsonProperty("role_list")
	private String roleList;
}
