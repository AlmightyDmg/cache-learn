package com.haizhi.databridge.client;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import com.haizhi.databridge.client.XxlJobClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import retrofit2.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author duanxiaoyi
 * @Description xxl login
 * @createTime 2021年05月19日 15:24:55
 */
@Slf4j
public class XxlLoginInterceptor extends BasePathMatchInterceptor {
    public static final String LOGIN_IDENTITY_KEY = "XXL_JOB_LOGIN_IDENTITY";
    private static final String HEADER_COOKIE = "Cookie";
    private static final String SET_COOKIE_HEADER = "Set-Cookie";

    private static String cookieValue = "";

    @Value("${xxl-job.username}")
    private String username;

    @Value("${xxl-job.password}")
    private String passwd;

    @Autowired
    private XxlJobClient xxlJobClient;

    @Override
    protected okhttp3.Response doIntercept(Chain chain) throws IOException {
        if (StringUtils.isEmpty(cookieValue)) {
            Response response = xxlJobClient.login(username, passwd, "on").execute();
            cookieValue = response.headers().get(SET_COOKIE_HEADER);
        }

        if (StringUtils.isNotEmpty(cookieValue)) {
            Request request = chain.request().newBuilder()
                    .addHeader(HEADER_COOKIE, cookieValue)
                    .build();
            return chain.proceed(request);
        } else {
            log.error("xxl-job login error");
            return chain.proceed(chain.request());
        }
    }
}
