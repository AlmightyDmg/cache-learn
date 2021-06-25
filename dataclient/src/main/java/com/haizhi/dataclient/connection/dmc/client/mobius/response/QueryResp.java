package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QueryResp {

	private Integer status;
	private List<List<String>> data;
	@JsonProperty("df_length")
	private Long dfLength;
	private Object schema;
	private String msg;

	@Data
	public static class Schema {
		private String name;
		private Integer type;
	}
}
