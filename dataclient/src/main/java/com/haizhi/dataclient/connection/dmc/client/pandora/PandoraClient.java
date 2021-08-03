package com.haizhi.dataclient.connection.dmc.client.pandora;

import javax.validation.constraints.NotBlank;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.haizhi.dataclient.connection.dmc.client.pandora.resp.ChartGetListByTbResp;
import com.haizhi.dataclient.connection.dmc.client.pandora.resp.PandoraResult;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:49:01
 */
public interface PandoraClient {
    @POST("api/chart/get_list_by_tb")
    @FormUrlEncoded
    PandoraResult<ChartGetListByTbResp> chartGetListByTb(@NotBlank @Field("user_id") String userId,
                                                         @NotBlank @Field("tb_id") String tbId, @Field("offset") Integer offset,
                                                         @Field("limit") Integer limit, @Field("public") Integer publicRequest);
}
