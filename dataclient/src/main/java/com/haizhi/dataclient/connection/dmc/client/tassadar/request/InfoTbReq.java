package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import java.util.Collections;
import java.util.List;

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
public class InfoTbReq extends TassadarReqBase {
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@Default
	@Field("user_id")
	private String userId = "";
	@Default
	@Field("groups")
	private List<Object> groups = Collections.emptyList();
	@Default
	@Field("query_for_ct")
	private String queryForCt = "";
	@Default
	@Field("query_for_version")
	private String queryForVersion = "";
	@Default
	@Field("role")
	private String role = null;
	@Default
	@Field("user_belong")
	private String userBelong = null;
	@Default
	@Field("diff_del_pmsn")
	private Integer diffDelPmsn = 0;

}
