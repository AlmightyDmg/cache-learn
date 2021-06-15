package com.haizhi.dataclient.connection.dmc.client.endpoint;

import java.util.Map;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 16:20:46
 */
public interface EndpointClient {

    @POST
    @FormUrlEncoded
    Map<String, String> getEndpoints();
}
