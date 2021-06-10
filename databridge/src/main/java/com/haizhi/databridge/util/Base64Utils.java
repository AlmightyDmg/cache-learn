package com.haizhi.databridge.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public final class Base64Utils {
	private Base64Utils() {
	}

	public static String encodeBase64(String text) throws UnsupportedEncodingException {
		return Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
	}

	public static String decodeBase64(String text) throws UnsupportedEncodingException {
		return new String(Base64.getDecoder().decode(text), "UTF-8");
	}
}
