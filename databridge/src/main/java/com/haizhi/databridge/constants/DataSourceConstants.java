package com.haizhi.databridge.constants;

public final class DataSourceConstants {
    private DataSourceConstants() {
    }

	// 数据来源类型，0：关系和非关系行数据库，1：流式数据库，kafka、datahub 2. restapi
	public static final class SourceType {
		public static final Integer SOURCE_FROM_DB = 0;
		public static final Integer SOURCE_FROM_STREAMING = 1;
		public static final Integer SOURCE_FROM_API = 2;

	}

	// 行为是读数据源还是写数据源
	public static final class RoleType {
    	public static final String DB_ROLE_WRITER = "WRITER";
    	public static final String DB_ROLE_READER = "READER";
	}
}
