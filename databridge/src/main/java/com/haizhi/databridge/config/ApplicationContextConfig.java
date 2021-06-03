package com.haizhi.databridge.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author WangChengYu
 * @description: spring容器对象的静态访问工具
 * @param: ${tags}
 * @return: ${return_type}
 * @throws
 * @date 2020-02-11 22:29
 */
@Configuration
public class ApplicationContextConfig implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ApplicationContextConfig.applicationContext == null) {
			ApplicationContextConfig.applicationContext = applicationContext;
		}
	}

	/**
	 * @throws
	 * @description: 获取applicationContext
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:31
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
