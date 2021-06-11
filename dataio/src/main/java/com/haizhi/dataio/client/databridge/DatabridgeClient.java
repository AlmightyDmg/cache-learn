package com.haizhi.dataio.client.databridge;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.JobUnitStateForm;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:06:16
 */
@RetrofitClient(baseUrl = "${databridge.url}")
public interface DatabridgeClient {

    @POST("/api/export_job/job/execute_info")
    @FormUrlEncoded
    DataTransJobDetail getJobExecInfo(@Field("jobId") String jobId);

    @POST("/api/job/update_job_status")
    String updateJobStatus(@Field("jobId") String jobId,
                           @Field("jobStatus") Integer jobStatus,
                           @Field("startTime") Long startTime,
                           @Field("endTime") Long endTime);

    @POST("/api/job/update_job_stask")
    String updateJobExecUnit(JobUnitStateForm stateForm);

    @POST("/api/job/update_job_stask_rel")
    String updateJobTaskRel(@Field("jobId") String jobId,
                            @Field("taskId") String taskId,
                            @Field("tableId") String tableId);
}
