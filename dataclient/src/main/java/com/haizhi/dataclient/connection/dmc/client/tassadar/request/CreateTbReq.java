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
public class CreateTbReq extends TassadarReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@NotBlank
	@Field("name")
	private String name;
	@NotBlank
	@Field("type")
	private String type;
	@NotBlank
	@Field("fields")
	private String fields;
	@Default
	@Field("title")
	private String title = "";
	@Default
	@Field("db_id")
	private String dbId = null;
	@Default
	@Field("storage_id")
	private String storageId = null;
	@Default
	@Field("force_merge")
	private String forceMerge = null;
	@Default
	@Field("comment")
	private String comment = "";
	@Default
	@Field("data_name")
	private String dataName = "";
	@Default
	@Field("is_etl_tb")
	private Integer isEtlTb = 0;
	@Default
	@Field("tree_type")
	private Integer treeType = 0;
	@Default
	@Field("update_mode")
	private String updateMode = null;
	@Default
	@Field("partition")
	private String partition = null;
	@Default
	@Field("manage_type")
	private Integer manageType = 0;
	@Default
	@Field("tag")
	private String tag = null;

}
