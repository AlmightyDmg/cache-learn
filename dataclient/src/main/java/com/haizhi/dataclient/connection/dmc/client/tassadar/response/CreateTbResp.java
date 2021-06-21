package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTbResp {
	private Integer version;
	@JsonProperty("real_id")
	private Integer realVersion;
	@JsonProperty("data_size")
	private Integer dataSize;
	@JsonProperty("raw_size")
	private Integer rawSize;

	@JsonProperty("tb_id")
	private String tbId;
	@JsonProperty("storage_id")
	private String storageId;
	private String owner;

	private List<TbField> fields;
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class TbField {
		private Integer type;
		private String remark;
		private String name;
		private String title;

		@JsonProperty("seq_no")
		private Integer seqNo;
		@JsonProperty("uniq_index")
		private Integer uniqIndex;
		@JsonProperty("field_id")
		private String fieldId;
		@JsonProperty("real_type")
		private String realType;
	}
}
