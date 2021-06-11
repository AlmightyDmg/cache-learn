package com.haizhi.databridge.client.common;

import java.util.Objects;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.slf4j.MDC;
import retrofit2.http.Query;

@Data
@SuperBuilder
public class ReqBase {

	@Query("trace_id")
	private String traceId;

	@Query("dmc_request")
	private Integer dmcRequest;

	@Query("session_id")
	private String sessionId;

	public String getTraceId() {
		return (traceId != null) ? traceId : Objects.toString(MDC.get("trace_id"));
	}

	public Integer getDmcRequest() {
		return (dmcRequest != null) ? dmcRequest
				: ((MDC.get("dmc_request") != null) ? Integer.parseInt(MDC.get("dmc_request")) : null);
	}

	public String getSessionId() {
		return (sessionId != null) ? sessionId
			: ((MDC.get("session_id") != null) ? Objects.toString(MDC.get("session_id")) : null);
	}
}
