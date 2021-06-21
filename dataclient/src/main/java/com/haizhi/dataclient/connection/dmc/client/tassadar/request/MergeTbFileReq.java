package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import javax.validation.constraints.NotBlank;

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
	@NotBlank
	@Field("fields")
	private String fields;
	@NotBlank
	@Field("separator")
	private String separator;
	@NotBlank
	@Field("null_holder")
	private String nullHolder;
	@NotBlank
	@Field("retention")
	private String retention;

}
