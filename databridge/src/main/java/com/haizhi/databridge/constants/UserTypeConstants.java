package com.haizhi.databridge.constants;

/**
 * @author zhangshuaishuai
 */
public final class UserTypeConstants {
	private UserTypeConstants() { }

	/*
	user includes following type
    1. enterprise root user (role 0)
    2. personal user (role 1)
    2. normal admin user (role 2)
    3. normal user (role 3)
	 */
	public static final Integer ENTERPRISE_ROOT_USER_TYPE = 0;
	public static final Integer PERSONAL_USER_TYPE = 1;
	public static final Integer NORMAL_ADMIN_USER_TYPE = 2;
	public static final Integer NORMAL_USER_TYPE = 3;
}
