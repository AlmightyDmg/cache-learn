package com.haizhi.databridge.client.xxljob;

import java.util.Map;

import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;

import com.haizhi.databridge.client.xxljob.interceptor.XxlLoginInterceptor;
import com.haizhi.databridge.client.xxljob.request.LogQueryParam;
import com.haizhi.databridge.client.xxljob.request.PageQueryParam;
import com.haizhi.databridge.client.xxljob.request.XxlJobInfo;
import com.haizhi.databridge.client.xxljob.response.ReturnT;
/**
 * xxl-job service api interface
 */
@RetrofitClient(baseUrl = "${xxl-job.url}", enableLog = true)
@Intercept(exclude = "/xxl-job-admin/login", handler = XxlLoginInterceptor.class)
public interface XxlJobClient {

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/add")
    ReturnT<String> add(@QueryBean XxlJobInfo xxlJobInfo);

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/update")
    ReturnT<String> update(@QueryBean XxlJobInfo xxlJobInfo);
    
    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/pageList")
    Map<String, Object> pageList(@QueryBean PageQueryParam queryParam);

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/remove")
    ReturnT<String> remove(@Field("id") int id);

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/start")
    ReturnT<String> start(@Field("id") int id);

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/stop")
    ReturnT<String> stop(@Field("id") int id);

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/trigger")
    ReturnT<String> trigger(@Field("id") int id, @Field("executorParam") String executorParam);

    @FormUrlEncoded
    @POST("/xxl-job-admin/login")
    Call<ResponseBody> login(@Field("userName") String userName,
                             @Field("password") String password,
                             @Field("ifRemember") String ifRemember);

    @FormUrlEncoded
    @POST("/xxl-job-admin/joblog/pageList")
    Map<String, Object> logList(@QueryBean LogQueryParam logQueryParam);

    @FormUrlEncoded
    @POST("/xxl-job-admin/joblog/logKill")
    ReturnT<String> logKill(@Field("id") int id);
}
