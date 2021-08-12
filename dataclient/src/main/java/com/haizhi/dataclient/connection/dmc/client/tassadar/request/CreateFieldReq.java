package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CreateFieldReq extends TassadarReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@NotBlank
	@Field("name")
	private String name;
	@NotBlank
	@Field("type")
	private String type;
	@Default
	@Field("title")
	private String title = "";
	@Default
	@Field("uniq_index")
	private Integer uniqIndex = 0;
	@Default
	@Field("aggregator")
	private String aggregator = "";
	@Default
	@Field("param")
	private String param = "";
	@Default
	@Field("flag")
	private String flag = "";
	@Default
	@Field("is_display")
	private String isDisplay = "";
	@Default
	@Field("remark")
	private String remark = "";
	@Default
	@Field("assist_field")
	private Integer assistField = 0;
	@Default
	@Field("field_id")
	private String fieldId = "";
	@Default
	@Field("seq_no")
	private String seqNo = null;
	@Default
	@Field("operator_id")
	private String operatorId = null;
}
