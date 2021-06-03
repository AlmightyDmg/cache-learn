package com.haizhi.databridge.web.interceptor;

import static com.haizhi.databridge.constants.DatabridgeConstants.DMC_ENT_ID;
import static com.haizhi.databridge.constants.DatabridgeConstants.DMC_REQUEST;
import static com.haizhi.databridge.constants.DatabridgeConstants.INNER_CALL;
import static com.haizhi.databridge.constants.DatabridgeConstants.REQUEST_URI;
import static com.haizhi.databridge.constants.DatabridgeConstants.SESSION_ID;
import static com.haizhi.databridge.constants.DatabridgeConstants.TRACE_ID;
import static com.haizhi.databridge.constants.DatabridgeConstants.USER_ID;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CommonDataInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
		String traceFromRequest = req.getParameter(TRACE_ID);
		String traceId = !StringUtils.isEmpty(traceFromRequest) ? traceFromRequest : UUID.randomUUID().toString();
		MDC.put(TRACE_ID, traceId);

		MDC.put(DMC_REQUEST, req.getParameter(DMC_REQUEST));
		MDC.put(USER_ID, req.getParameter(USER_ID));
		MDC.put(SESSION_ID, req.getParameter(SESSION_ID));
		// inner_call＝1, 内部调用不校验用户ID
		MDC.put(INNER_CALL, req.getParameter(INNER_CALL));
		MDC.put(DMC_ENT_ID, req.getParameter(DMC_ENT_ID));

		MDC.put(REQUEST_URI, req.getRequestURI());
		return true;
	}

	@Override
	public void postHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		ModelAndView modelAndView) {
		/* MDC.clear(); */
	}

	@Override
	public void afterCompletion(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler, Exception ex) throws Exception {
		MDC.clear();
	}
}
