// CHECKSTYLE:OFF
package com.haizhi.dataio.client.flinkx.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FlinkxResult<T> {

	private Integer status;
	private String msg;
	private String errstr;
	private T result;
	@JsonProperty("err_parameter")
	private Object errParameter;
}
