package com.haizhi.dataclient.connection.dmc.client.tassadar;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.client.mobius.response.CreateFolderResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ChangeFolderReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CheckTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.DeleteMapTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.DeleteTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.InfoTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbFileReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ModifyTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.CreateTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.DeleteMapTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.DeleteTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbFileResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.ModifyTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.StandartableInfoResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;

public interface TassadarClient {
    @POST("tb/create")
    @FormUrlEncoded
    TassadarResult<CreateTbResp> createTb(@Valid @QueryBean CreateTbReq request);

    @POST("tb/data/merge")
    @FormUrlEncoded
    TassadarResult<MergeTbResp> mergeTb(@Valid @QueryBean MergeTbReq request);

    @POST("tb/data/mergefile")
    @FormUrlEncoded
    TassadarResult<MergeTbFileResp> mergeTbFile(@Valid @QueryBean MergeTbFileReq request);

    @POST("tb/modify")
    @FormUrlEncoded
    TassadarResult<ModifyTbResp> modifyTb(@Valid @QueryBean ModifyTbReq request);

    @POST("tb/info")
    @FormUrlEncoded
    TassadarResult<InfoTbResp> infoTb(@Valid @QueryBean InfoTbReq request);

    @POST("folder/createifnotexist")
    @FormUrlEncoded
    TassadarResult<CreateFolderResp> createFolderIfNotExist(@NotBlank @Field("role") Integer role,
                                                            @NotBlank @Field("user_id") String userId,
                                                            @NotBlank @Field("folder_name") String folderName,
                                                            @Field("ent_id") String entId);

    @POST("folder/change")
    @FormUrlEncoded
    TassadarResult<String> changeFolder(@Valid @QueryBean ChangeFolderReq request);

    @POST("tb/delete")
    @FormUrlEncoded
    TassadarResult<DeleteTbResp> deleteTb(@Valid @QueryBean DeleteTbReq request);

    @POST("tb/deletemaptb")
    @FormUrlEncoded
    TassadarResult<DeleteMapTbResp> deleteMapTb(@Valid @QueryBean DeleteMapTbReq request);

    @POST("tb/check")
    @FormUrlEncoded
    TassadarResult<Boolean> checkTb(@Valid @QueryBean CheckTbReq request);

    @POST("tb/standarinfo")
    @FormUrlEncoded
    TassadarResult<StandartableInfoResp> standartableInfo(@NotBlank @Field("tb_id") String tbId,
                                                          @NotBlank @Field("user_id") String userId);
}
