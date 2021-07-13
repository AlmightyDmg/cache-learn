package com.haizhi.dataio.client.flinkx;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.haizhi.dataio.client.flinkx.response.FlinkJobStartStopResp;
import com.haizhi.dataio.client.flinkx.response.FlinkxJobStatus;
import com.haizhi.dataio.client.flinkx.response.FlinkxResult;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:06:16
 */
@RetrofitClient(baseUrl = "${flinkx.url}")
public interface FlinkxClient {

    @POST("/flinkx/start")
    @FormUrlEncoded
    FlinkxResult<FlinkJobStartStopResp> startJob(@Field("jobName") String jobName, @Field("reader") String reader, @Field("writer") String writer);

    @POST("/flinkx/status")
    @FormUrlEncoded
    FlinkxResult<FlinkxJobStatus> getStatus(@Field("jobId") String jobId);

    @POST("/flinkx/stop")
    @FormUrlEncoded
    FlinkxResult<FlinkJobStartStopResp> stopJob(@Field("jobId") String jobId);
}
