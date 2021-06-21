package com.haizhi.dataclient.connection.dmc.client.tassadar;

import javax.validation.Valid;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.InfoTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbFileReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ModifyTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.CreateTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbFileResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.ModifyTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;

public interface TassadarClient {
    @POST("tb/create")
    @FormUrlEncoded
    TassadarResult<CreateTbResp> createTb(@Valid @QueryBean CreateTbReq request);

    @POST("tb/data/mergefile")
    @FormUrlEncoded
    TassadarResult<MergeTbFileResp> mergeTbFile(@Valid @QueryBean MergeTbFileReq request);

    @POST("tb/modify")
    @FormUrlEncoded
    TassadarResult<ModifyTbResp> modifyTb(@Valid @QueryBean ModifyTbReq request);

    @POST("tb/info")
    @FormUrlEncoded
    TassadarResult<InfoTbResp> infoTb(@Valid @QueryBean InfoTbReq request);
}
