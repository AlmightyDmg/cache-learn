// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.mobius;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.JsonBeanParam;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.client.mobius.request.DelOldDataReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetReaderReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetWriterReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.TableCreateReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.CreateTableResp;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcReader;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcWriter;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.ExplainResp;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.MergePatchResp;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.MobiusResult;


/**
 * todo 新增接口，参数从request 中定义输入对象
 */
public interface MobiusClient {

	/**
	 * sql语法检查
	 */
	@POST("/query/explain")
	@FormUrlEncoded
	ExplainResp explain(@Valid @QueryBean QueryExplainReq queryExplainReq);

	@POST("/tb/create")
	@FormUrlEncoded
	CreateTableResp createTable(@Valid @QueryBean TableCreateReq createReq);

	/**
	 * 提交写入数据操作
	 *
	 * @param 表名
	 * @param commit之前的表数据
	 * @param 去重字段，多个去重字段以","分隔
	 * @param 合并数据超时时间，默认使用配置文件配置的超时时间
	 * @param commit参数，包括字段相关信息
	 * @param 全量更新标识，默认非全量，0表示非全量，1表示全量
	 */
	@POST("/table/commit")
	@FormUrlEncoded
	MobiusResult<MergePatchResp> tableCommit(@NotBlank @Field("tbName") String tbName,
											@Field("dataCount") Long dataCount, @Field("keys") String keys, @Field("timeout") Long timeout,
											@NotBlank @Field("params") String params, @Field("forceMerge") Integer forceMerge);

	@POST("/tb/getDmcWriter")
	@FormUrlEncoded
	MobiusResult<String> getDmcWriter(@QueryBean GetWriterReq getWriterReq);

	@POST("/tb/getDmcReadwer")
	@FormUrlEncoded
	MobiusResult<String> getDmcReader(@QueryBean GetReaderReq getReaderReq);


	@POST("/view/deleteOldData")
	@FormUrlEncoded
	MobiusResult<String> deleteOldData(@QueryBean DelOldDataReq delOldDataReq);
}
