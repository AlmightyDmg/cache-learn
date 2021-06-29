package com.haizhi.databridge.client.behemoth.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@ToString(callSuper = true)
public class ChatListReq {
	@NotBlank
	@Field("chat_ids")
	@JsonProperty("chat_ids")
	private String chatIds;

}
