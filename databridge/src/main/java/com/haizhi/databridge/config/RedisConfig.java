package com.haizhi.databridge.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.haizhi.databridge.exception.DatabridgeException;
/**
 * redis配置类
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {


	static final int DEFAULT_KEY_CACHE_TTL = 30 * 60;    // 30min
	static final int KEY_CACHE_TTL = 10 * 60;    // 10min

	private static RedisTemplate<String, Object> redisTemplate;

	/**
	 * 　　* @description: 工具类内获取redisTemplate方法
	 * 　　* @param: ${tags}
	 * 　　* @return: ${return_type}
	 * 　　* @throws
	 * 　　* @author WangChengYu
	 * 　　* @date 2020-02-20 18:48
	 */
	public static RedisTemplate getRedisTemplate() {
		if (redisTemplate == null) {
			throw new DatabridgeException("redisTemplate初始化失败");
		}
		return redisTemplate;
	}
}
