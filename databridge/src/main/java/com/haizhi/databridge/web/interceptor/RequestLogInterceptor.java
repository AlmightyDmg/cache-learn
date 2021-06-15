package com.haizhi.databridge.web.interceptor;

import static com.haizhi.databridge.constants.DatabridgeConstant.TRACE_ID;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {
	private final Pattern lineRegexPattern = Pattern.compile("\t|\r|\n");
	private final int normalStatus = 200;

	@Override
	public boolean preHandle(
		HttpServletRequest req,
		HttpServletResponse resp,
		Object handler) {
		String traceFromRequest = req.getParameter(TRACE_ID);
		String traceId = !StringUtils.isEmpty(traceFromRequest) ? traceFromRequest : UUID.randomUUID().toString();
		req.setAttribute("reqStartTime", System.currentTimeMillis());
		req.setAttribute("traceId", traceId);
		logRequest(req);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
								Object handler, @Nullable Exception ex) throws Exception {
		long elasTime = System.currentTimeMillis() - (long) request.getAttribute("reqStartTime");
		if ("/".equals(request.getRequestURI())) {
			return;
		}
		if (response.getStatus() == normalStatus) {
			log.info(String.format("Request end: %s true %s ms", request.getAttribute("traceId"), elasTime));
		} else {
			log.info(String.format("Request end: %s false %s ms", request.getAttribute("traceId"), elasTime));
		}
	}

	private void logRequest(HttpServletRequest request) {
		if ("/".equals(request.getRequestURI())) {
			return;
		}
		Matcher m = lineRegexPattern.matcher(getRequestParam(request));
		String realIp = request.getHeader("X-Real-Ip");
		if (realIp == null) {
			realIp = request.getRemoteHost();
		}
		log.info(String.format("%s %s %s?%s [%s]", realIp, request.getMethod(),
			request.getRequestURI(), m.replaceAll(" "), request.getAttribute("reqId")));
	}

	private String getRequestParam(HttpServletRequest request) {
		Set<String> paramKeys = new HashSet<>();
		for (String param : request.getParameterMap().keySet()) {
			String formattedParam = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, param);
			paramKeys.add(formattedParam);
		}

		StringBuilder sBuilder = new StringBuilder();
		for (String paramKey : paramKeys) {
			sBuilder.append(paramKey).append("=").append(request.getParameter(paramKey)).append("&");
		}

		if (sBuilder.length() > 1) {
			sBuilder.deleteCharAt(sBuilder.length() - 1);
		}
		return sBuilder.toString();
	}
}
