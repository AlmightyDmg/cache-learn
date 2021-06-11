// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MobiusResult<T> {
	private Integer status;
	private String msg;
	private String errstr;
	private T result;
	@JsonProperty("err_parameter")
	private Object errParameter;
}
