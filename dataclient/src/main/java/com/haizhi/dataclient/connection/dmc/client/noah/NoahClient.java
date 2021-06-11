package com.haizhi.dataclient.connection.dmc.client.noah;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:49:01
 */
public interface NoahClient {
    @POST
    @FormUrlEncoded
    void startImportJob(String jobid);
}
