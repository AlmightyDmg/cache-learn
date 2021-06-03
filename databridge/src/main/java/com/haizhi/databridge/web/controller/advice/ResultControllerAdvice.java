package com.haizhi.databridge.web.controller.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.haizhi.databridge.web.result.WebResult;

/**
 * 统一数据返回格式处理
 */
@ControllerAdvice(basePackages = "com.haizhi.databridge.web")
public class ResultControllerAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(
			Object body,
			MethodParameter methodParameter,
			MediaType mediaType,
			Class<? extends HttpMessageConverter<?>> aClass,
			ServerHttpRequest req,
			ServerHttpResponse resp) {
		if (mediaType.compareTo(MediaType.APPLICATION_JSON) != 0) {
			return body;
		}

		return body instanceof WebResult ? body : WebResult.of(body);
	}
}
