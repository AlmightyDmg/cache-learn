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
public class CreateFolderReq extends TassadarReqBase {
	@NotBlank
	@Field("role")
	private String role;
	@NotBlank
	@Field("user_id")
	private String userId;
	@NotBlank
	@Field("name")
	private String name;
	@NotBlank
	@Field("parent_id")
	private String parentId;
	@Default
	@Field("tree_type")
	private Integer treeType = 0;
	@Default
	@Field("ent_id")
	private String entId = "";
	@Default
	@Field("module_type")
	private String moduleType = "";

}
