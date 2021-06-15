package com.haizhi.databridge.util;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import com.haizhi.databridge.config.ApplicationContextConfig;
/**
 * @author WangChengYu
 * @description: spring容器对象的静态访问工具
 * @date 2020-02-11 22:29
 */
public final class SpringUtils {

	private SpringUtils() {
	}

	/**
	 * 　　* @description: 获取ApplicationContext对象
	 * 　　* @param: ${tags}
	 * 　　* @return: ${return_type}
	 * 　　* @throws
	 * 　　* @author WangChengYu
	 * 　　* @date 2020-02-20 18:09
	 */
	public static ApplicationContext getApplicationContext() {
		return ApplicationContextConfig.getApplicationContext();
	}

	/**
	 * @throws
	 * @description: 通过name获取 Bean
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:31
	 */
	public static Object getBean(String name) {
		return ApplicationContextConfig.getApplicationContext().getBean(name);
	}

	/**
	 * @throws
	 * @description: 通过class获取Bean.
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:32
	 */
	public static <T> T getBean(Class<T> clazz) {
		return ApplicationContextConfig.getApplicationContext().getBean(clazz);
	}

	/**
	 * @throws
	 * @description: 通过name, 以及Clazz返回指定的Bean
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:32
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return ApplicationContextConfig.getApplicationContext().getBean(name, clazz);
	}

	/**
	 * @throws
	 * @description: 通过Clazz返回指定的Beans
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:32
	 */
	public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
		return ApplicationContextConfig.getApplicationContext().getBeansOfType(clazz);
	}

	/**
	 * @throws
	 * @description: 通过key，读取属性值
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:33
	 */
	public static <T> T getProperty(String name, Class<T> clazz) {
		Environment environment = ApplicationContextConfig.getApplicationContext().getEnvironment();
		return environment.getProperty(name, clazz);
	}

	/**
	 * @throws
	 * @description: 通过key，读取属性值，支持设置默认值
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 22:33
	 */
	public static <T> T getProperty(String name, Class<T> clazz, T defaultValue) {
		Environment environment = ApplicationContextConfig.getApplicationContext().getEnvironment();
		return environment.getProperty(name, clazz, defaultValue);
	}

	/**
	 * @throws
	 * @description: 解析并返回带占位符的字符串
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @author WangChengYu
	 * @date 2020-02-11 23:04
	 */
	public static String resolvePlaceholders(String text) {
		Environment environment = ApplicationContextConfig.getApplicationContext().getEnvironment();
		return environment.resolvePlaceholders(text);
	}

}
