package com.haizhi.databridge.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.util.DLock;

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

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(stringRedisSerializer);

		// 设置hash key 和value序列化模式
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(stringRedisSerializer);
		template.afterPropertiesSet();

		redisTemplate = template;
		return template;
	}

	@Bean
	public DLock dLock(RedisConnectionFactory factory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(stringRedisSerializer);

		// 设置hash key 和value序列化模式
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(stringRedisSerializer);
		template.afterPropertiesSet();

		return new DLock(template);
	}
}
