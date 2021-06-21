package com.haizhi.dataio.client.flinkx;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.haizhi.dataio.client.flinkx.response.FlinkxJobStatus;
import com.haizhi.dataio.client.flinkx.response.FlinkxResult;
import com.haizhi.dataio.job.action.FlinkAction;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:06:16
 */
@RetrofitClient(baseUrl = "${flinkx.url}")
public interface FlinkxClient {

    @POST("/mock/11/flinkx/start")
    FlinkxResult<String> startJob(@Body FlinkAction.FlinkActionParam flinkActionParam);

    @POST("/mock/11/flinkx/status")
    @FormUrlEncoded
    FlinkxResult<FlinkxJobStatus> getStatus(@Query("jobId") String jobId);

    @POST("/mock/11/flinkx/stop")
    @FormUrlEncoded
    FlinkxResult<String> stopJob(@Query("jobId") String jobId);
}
