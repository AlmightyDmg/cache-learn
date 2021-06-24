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

	public static final class DataBaseType {
		public static final String MYSQL = "MYSQL";
		public static final String BINLOG = "BINLOG";
		public static final String KAFKA = "KAFKA";
		public static final String DATAHUB = "DATAHUB";
		public static final String API = "API";
	}

	public static final class DataTableStatus {
		public static final String ERROR = "error";
		public static final String FINISHED = "finished";
		public static final String SYNCING = "syncing";
		public static final String TERMINATED = "terminated";
		public static final String STATUS_NEW = "new";
		public static final String STATUS_IGNORED = "ignored";
	}

	public static final class SyncType {
    	public static final String SYNC_TYPE_FULL = "全量同步";
		public static final String SYNC_TYPE_INCREASE = "增量同步";
	}

	public static final class SyncCycle {
		public static final String SYNC_CYCLE_STOP = "--";
		public static final String SYNC_CYCLE_ORIGIN = "自定义";
		public static final String SYNC_CYCLE_DELTA = "每小时";
		public static final String SYNC_CYCLE_CRONTAB = "Crontab";
		public static final String SYNC_CYCLE_MINUTE = "分钟级";
	}

	public static final class SchedulerTiming {
		public static final String TIMING_TYPE_ORIGIN = "origin";
		public static final String TIMING_TYPE_DELTA = "delta";
		public static final String TIMING_TYPE_CRONTAB = "crontab";
		public static final String TIMING_TYPE_MINUTE = "minute";

	}

	public static final class SchedulerType {
    	public static final String NORMAL =  "NONE";
		public static final String CRON = "CRON";
		public static final String FIX_RATE = "FIX_RATE";
	}

	public static final class TaskType {
		public static final String IMPORT =  "import";
		public static final String EXPORT = "export";
	}

	public static final class DataBaseCrypterKey {
    	public static final String KEY = "nmdsPVv2z699voVC";
	}
}
