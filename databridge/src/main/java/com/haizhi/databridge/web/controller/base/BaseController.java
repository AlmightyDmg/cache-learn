package com.haizhi.databridge.web.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.haizhi.databridge.exception.ValidationErrorException;
import com.haizhi.databridge.util.ErrorsUtils;
import com.haizhi.databridge.util.RequestCommonData;

public class BaseController extends RequestCommonData {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	protected HttpServletRequest request;


	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private static final String[] HEADERS_TO_TRY = new String[] {
		"X-Forwarded-For", "Proxy-Client-IP",
		"WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"
	};

	public void check(Errors errors) {
		if (errors.hasErrors()) {
			throw new ValidationErrorException(ErrorsUtils.compositeValiditionError(errors), "check params errors");
		}
	}

	public String getRemoteIpFromHttpRequest() {
		String addr = getClientIpAddress(this.request);
		return !StringUtils.isEmpty(addr) ? addr.split(",")[0].trim() : addr;
	}

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}
