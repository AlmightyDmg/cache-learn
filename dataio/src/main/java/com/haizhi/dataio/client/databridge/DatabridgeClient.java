package com.haizhi.dataio.client.databridge;

import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;

import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.JobStateForm;
import com.haizhi.dataio.bean.JobUnitStateForm;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:06:16
 */
@RetrofitClient(baseUrl = "${databridge.url}")
@Intercept(handler = WebResultInterceptor.class)
public interface DatabridgeClient {

    @POST("/api/job/execute_info")
    @FormUrlEncoded
    DataTransJobDetail getJobExecInfo(@Field("jobId") String jobId, @Field("jobType") String jobType);

    @POST("/api/job/update_job")
    @FormUrlEncoded
    String updateJobStatus(@QueryBean JobStateForm jobStateForm);

    @POST("/api/job/update_job_task")
    @FormUrlEncoded
    String updateJobExecUnit(@QueryBean JobUnitStateForm stateForm);

    @POST("/api/job/update_job_task_rel")
    @FormUrlEncoded
    String updateJobTaskRel(@Field("jobId") String jobId,
                            @Field("taskId") String taskId,
                            @Field("fromTableId") String fromTableId,
                            @Field("toTableId") String toTableId,
                            @Field("owner") String owner);

    @POST("/api/job/job_finished")
    @FormUrlEncoded
    Boolean jobFinished(@Field("jobId") String jobId, @Field("jobType") String jobType);
}
