// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TassadarResult<T> {

	private Integer status;
	private T result;
	private String errstr;
	@JsonProperty("err_parameter")
	private Object errParameter;
	private String trcid;
}
