package com.haizhi.databridge.client.behemoth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatListResp {
	@JsonProperty("chat_id")
	private String chatId;

	@JsonProperty("chat_name")
	private String chatName;

	@JsonProperty("ent_id")
	private String entId;
}
