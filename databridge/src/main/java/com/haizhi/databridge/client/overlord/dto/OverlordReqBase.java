package com.haizhi.databridge.client.overlord.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Query;

import com.haizhi.databridge.client.common.ReqBase;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class OverlordReqBase extends ReqBase {

	@Query("ua")
	private String ua;

	@Query("imei")
	private String imei;

	@Query("uuid")
	private String uuid;

	@Query("source")
	private int source;

	@Query("dmc_request")
	private Integer dmcRequest;
}
