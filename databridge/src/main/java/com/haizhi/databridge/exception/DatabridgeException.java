package com.haizhi.databridge.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.haizhi.databridge.web.result.StatusCode;
import java.util.Map;

public class DatabridgeException extends RuntimeException {
	private static final long serialVersionUID = -940285811464169752L;

	private StatusCode status;

	@JsonProperty("err_parameter")
	private Map<String, Object> errorParam;

	public DatabridgeException(String msg) {
		super(msg);
		status = StatusCode.API_INTERNAL_ERROR;
	}

	public DatabridgeException(StatusCode status, String msg) {
		super(msg);
		this.status = status;
	}

	public DatabridgeException(StatusCode status) {
		super(status.getMsg());
		this.status = status;
	}

	public DatabridgeException(Throwable throwable) {
		super(throwable);
		status = StatusCode.API_INTERNAL_ERROR;
	}

	public DatabridgeException(String msg, Throwable throwable) {
		super(msg, throwable);
		status = StatusCode.API_INTERNAL_ERROR;
	}

	public DatabridgeException(String msg, Throwable throwable, StatusCode status) {
		super(msg, throwable);
		this.status = status;
	}

	public DatabridgeException(Throwable throwable, StatusCode status) {
		super(throwable);
		this.status = status;
	}

	public DatabridgeException(StatusCode status, String msg, Map<String, Object> errorParam) {
		super(msg);
		this.status = status;
		this.errorParam = errorParam;
	}

	public StatusCode getStatus() {
		return status;
	}

	public Map<String, Object> getErrorParam() {
		return errorParam;
	}
}
