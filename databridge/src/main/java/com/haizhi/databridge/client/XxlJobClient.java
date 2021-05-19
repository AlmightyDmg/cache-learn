package com.haizhi.databridge.client;

import com.github.lianjiatech.retrofit.spring.boot.annotation.Intercept;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;
import java.io.Serializable;
import java.util.Map;

/**
 * xxl-job service api interface
 */
@RetrofitClient(baseUrl = "${xxl-job.url}")
@Intercept(exclude = "/xxl-job-admin/login", handler = XxlLoginInterceptor.class)
public interface XxlJobClient {

    @FormUrlEncoded
    @POST("/xxl-job-admin/jobinfo/add")
    ReturnT<String> add(@QueryBean XxlJobInfo xxlJobInfo);
    
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
    ReturnT<String> trigger(@Field("id") int id);

    @FormUrlEncoded
    @POST("/xxl-job-admin/login")
    Call<ResponseBody> login(@Field("userName") String userName,
                             @Field("password") String password,
                             @Field("ifRemember") String ifRemember);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class XxlJobInfo {
        @Field("id")
        int id;				// 主键ID

        @Field("jobGroup")
        int jobGroup;		// 执行器主键ID

        @Field("jobDesc")
        String jobDesc;

        @Field("author")
        String author;		// 负责人

        @Field("alarmEmail")
        String alarmEmail;	// 报警邮件

        @Field("scheduleType")
        String scheduleType;			// 调度类型

        @Field("scheduleConf")
        String scheduleConf;			// 调度配置，值含义取决于调度类型

        @Field("misfireStrategy")
        String misfireStrategy;			// 调度过期策略

        @Field("executorRouteStrategy")
        String executorRouteStrategy;	// 执行器路由策略

        @Field("executorHandler")
        String executorHandler;		    // 执行器，任务Handler名称

        @Field("executorParam")
        String executorParam;		    // 执行器，任务参数

        @Field("executorBlockStrategy")
        String executorBlockStrategy;	// 阻塞处理策略

        @Field("executorTimeout")
        int executorTimeout;     		// 任务执行超时时间，单位秒

        @Field("executorFailRetryCount")
        int executorFailRetryCount;		// 失败重试次数

        @Field("glueType")
        String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum

        @Field("glueSource")
        String glueSource;		// GLUE源代码

        @Field("glueRemark")
        String glueRemark;		// GLUE备注

        @Field("childJobId")
        String childJobId;	// 子任务ID，多个逗号分隔
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class PageQueryParam {
        @Field("start") int start;
        @Field("length") int length;
        @Field("jobGroup") int jobGroup;
        @Field("triggerStatus") int triggerStatus;
        @Field("jobDesc") String jobDesc;
        @Field("executorHandler") String executorHandler;
        @Field("author") String author;
    }
    
    @Data
    @AllArgsConstructor
    class ReturnT<T> implements Serializable {
        public static final long serialVersionUID = 42L;

        public static final int SUCCESS_CODE = 200;
        public static final int FAIL_CODE = 500;

        public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
        public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);

        private int code;
        private String msg;
        private T content;

        public ReturnT() {
        }

        public ReturnT(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public ReturnT(T content) {
            this.code = SUCCESS_CODE;
            this.content = content;
        }
    }
}
