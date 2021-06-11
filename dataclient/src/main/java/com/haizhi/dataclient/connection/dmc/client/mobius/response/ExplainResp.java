package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import lombok.Data;

@Data
public class ExplainResp {
	//状态码
	private int status;
	//sql的校验信息
	private String plan;

}
