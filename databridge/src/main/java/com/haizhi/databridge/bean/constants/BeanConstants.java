package com.haizhi.databridge.bean.constants;

import java.util.HashMap;
import java.util.Map;

import com.haizhi.databridge.constants.MetaConstants;

public final class BeanConstants {
    private BeanConstants() {
    }

    public static final class TbConstants {
        private TbConstants() {
        }

        // 额外字段，不做存储，从Field表中拼装出来
        public static final String FIELDS = "fields";
        public static final String ROW_FILTER = "row_filter";
        public static final String INCR_COUNT = "incr_count";
        public static final String REAL_VERSION = "real_id";  // 用于TB().get_tb(),如果是分享表则返回被分享表的VERSOIN,否则返回实际表VERSION
        public static final String SPECIAL_STATUS = "special_status";  // 4, 5 为di云表单使用, 7为合表延迟执行
        public static final String TYPE_FIXED = "type_fixed";  // 该表是否需要转换字段类型
        public static final String RAW_SIZE = "raw_size";  // 未压缩前的数据大小
        public static final String URL_CONFIG = "url_config";

        //"""数据表类型定义"""
        public static final Integer EXCEL_TYPE = 0;
        public static final Integer OPENDS_TYPE = 1;
        public static final Integer DS_TYPE = 2;
        public static final Integer VIEW_TYPE = 3;
        public static final Integer SHARED_TYPE = 4;
        public static final Integer PUBLIC_TYPE = 5;
        public static final Integer BUSINESS_TYPE = 6;  // 业务表,其他系统同步过来的,如SEM固化项目
        public static final Integer GRAFT_TYPE = 10;  // 嫁接表

        public static final Integer HIVE_MAPPING_TYPE = 11;
        public static final Integer ODPS_MAPPING_TYPE = 12;  // 阿里云MaxCompute表
        public static final Integer SNAPSHOT_TYPE = 13;  // 快照表，无编辑权限，只有删除，参与合表权限
        public static final Integer JDBC_MAPPING_TYPE = 14;  // jdbc 映射表类型
        public static final Integer MAPTB_TYPE = 15;  // "生成标准字典表"; 类型
        public static final Integer DATAFLOW_TYPE = 16;  // 模型做出的结果表 2019Q1重构版
        public static final Integer TOPIC_TYPE = 18;  // 主题表
        public static final Integer TOPIC_DEF_TYPE = 19;  // 自定义主题表

        public static final Integer OPENDS_STREAMING_TYPE = 20;
        public static final Integer KAFKA_STREAMING_TYPE = 21;  // 对接用户kafka流式表

		public static final Integer ELEMENT_TYPE = 22;  // 要素表
		public static final Integer RELATION_TYPE = 26;

		public static final Integer DATAHUB_STREAMING_TYPE = 23;  // 对接用户datahub流式表
		public static final Integer DMC_KAFKA_STREAMING_TYPE = 24;  // dmc流式模型落盘的kafka表
		public static final Integer DMC_TB_STREAMING_TYPE = 25;  // dmc流式模型落盘的工作表
		public static final Integer COMBINE_RELATION_TYPE = 27;
		public static final Integer DMC_DATAHUB_STREAMING_TYPE = 28; // dmc流式落盘的datahub表
		public static final Integer TAG_TYPE = 29; // 标签表
        // 跨域合并类型
        public static final Integer GATHER_TYPE = 5;
        // """数据表状态定义"""
        public static final Integer CREATE_STATUS = 0;
        public static final Integer SYNC_FINISH = 1;
        public static final Integer SYNC_ERROR = 2;
        public static final Integer MERGE_ERROR = 2;
        public static final Integer REPLACE_ERROR = 2;
        public static final Integer SYNCING_STATUS = 3;
        public static final Integer MIGRATE = 4;
        public static final Integer MIGRATE_ERROR = 5;
        public static final Integer QUEUEN_UP = 6;

        public static final Integer TOPIC_NO_MAPPING = 7;   // 主题表未映射
        // LOCK_TEMP = 8  // 临时锁定
        // LOCK_PERMANENT = 9  // 权限收回，永久锁定

        // """分区设置"""
        public static final String PARTITION_BASE_FIELD = "base_field";
        public static final String PARTITION_FORMULA = "formula";
        // 如果设置分区时的版本和工作表的版本一样，说明表还没有更新，此时partition是无效的，查询时不应该引入分区字段
        public static final String PARTITION_SET_VERSION = "set_version";
        public static final String PARTITION_PARAM = "param";
        public static final String PARTITION_READONLY = "readonly";

        // """工作表底层存储类型定义"""
        public static final Integer PARQUET_SOURCE = 0;
        public static final Integer TEXT_SOURCE = 1;

        //	"""表的更新处理模式"""
        public static final String UPDATE_MODE = "update_mode";
        public static final Integer NORMAL_UPDATE_MODE = 0;
        public static final Integer INCREMENT_UPDATE_MODE = 1;
        public static final Integer REALTIME_UPDATE_MODE = 2;

        //	"""commit频次限制"""
        // 基础表数据更新频率限制，-1：不限，null根据默认的限制策略执行, 其他值表示按照固定的时间进行频次限制,单位为分钟
        public static final String COMMIT_LIMIT = "commit_limit";
        public static final Integer NO_COMMIT_LIMIT = -1;
        public static final Integer REALTIME_LIMIT = 1;
        public static final Integer NORMAL_LIMIT = 60;

        // kafka消费相关
        public static final Integer KAFKA_TIME_OUT = 100;
        public static final Integer KAFKA_MAX_POLL_INTERVAL_MS = 300000;
        public static final String KAFKA_SESSION_TIMEOUT_MS = "60000";
        public static final Integer KAFKA_MAX_POLL_RECORDS = 5;
        public static final Integer KAFKA_CONFIG_DEFAULT_USE_COUNT = 1;
        public static final String KAFKA_CONSUMER_USE_SYSTEAM_TIME = "SYS_TIME";
        public static final String STREAM_KAFKA_DATA = "STREAM_KAFKA_DATA";
        public static final Integer HS = 1000;
		public static final Integer REDIS_EXPIRE_TIME = 3600 * 100;


		// """高频表的合表频次限制"""
        public static final Integer VIEW_CREATE_LIMIT = 60;

        // """合表延迟更新相关"""
        public static final Integer DEFAULT_UPDATE = 0;  // 默认更新方式
        public static final Integer DELAY_UPDATE = 7;  // 延迟更新

        // """合表转换字段类型"""
        public static final Integer NEED_FIXED_TYPE = 1;  // 默认需要字段类型转换
        public static final Integer NO_FIXED_TYPE = 0;  // 不需要字段类型转换

        // """工作表通过什么来源创建的, db字段is_etl_tb取值"""
        public static final Integer ETL_TB_FALSE = 0;
        public static final Integer ETL_FROM_DMC = 1;  // dmc－模型分析出的表
        public static final Integer ETL_FROM_MODEL = 2;  // 数据建模

        // """是否在bdp中显示高级合表创建的表"""
        public static final Integer ETL_TB_VISIBLE_TRUE = 1;
        public static final Integer ETL_TB_VISIBLE_FALSE = 0;

        public static final Integer IS_COMBINE = 0;
        public static final Integer IS_NOT_COMBINE = 1;

        public static final Integer STANDARD_MANAGE_TYPE_MOVE = 3;
        public static final Integer RELATION_MANAGE_TYPE_MOVE = 5;
        public static final Integer COMBINE_RELATION_MANAGE_TYPE_MOVE = 6;
        public static final Integer TAG_MANAGE_TYPE_MOVE = 9;
        public static final Integer TOPIC_MANAGE_TYPE = 1 << 2;
        public static final Integer STANDARD_MANAGE_TYPE = 1 << STANDARD_MANAGE_TYPE_MOVE;
        public static final Integer ORIGIN_MANAGE_TYPE = 1;
        public static final Integer MAP_MANAGE_TYPE = 1 << 1;
        public static final Integer RELATION_MANAGE_TYPE = 1 << RELATION_MANAGE_TYPE_MOVE;
        public static final Integer TAG_MANAGE_TYPE = 1 << TAG_MANAGE_TYPE_MOVE; //512
        public static final Integer COMBINE_RELATION_MANAGE_TYPE = 1 << COMBINE_RELATION_MANAGE_TYPE_MOVE;

		public static final Map<Integer, String> TB_MANAGE_TYPE_MAP = new HashMap();

		static {
			TB_MANAGE_TYPE_MAP.put(STANDARD_MANAGE_TYPE, "sdtb");
			TB_MANAGE_TYPE_MAP.put(RELATION_MANAGE_TYPE, "relation");
			TB_MANAGE_TYPE_MAP.put(TAG_MANAGE_TYPE, "tag");
			TB_MANAGE_TYPE_MAP.put(COMBINE_RELATION_MANAGE_TYPE, "relation");
		}

        // 标签表由manage_type字段标记，不走tb_type，所以似乎放进map中不太合适，但又确实需要这个常量
        public static final String TB_TYPE_TAG = "tag";

        public static final Integer TREE_TYPE_MAP = 3;

        public static final Map TB_TYPE_MAP = new HashMap();

        static {
            TB_TYPE_MAP.put(EXCEL_TYPE, "excel");
            TB_TYPE_MAP.put(OPENDS_TYPE, "opends");
            TB_TYPE_MAP.put(DS_TYPE, "ds");
            TB_TYPE_MAP.put(VIEW_TYPE, "view");
            TB_TYPE_MAP.put(BUSINESS_TYPE, "ds");
            TB_TYPE_MAP.put(GRAFT_TYPE, "graft");
            TB_TYPE_MAP.put(HIVE_MAPPING_TYPE, "extrahive");
            TB_TYPE_MAP.put(ODPS_MAPPING_TYPE, "extrahive");
            TB_TYPE_MAP.put(SNAPSHOT_TYPE, "snapshot");
            TB_TYPE_MAP.put(MAPTB_TYPE, "gen_maptb");
            TB_TYPE_MAP.put(TOPIC_TYPE, "topic");
            TB_TYPE_MAP.put(TOPIC_DEF_TYPE, "topicdef");
            TB_TYPE_MAP.put(DATAFLOW_TYPE, "flow");
            TB_TYPE_MAP.put(KAFKA_STREAMING_TYPE, "streaming");
            TB_TYPE_MAP.put(OPENDS_STREAMING_TYPE, "streaming");
            TB_TYPE_MAP.put(MetaConstants.Tb.DMC_TB_COMBINE_RELATION_TYPE, "combine_relation");
            TB_TYPE_MAP.put(MetaConstants.Tb.RELATION_TYPE, "relation");
            TB_TYPE_MAP.put(ELEMENT_TYPE, "element");
        }

        public static final Map DATA_TYPE = new HashMap();

        static {
            DATA_TYPE.put(0, "number");
            DATA_TYPE.put(1, "number");
            DATA_TYPE.put(1 + 1, "string");
            DATA_TYPE.put(1 + 1 + 1, "date");
        }

        public static final String RAW_EXCEL_TB_TYPE = "EXCEL";
		public static final String RAW_TB_TYPE = "RAW";
		public static final String RESULT_TB_TYPE = "RESULT";
		public static final String STANDARD_TB_TYPE = "STANDARD";
		public static final String TOPIC_TB_TYPE = "TOPIC";
		public static final String ELEMENT_TB_TYPE = "ELEMENT";
		public static final String RELATION_TB_TYPE = "RELATION";
		public static final String TAG_TB_TYPE = "TAG";
		public static final String STREAMING_TB_TYPE = "STREAMINGTB";

		public static final Map<Integer, String> FILE_TYPE_MAP = new HashMap<Integer, String>() {{
			put(EXCEL_TYPE, "excel");
			put(OPENDS_TYPE, "opends");
			put(DS_TYPE, "ds");
			put(VIEW_TYPE, "view");
			put(DATAFLOW_TYPE, "flow");
			put(DMC_KAFKA_STREAMING_TYPE, "streamingkafka");
			put(DMC_TB_STREAMING_TYPE, "streamingtb");
			put(KAFKA_STREAMING_TYPE, "streaming");
			put(OPENDS_STREAMING_TYPE, "streaming");
			put(DATAHUB_STREAMING_TYPE, "streaming");
			put(DMC_DATAHUB_STREAMING_TYPE, "streamingdatahub");
		}};
    }

    public static final class FolderConstants {

		public static final String RELATION = "relation";

		public static final String TAG = "tag";

    	public static final String TOPIC = "topic";

		public static final String STANDARD = "standard";

		public static final String ELEMENT = "element";

		public static final String RELATION_ROOT = "relation:folder_root";

		public static final String TAG_ROOT = "tag:folder_root";

		public static final String TOPIC_ROOT = "topic:folder_root";

		public static final String STANDARD_ROOT = "standard:folder_root";

		public static final String ELEMENT_ROOT = "element:folder_root";
	}

    public static final class StreamingConstants {
        private StreamingConstants() {
        }

        // STATUS
        public static final Integer CLOSE = 0;
        public static final Integer START = 1;

        // '''streaming 转态定义'''
        public static final Integer STREAMING_OFF = 0;  //没有运行
        public static final Integer STREAMING_RUNNING = 1;  // 运行中
        public static final Integer STREAMING_ERROR = 2;  // 错误
    }

    public static final class TbPermissionConstants {
        private TbPermissionConstants() {
        }

        public static final Integer ROLE_TYPE_USER = 0;  // 用户
        public static final Integer ROLE_TYPE_GROUP = 1;  // 组
        public static final Integer ROLE_TYPE_PERMISSION = 2;  // 按权限？dmc后没有？ todo
        public static final Integer ROLE_TYPE_ROLEACCOUNT = 3;  // 按角色， 用户中心新版概念20190424
        public static final Integer ROLE_TYPE_CHAT = 4;  // 按临时组， 用户中心新版概念20190424

        public static final Integer PERMISSION_TYPE_ALL = 0;
        public static final Integer PERMISSION_TYPE_MODIFY = 1;
        public static final Integer PERMISSION_TYPE_READ = 2;
        public static final Integer PERMISSION_TYPE_AUTHORIZE = 3;  // 对数据是只读权限，但是拥有可以往下分配的权限
        public static final Integer PERMISSION_TYPE_NO_PERMISSION = 99;

        //data_ptype 数据权限类型 start
        public static final Integer DPTYPE_LOCK_TEMP = 8;  // 数据临时锁定，通过模型更新触发
        public static final Integer DPTYPE_LOCK_PERMANENT = 9;  // 权限收回，永久锁定
        // data_ptype 数据权限类型 end

        public static final Integer FILTER_VALID = 1;
        public static final Integer FILTER_INVALID = 0;
    }


    public static final class TbUpdateLogConstants {
    	public static final Integer USED_TYPE = 1; // 使用记录
		public static final Integer UPDATE_CHANGE_TYPE = 0; // 变更记录


		// op_type
		public static final Integer BASE_INFO_MODIFY = 1;
		public static final Integer BASE_INFO_ADD = 2;
		public static final Integer BASE_INFO_DELETE = 3;

		// type 变更类型
		public static final Integer TB_CHANGE_ALL_TYPE = -1;
		public static final Integer TB_BASE_INFO_CHANGE = 0; // 基础信息
		public static final Integer TB_STRUCT_CHANGE = 1; // 表结构
		public static final Integer TB_UPDATE_MODE_CHANGE = 2;
		public static final Integer TB_PERMISSION_CHANGE = 4;  // 授权信息变更
		public static final Integer TB_MAPPING_TOPIC_CHANGE = 5; // 主题映射
		public static final Integer TB_ELEMENT_FLAG_CHANGE = 6;
		public static final Integer TB_RELATION_CONFIG_CHANGE = 7;
		public static final Integer TB_TAG_CONFIG_CHANGE = 8;
		public static final Integer TB_HIGHT_SETTING_CHANGE = 9;

		// type 使用记录类型
		public static final Integer TB_FLOW_USED = 0;
		public static final Integer TB_CHART_USED = 1;
		public static final Integer OUTPUT_USED = 2;


		public static final Map<Integer, String> ALL_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_BASE_INFO_CHANGE, "基础信息");
			put(TB_STRUCT_CHANGE, "表结构");
			put(TB_PERMISSION_CHANGE, "授权概况");
			put(TB_MAPPING_TOPIC_CHANGE, "主题映射");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
			put(TB_ELEMENT_FLAG_CHANGE, "要素标识");
			put(TB_RELATION_CONFIG_CHANGE, "关系配置");
			put(TB_TAG_CONFIG_CHANGE, "标签配置");
			put(TB_HIGHT_SETTING_CHANGE, "高级设置");
		}};


		public static final Map<Integer, String> RAW_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_BASE_INFO_CHANGE, "基础信息");
			put(TB_STRUCT_CHANGE, "表结构");
			put(TB_HIGHT_SETTING_CHANGE, "高级设置");
		}};

		public static final Map<Integer, String> RESULT_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_BASE_INFO_CHANGE, "基础信息");
			put(TB_STRUCT_CHANGE, "表结构");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
		}};

		public static final Map<Integer, String> STANDARD_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_STRUCT_CHANGE, "表结构");
			put(TB_BASE_INFO_CHANGE, "基础信息");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
			put(TB_PERMISSION_CHANGE, "授权概况");
		}};

		public static final Map<Integer, String> TOPIC_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_MAPPING_TOPIC_CHANGE, "主题映射");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
			put(TB_PERMISSION_CHANGE, "授权概况");
		}};

		public static final Map<Integer, String> ELEMENT_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_BASE_INFO_CHANGE, "基础信息");
			put(TB_ELEMENT_FLAG_CHANGE, "要素标识");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
		}};

		public static final Map<Integer, String> TAG_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_TAG_CONFIG_CHANGE, "标签配置");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
			put(TB_PERMISSION_CHANGE, "授权概况");
		}};

		public static final Map<Integer, String> RELATION_MODIFY_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_RELATION_CONFIG_CHANGE, "关系配置");
			put(TB_UPDATE_MODE_CHANGE, "更新设置");
			put(TB_PERMISSION_CHANGE, "授权概况");
		}};

		/**
		 * 使用日志下拉筛选
		 */
		public static final Map<Integer, String> USED_TYPE_MAP = new HashMap<Integer, String>() {{
			put(TB_CHANGE_ALL_TYPE, "全部类型");
			put(TB_FLOW_USED, "模型使用");
			put(TB_CHART_USED, "图表使用");
			put(OUTPUT_USED, "外部导出使用");
		}};

		public static final Integer FOUR = 4;
		public static final Integer FIVE = 5;
		public static final Integer SIX = 6;
		public static final Integer ONE = 1;
		public static final Integer TWO = 2;
		public static final Integer THREE = 3;
		public static final Integer ZERO = 0;

		public static final String SYNC_DEFAULT_CONFIG = "{\"sync_config\": \"1 * * 11 *\", \"mode\": 3}";


		public static final Map<Integer, String> BASE_INFO_OPERATOR_TYPE = new HashMap();

		static {
			BASE_INFO_OPERATOR_TYPE.put(BASE_INFO_MODIFY, "变更");
			BASE_INFO_OPERATOR_TYPE.put(BASE_INFO_ADD, "新增");
			BASE_INFO_OPERATOR_TYPE.put(BASE_INFO_DELETE, "删除");
		}

		// cron 转意
		public static final Map<String, String> SYNC_MAP = new HashMap<String, String>() {{
			put("*", "每天");
			put("1", "每周一");
			put("2", "每周二");
			put("3", "每周三");
			put("4", "每周四");
			put("5", "每周五");
			put("6", "每周六");
			put("7", "每周天");
		}};

		public static final Map<Integer, String> SYNC_TYPE_MAP = new HashMap<Integer, String>() {{
			put(ZERO, "自定义更新");
			put(ONE, "定时更新");
			put(TWO, "自动更新");
			put(THREE, "暂停更新");
		}};
	}
}
