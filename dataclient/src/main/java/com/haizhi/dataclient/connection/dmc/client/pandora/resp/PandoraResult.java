// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.pandora.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PandoraResult<T> {

	private Integer status;
	private T result;
	private String errstr;
	@JsonProperty("err_parameter")
	private Object errParameter;
}
