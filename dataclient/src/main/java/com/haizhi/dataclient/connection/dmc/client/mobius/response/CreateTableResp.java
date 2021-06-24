package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTableResp {
	private Integer status;
	private String msg;
	private String errstr;
	private String tbName;
	@JsonProperty("err_parameter")
	private Object errParameter;
}
