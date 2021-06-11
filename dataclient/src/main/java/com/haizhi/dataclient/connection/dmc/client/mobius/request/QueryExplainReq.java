package com.haizhi.dataclient.connection.dmc.client.mobius.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

/**
 * Desc :
 * author : xierong
 * createTime : 2020/9/14
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class QueryExplainReq extends MobiusReqBase {
	@NotBlank
	@Field("sql")
	private String sql;	// 待解析sql
}
