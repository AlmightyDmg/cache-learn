package com.haizhi.databridge.config;

import com.google.common.base.CaseFormat;
import com.haizhi.databridge.web.interceptor.CommonDataInterceptor;
import com.haizhi.databridge.web.interceptor.RequestLogInterceptor;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * @description: 添加web过滤器
	 * @author WangChengYu
	 * @date 2020-02-20 17:09
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CommonDataInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(new RequestLogInterceptor()).addPathPatterns("/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	/**
	 * 将snake case（lower undersocre）风格的参数转换为camel风格
	 * 以便兼容之前python服务定义的参数，同时保持Java的编码风格
	 */
	@Bean
	public Filter snakeConverter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
				throws ServletException, IOException {
				final Map<String, String[]> formattedParams = new HashMap<>();

				for (String param : request.getParameterMap().keySet()) {
					String[] paramValues = request.getParameterValues(param);
					formattedParams.put(param, paramValues);

					String formattedParam = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param);
					formattedParams.put(formattedParam, paramValues);
				}

				filterChain.doFilter(new HttpServletRequestWrapper(request) {
					@Override
					public String getParameter(String name) {
						return formattedParams.containsKey(name) ? formattedParams.get(name)[0] : null;
					}

					@Override
					public Enumeration<String> getParameterNames() {
						return Collections.enumeration(formattedParams.keySet());
					}

					@Override
					public String[] getParameterValues(String name) {
						return formattedParams.get(name);
					}

					@Override
					public Map<String, String[]> getParameterMap() {
						return formattedParams;
					}
				}, response);
			}
		};
	}
}
