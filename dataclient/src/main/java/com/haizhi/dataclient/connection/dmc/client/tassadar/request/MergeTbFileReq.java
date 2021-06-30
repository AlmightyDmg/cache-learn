package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;
import retrofit2.http.JsonBeanParam;

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

	@Field(value = "separator", encoded = true)
	private String separator;

	@Field(value = "null_holder", encoded = true)
	private String nullHolder;

	@JsonBeanParam
	@Field("fields")
	private List<String> fields;
}
