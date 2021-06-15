package com.haizhi.databridge.client.overlord.request;

import javax.validation.constraints.NotBlank;

import lombok.Builder.Default;
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
public class InfoReq extends OverlordReqBase {
	@NotBlank
	@Field("user_id")
	private String userId;
	@Default
	@Field("sub_id")
	private String subId = "";
	@Default
	@Field("details")
	private Integer details = 0;

}
