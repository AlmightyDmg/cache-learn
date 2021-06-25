package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

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
public class MergeTbFileReq extends TassadarReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;

	@NotBlank
	@Field("tb_id")
	private String tbId;

	@Field("force_merge")
	@JsonProperty("force_merge")
	private Integer forceMerge;
}
