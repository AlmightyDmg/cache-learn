package com.haizhi.dataclient.connection.dmc.client.pandora.req;

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
public class FieldDependencyCheckReq extends PandoraReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@NotBlank
	@Field("data")
	private String data;

}
