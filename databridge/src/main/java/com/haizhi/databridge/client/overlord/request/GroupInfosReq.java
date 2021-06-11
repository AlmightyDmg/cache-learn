package com.haizhi.databridge.client.overlord.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import retrofit2.http.Field;

import com.haizhi.databridge.client.overlord.dto.OverlordReqBase;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupInfosReq extends OverlordReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@Field("group_id_list")
	private String groupIdList;

}
