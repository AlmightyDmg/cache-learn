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
public class DbQueryReq extends MobiusReqBase {
	@NotBlank
	@Field("sql")
	private String sql;	// 查询sql

	@Field("storage_id")
	private String storageId;	// 查询的表的storage_id，默认为空

	@Field("pool")
	private String pool;	// 任务执行的资源池，默认资源池为default

	@Field("group_id")
	private String groupId;	// 任务组id，用于取消相同任务，节省资源

	@Field("group")
	private String group;	// 查询任务是否优先调度标志，默认非优先调度。'0'表示优先调度，'1'表示非优先调度

	@Field("limit_start")
	private Integer limitStart;	// 起始值

	@Field("limit_end")
	private Integer limitEnd;	//  结束值
}
