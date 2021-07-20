package com.haizhi.dataclient.connection.dmc.client.noah;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.client.noah.request.StartReq;
import com.haizhi.dataclient.connection.dmc.client.noah.request.StopReq;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataFieldResp;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataResp;
import com.haizhi.dataclient.connection.dmc.client.noah.response.StartResp;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:49:01
 */
public interface NoahClient {
    @POST("/api/noah/task/noahstart")
    @FormUrlEncoded
    StartResp startImportJob(@QueryBean StartReq startReq);

    @POST("/api/noah/task/noahstop")
    @FormUrlEncoded
    StartResp stopImportJob(@QueryBean StopReq stopReq);

    @POST("/api/noah/connector/table")
    @FormUrlEncoded
    GetTableDataFieldResp getTableDataField(@Field("connect_id") String connectId,
                                            @Field("ref") String ref,
                                            @Field("table_id") String tableId,
                                            @Query("user_id") String userId);

    @POST("/api/noah/connector/query")
    @FormUrlEncoded
    GetTableDataResp getTableDataQuery(@Field("connect_id") String connectId,
                                       @Field("sql") String sql,
                                       @Query("user_id") String userId);
}
