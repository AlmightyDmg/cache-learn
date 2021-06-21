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
public class ModifyTbReq extends TassadarReqBase {
	@NotBlank
	@Field("tb_id")
	private String tbId;
	@Default
	@Field("user_id")
	private String userId = "";
	@Default
	@Field("db_id")
	private String dbId = null;
	@Default
	@Field("title")
	private String title = null;
	@Default
	@Field("status")
	private Integer status = null;
	@Default
	@Field("storage_id")
	private String storageId = null;
	@Default
	@Field("data_count")
	private String dataCount = null;
	@Default
	@Field("error_msg")
	private String errorMsg = null;
	@Default
	@Field("version")
	private String version = null;
	@Default
	@Field("iVersion")
	private String iVersion = null;
	@Default
	@Field("tag")
	private String tag = null;
	@Default
	@Field("label")
	private String label = null;
	@Default
	@Field("comment")
	private String comment = null;
	@Default
	@Field("force_merge")
	private String forceMerge = null;
	@Default
	@Field("is_etl_tb")
	private Integer isEtlTb = 0;

}
