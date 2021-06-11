// CHECKSTYLE:OFF
package com.haizhi.databridge.client.behemoth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BehemothResult<T> {
	private Integer status;
	private String msg;
	private String errstr;
	private T result;
	@JsonProperty("err_parameter")
	private Object errParameter;
	@JsonProperty("trace_id")
	private String traceId;
}
