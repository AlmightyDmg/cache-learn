package com.haizhi.databridge.client.behemoth.request;

import lombok.experimental.SuperBuilder;
import retrofit2.http.Query;

import com.haizhi.databridge.client.common.ReqBase;

@SuperBuilder
public class BehemothReqBase extends ReqBase {
	@Query("inner_user_id")
	private String innerUserId;		// 内部调用

	public String getInnerUserId() {
		return null == innerUserId || "".equals(innerUserId) ? "innerFlag" : innerUserId;
	}

}
