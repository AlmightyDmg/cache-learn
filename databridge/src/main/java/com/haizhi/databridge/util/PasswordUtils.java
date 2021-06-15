package com.haizhi.databridge.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public final class PasswordUtils {
	private PasswordUtils() {
	}

	/**
	 * 将加密后的数据通过oxton服务解密
	 * 如果传入的数据不是加密格式的，则直接返回
	 * @param encryptedPassword
	 * @return
	 */
	public static String decrypt(String encryptedPassword) {
		if (encryptedPassword == null || "".equals(encryptedPassword)) {
			return encryptedPassword;
		}
		if (!encryptedPassword.startsWith("{") || !encryptedPassword.contains("}")) {
			return encryptedPassword;
		}
		String requestPath = encryptedPassword.replace("{", "/").replace("}", "/");
		String oxtonUrl = System.getenv("OXTON_URL");
		if (oxtonUrl == null) {
			oxtonUrl = "http://bdp-core:20511";
		}
		return decryptByOxton(oxtonUrl + requestPath);
	}

	private static String decryptByOxton(String requestUrl) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			StringBuilder data = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				data.append(line).append("\n");
			}
			// 有数据的话，去除最后一个没用的换行符
			if (data.length() > 0) {
				data.deleteCharAt(data.length() - 1);
			}
			reader.close();
			conn.disconnect();
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				return data.toString();
			}
			throw new Exception("Call oxton service failed: " + data.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encodeBase64(String text) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
	}

	public static String decodeBase64(String text) throws UnsupportedEncodingException {
		return new String(Base64.getDecoder().decode(text), "UTF-8");
	}
}
