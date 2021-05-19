package com.haizhi.databridge.exception;

public class ReturnToHttpException extends RuntimeException {
	private static final long serialVersionUID = 1261587605538248704L;

	private int httpCode;
	// 用于返回response的body
	private Object body;

	public ReturnToHttpException(int httpCode, Object body, String message, Object... data) {
		super(String.format(message, data));
		this.httpCode = httpCode;
		this.body = body;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public Object getBody() {
		return body;
	}
}
