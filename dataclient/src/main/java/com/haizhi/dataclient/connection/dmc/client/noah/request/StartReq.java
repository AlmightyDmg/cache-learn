package com.haizhi.dataclient.connection.dmc.client.noah.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;
import retrofit2.http.JsonBeanParam;
import retrofit2.http.Query;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartReq {
    @Query("user_id")
    String userId;

    @Field("scheduler_id")
    String jobId;

    @JsonBeanParam
    @Field("tables")
    List<String> tables;

    @Field("full")
    Integer full;
}
