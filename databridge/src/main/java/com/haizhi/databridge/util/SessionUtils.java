package com.haizhi.databridge.util;

import java.util.concurrent.TimeUnit;

import com.haizhi.databridge.config.RedisConfig;
/**
 * SessionUtil
 */
public final class SessionUtils {
	private SessionUtils() {
	}

	private static final String SESSION_PREFIX = "GLOBAL_SESSION";
	private static final Long SESSION_TTL = 24 * 60 * 60L;


	private static String buildSessionKey(String sessionId) {
		return String.format("%s:%s", SESSION_PREFIX, sessionId);
	}

	public static void resetTTL(String sessionId) {
		String sessionKey = buildSessionKey(sessionId);
		RedisConfig.getRedisTemplate().expire(sessionKey, SESSION_TTL, TimeUnit.SECONDS);
	}

	public static String getSessionInfoByKey(String sessionId, String key) {
		String sessionKey = buildSessionKey(sessionId);
		return (String) RedisConfig.getRedisTemplate().opsForHash().get(sessionKey, key);
	}

	public static String getEnterpriseId(String sessionId) {
		return getSessionInfoByKey(sessionId, "enterprise_id");
	}

	public static String getUserId(String sessionId) {
		return getSessionInfoByKey(sessionId, "user_id");
	}

	public static String getUserName(String sessionId) {
		return getSessionInfoByKey(sessionId, "username");
	}
}
