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
public class ModifyFieldReq extends TassadarReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@NotBlank
	@Field("field_id")
	private String fieldId;
	@Default
	@Field("title")
	private String title = null;
	@Default
	@Field("uniq_index")
	private Integer uniqIndex = null;
	@Default
	@Field("type")
	private Integer type = null;
	@Default
	@Field("seq_no")
	private String seqNo = null;
	@Default
	@Field("aggregator")
	private String aggregator = null;
	@Default
	@Field("param")
	private String param = null;
	@Default
	@Field("flag")
	private String flag = null;
	@Default
	@Field("remark")
	private String remark = null;
	@Default
	@Field("is_display")
	private String isDisplay = null;
	@Default
	@Field("query_for_ct")
	private String queryForCt = null;
	@Default
	@Field("assist_field")
	private Integer assistField = 0;
	@Default
	@Field("user_belong")
	private String userBelong = null;
	@Default
	@Field("operator_id")
	private String operatorId = null;

}
