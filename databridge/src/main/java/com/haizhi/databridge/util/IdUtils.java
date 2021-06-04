package com.haizhi.databridge.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.haizhi.databridge.constants.MetaConstants;

public final class IdUtils {
	private IdUtils() {
	}

	public static String md5(String str) {
		return DigestUtils.md5DigestAsHex(str.getBytes());
	}

	public static Map<String, Object> bean2map(Object bean) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 获取JavaBean的描述器
		BeanInfo b = Introspector.getBeanInfo(bean.getClass(), Object.class);
		// 获取属性描述器
		PropertyDescriptor[] pds = b.getPropertyDescriptors();
		// 对属性迭代
		for (PropertyDescriptor pd : pds) {
			// 属性名称
			String propertyName = pd.getName();
			// 属性值,用getter方法获取
			Method m = pd.getReadMethod();
			Object properValue = m.invoke(bean); // 用对象执行getter方法获得属性值

			// 把属性名-属性值 存到Map中
			map.put(propertyName, properValue);
		}
		return map;
	}
	
	/**
	 * 生成业务ID：folder_xxx， tb_xxx
	 * 
	 * @param prefix
	 * @return
	 */
	public static String genKey(String prefix) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return String.format("%s_%s", prefix, uuid);
	}

	public static String genKey() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}

	/**
	 * 随机生成底层表名，即storageId。
	 * 为了避免一些引擎无法处理数字开头的表名，storageId，已d字母开头，f字母结尾，d\f无特殊含义
	 */
	public static String createStorageId() {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String sortUuid = uuid.substring(1, uuid.length() - 1);
		return String.format("d%sf", sortUuid);
	}

	public static String generateUniqueId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	// 生成uniq_id
	public static String uniqId(String prefix) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		if (StringUtils.isEmpty(prefix)) {
			return uuid;
		}
		return String.format("%s_%s", prefix, uuid);
	}

	public static String createFieldId(String baseInfo) {
		int start = 0;
		return "fk" + md5(baseInfo).substring(start, MetaConstants.MachineLearning.INDEX_S);
	}

	public static String createFieldId() {
		int start = 0;
		String key = genKey();
		return "fk" + key.substring(start, MetaConstants.MachineLearning.INDEX_S);
	}

	public static String createStreamingId() {
		return uniqId("streaming");
	}

}
