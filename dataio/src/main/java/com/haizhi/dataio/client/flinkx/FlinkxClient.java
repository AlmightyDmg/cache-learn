package com.haizhi.dataio.client.flinkx;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.haizhi.dataio.job.action.FlinkAction;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:06:16
 */
@RetrofitClient(baseUrl = "${flinkx.url}")
public interface FlinkxClient {

    @POST("")
    @FormUrlEncoded
    String startJob(@Field("flinkActionParam") FlinkAction.FlinkActionParam flinkActionParam);

    @POST("")
    @FormUrlEncoded
    String getFlinkTask(@Field("taskId") String taskId);
}
