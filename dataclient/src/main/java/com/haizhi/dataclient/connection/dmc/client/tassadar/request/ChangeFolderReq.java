package com.haizhi.dataclient.connection.dmc.client.tassadar.request;

import java.util.Collections;
import java.util.List;

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
public class ChangeFolderReq extends TassadarReqBase {
	@Field("role")
	private Integer role;
	@NotBlank
	@Field("user_id")
	private String userId;
	@Default
	@Field("groups")
	private List<Object> groups = Collections.emptyList();
	@Default
	@Field("tb_id")
	private String tbId = null;
	@Default
	@Field("to_folder")
	private String toFolder = null;
	@Default
	@Field("to_seq")
	private Integer toSeq = -1;
	@Default
	@Field("tb_index")
	private Integer tbIndex = -1;
	@Default
	@Field("tree_type")
	private Integer treeType = 0;
	@Default
	@Field("opt_id")
	private String optId = "";
	@Default
	@Field("ent_id")
	private String entId = "";
	@Default
	@Field("module_type")
	private String moduleType = "";

}
