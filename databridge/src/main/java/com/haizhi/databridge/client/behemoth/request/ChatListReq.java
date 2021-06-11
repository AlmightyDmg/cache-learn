package com.haizhi.databridge.client.behemoth.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ChatListReq extends BehemothReqBase {
	@NotBlank
	@Field("chat_ids")
	private String chatIds;

}
