// CHECKSTYLE:OFF
package com.haizhi.databridge.client.overlord;


import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.haizhi.databridge.client.overlord.dto.OverlordResult;
import com.haizhi.databridge.client.overlord.request.GroupInfosReq;
import com.haizhi.databridge.client.overlord.request.InfoReq;
import com.haizhi.databridge.client.overlord.request.ListReq;
import com.haizhi.databridge.client.overlord.response.GroupInfosResp;
import com.haizhi.databridge.client.overlord.response.InfoResp;
import com.haizhi.databridge.client.overlord.response.ListResp;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryBean;
import java.util.List;

import javax.validation.Valid;

@RetrofitClient(baseUrl = "${localdmc.overlord.url}")
public interface OverlordClient {
	@POST("api/account/info")
	@FormUrlEncoded
	OverlordResult<InfoResp> info(@Valid @QueryBean InfoReq request);

	@POST("api/account/list")
	@FormUrlEncoded
	OverlordResult<List<ListResp>> list(@Valid @QueryBean ListReq request);

	@POST("api/group/infos")
	@FormUrlEncoded
	OverlordResult<List<GroupInfosResp>> groupInfos(@Valid @QueryBean GroupInfosReq request);
}
