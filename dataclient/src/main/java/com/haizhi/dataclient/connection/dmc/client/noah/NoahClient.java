package com.haizhi.dataclient.connection.dmc.client.noah;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.JsonBeanParam;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataFieldResp;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataResp;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:49:01
 */
public interface NoahClient {
    @POST("/api/noah/task/noahstart")
    @FormUrlEncoded
    void startImportJob(@Query("user_id") String userId,
                        @Field("scheduler_id") String jobId,
                        @Field("tables") @JsonBeanParam List<String> tables,
                        @Field("full") Integer full);

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
