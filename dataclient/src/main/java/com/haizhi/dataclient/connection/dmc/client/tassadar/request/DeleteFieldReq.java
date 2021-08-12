package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeleteFieldReq extends TassadarReqBase {
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@NotBlank
	@Field("field_id")
	private String fieldId;
	@Default
	@Field("user_id")
	private String userId = "";
	@Default
	@Field("query_for_ct")
	private String queryForCt = "";
	@Default
	@Field("assist_field")
	private Integer assistField = 0;
	@Default
	@Field("user_belong")
	private String userBelong = null;
	@Default
	@Field("operator_id")
	private String operatorId = null;
}
