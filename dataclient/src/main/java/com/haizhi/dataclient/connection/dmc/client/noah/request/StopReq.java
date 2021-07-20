package com.haizhi.dataclient.connection.dmc.client.noah.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;
import retrofit2.http.Query;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StopReq {
    @Query("user_id")
    String userId;

    @Field("task_id")
    String jobId;
}
