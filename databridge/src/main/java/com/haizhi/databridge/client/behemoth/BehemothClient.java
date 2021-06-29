package com.haizhi.databridge.client.behemoth;

import java.util.List;

import javax.validation.Valid;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;

import com.haizhi.databridge.client.behemoth.request.ChatListReq;
import com.haizhi.databridge.client.behemoth.request.RoleListReq;
import com.haizhi.databridge.client.behemoth.response.BehemothResult;
import com.haizhi.databridge.client.behemoth.response.ChatListResp;
import com.haizhi.databridge.client.behemoth.response.RoleListResp;

//@RetrofitClient(baseUrl = "${localdmc.behemoth.url}")
public interface BehemothClient {

	@POST("api/role/list")
	@FormUrlEncoded
	BehemothResult<List<RoleListResp>> roleList(@Valid @QueryBean RoleListReq request);

	@POST("api/chat/list")
	@FormUrlEncoded
	BehemothResult<List<ChatListResp>> chatList(@Valid @QueryBean ChatListReq request);
}
