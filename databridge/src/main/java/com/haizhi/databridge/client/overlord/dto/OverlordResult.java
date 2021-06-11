// CHECKSTYLE:OFF
package com.haizhi.databridge.client.overlord.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OverlordResult<T> {
	private Integer status;
	private String msg;
	private String errstr;
	private T result;
	@JsonProperty("err_parameter")
	private Object errParameter;
}
