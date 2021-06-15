package com.haizhi.databridge.client.overlord.response;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoResp {
	private String userId;
	private Integer role;
	private String username;
	private String name;
	private Integer status;

	// 企业相关
	@JsonProperty("enterprise_id")
	private String enterpriseId;
	@JsonProperty("enterprise_name")
	private String enterpriseName;
	private String domain;
	@JsonProperty("enterprise_type")
	private Integer enterpriseType;

	// 权限相关
	@JsonProperty("dsh_manage")
	private Integer dshManage;
	@JsonProperty("tb_manage")
	private Integer tbManage;
	@JsonProperty("account_permission")
	private Integer accountPermission;

	// e.g. {"group_id": "xxx", "group_name": "全公司", "product_list": "[\"pro_001\"]"}
	@JsonProperty("groups")
	private List<HashMap<String, String>> groupList;

	// e.g. {"chat_id": "xxx", "chat_name": "全公司", "product_list": "[\"pro_001\"]"}
	@JsonProperty("chats")
	private List<HashMap<String, String>> chats;

	// e.g. {"role_id": "xxx", "role_name": "全公司", "role_info": "desc"}
	@JsonProperty("roles")
	private List<HashMap<String, String>> roles;
}
