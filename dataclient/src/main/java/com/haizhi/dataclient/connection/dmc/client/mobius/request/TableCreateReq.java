package com.haizhi.dataclient.connection.dmc.client.mobius.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
public class TableCreateReq extends MobiusReqBase {
	@Field("schema")
	private List<String> schema;
	private String tbName;
	private Integer isPartition = 0;
	private String isStreaming = "0";

	@Data
	@SuperBuilder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CreateField {
		@JsonProperty("seq_no")
		private Integer seqNo;
		@JsonProperty("name")
		private String name;
		private String mapping;
		private String type = "string";
	}
}
