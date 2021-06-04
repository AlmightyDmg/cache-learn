package com.haizhi.databridge.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;


/**
 * 数据库表字段取值常量
 */
public final class MetaConstants {
    private MetaConstants() {
    }

    // 数据库长度限制
    public static final Integer FIFTY_LENGTH = 50;
    public static final Integer SIXTYFOUR_LENGTH = 64;
    public static final Integer ONETWENTYEIGHT_LENGTH = 128;
    public static final Integer TWOFIFTYSIX_LENGTH = 256;
    public static final Integer FIVEONETWO_LENGTH = 512;
    public static final Integer SIXFIVEFIVETHREEFIVE_LENGTH = 65535;

    /**
     * 工作表权限
     */
    public static final class TbPermission {
        public static final String TB_ID = "tb_id";
        public static final String ROLE_ID = "role_id";
        public static final String ROLE_TYPE = "role_type";
        public static final String PERMISSION_TYPE = "permission_type";
        public static final String COL_FILTER = "col_filter";
        public static final String ROW_FILTER = "row_filter";
        public static final String ROW_FILTER_LIST = "row_filter_list";
        public static final String IS_COL_FILTER = "is_col_filter";
        public static final String PARAM = "param";
        public static final String DATA_COUNT = "data_count";
        public static final String VERSION = "version";
        public static final String CTIME = "ctime";
        public static final String UTIME = "utime";
        public static final String OPERATOR = "operator";
        public static final String PERMISSION_INFO = "permission_info"; // 为权限分配新增字段
        public static final String FID = "fid";
        public static final String METHOD = "method";
        public static final String PERM_INFO_TYPE = "perm_info_type";
        public static final String DATA_PTYPE = "data_ptype";   // 数据权限类型设置

        public static final int ROLE_TYPE_USER = 0;  // 用户
        public static final int ROLE_TYPE_GROUP = 1;  // 组
        public static final int ROLE_TYPE_PERMISSION = 2;  // 按权限？dmc后没有？ todo
        public static final int ROLE_TYPE_ROLEACCOUNT = 3;  // 按角色， 用户中心新版概念20190424
        public static final int ROLE_TYPE_CHAT = 4;  // 按临时组， 用户中心新版概念20190424

        public static final Integer PERMISSION_TYPE_ALL = 0;
        public static final Integer PERMISSION_TYPE_MODIFY = 1;
        public static final Integer PERMISSION_TYPE_READ = 2;
        public static final Integer PERMISSION_TYPE_AUTHORIZE = 3;  // 对数据是只读权限，但是拥有可以往下分配的权限
        public static final Integer PERMISSION_TYPE_NO_PERMISSION = 99;

        // data_ptype 数据权限类型 start
        public static final Integer DPTYPE_NO_LOCK = 0;        // 无数据锁定， 数据库存储为null, ""
        public static final Integer DPTYPE_LOCK_TEMP = 8;  // 数据临时锁定，通过模型更新触发
        public static final Integer DPTYPE_LOCK_PERMANENT = 9;  // 权限收回，永久锁定
        // data_ptype 数据权限类型 end

        public static final Integer FILTER_VALID = 1;
        public static final Integer FILTER_INVALID = 0;

        /**
         * 各种业务表偏移量
         */
        // 原始表
        public static final int MANAGE_TYPE_BIT_OFFSET_ORI = 1;
        // 字典表
        public static final int MANAGE_TYPE_BIT_OFFSET_DICT = 2;
        // 主题表
        public static final int MANAGE_TYPE_BIT_OFFSET_TME = 3;
        // 标准表
        public static final int MANAGE_TYPE_BIT_OFFSET_STD = 4;
        // 关系表
        public static final int MANAGE_TYPE_BIT_OFFSET_REL = 5;

    }

    /**
     * FOLDER_NEW文件夹
     */
    public static final class Folder {
        /**
         * tree_type 取值
         */
        // 工作表原始表
        public static final Integer TREE_TYPE_INIT = 0;
        // 标准表
        public static final Integer TREE_TYPE_STD = 1;
        // 原始字典表 todo 重构新版应该不再有此类型
        public static final Integer TREE_TYPE_OMAP = 2;
        // 标准字典表
        public static final Integer TREE_TYPE_STDMAP = 3;
        //  DMC模型结果表 add at dmc_1.1.1
        public static final Integer TREE_TYPE_DMCTB = 4;
        // 关系表
//        public static final Integer TREE_TYPE_RELATIONTB = 5;	// 新版关系库无此文件夹结构
        // 主题表
        public static final Integer TREE_TYPE_TOPIC = 6;
		// 要素表
		public static final Integer TREE_TYPE_ELEMENT = 7;
        // DMC模型 文件夹关系
        public static final Integer TREE_TYPE_DATAFLOW = 99;

        /**
         * folder_id 根目录
         */
        public static final String FOLDER_ROOT = "folder_root";
    }

    /**
     * 模型
     */
    public static final class DataFlow {
        // id前缀
        public static final String PREFIX_FLOW = "flow";

        // DataFlow表的字段信息
        public static final String NODE_META_PARAM = "node_meta";
        public static final String LINE_META_PARAM = "line_meta";
        public static final String SQL_PARAM = "sql";
        public static final String VALID_PARAM = "valid";
        public static final String CHECK_RESULT_PARAM = "check_result";
        public static final String CODE_PARAM = "code";
        public static final String META_PARAM = "meta";
        public static final String TYPE_PARAM = "type";
        public static final String FIELDS_PARAM = "fields";
        public static final String TB_ID_PARAM = "tb_id";
        public static final String KEY_PARAM = "key";
        public static final String MAPTB_INFO_PARAM = "maptb_info";
        public static final String SDTB_INFO_PARAM = "sdtb_info";
        public static final String IS_MAPTB_PARAM = "is_maptb";
        public static final String IS_SDTB_PARAM = "is_sdtb";
        public static final String MAP_FIELDS_PARAM = "map_fields";
        public static final String TAGTB_INFO_PARAM = "tagtb_info";
        public static final String IS_TAGTB_PARAM = "is_tagtb";
        public static final String STORAGE_PARAM = "storage";
        public static final String OPT_ID_PARAM = "opt_id";
        public static final String WHERE_PARAM = "where";
        public static final String WHERE_TYPE_PARAM = "where_type";
        public static final String PREVIEW_FIELDS_PARAM = "preview_fields";
        public static final String MSG_PARAM = "msg";
        public static final String TABLE_FIELDS_PARAM = "table_fields";
        public static final String NEW_TITLE_PARAM = "new_title";
        public static final String TITLE_PARAM = "title";
        public static final String STATUS_PARAM = "status";
        public static final String DATA_P_STATUS_PARAM = "data_p_status";
        public static final String RESULT_PARAM = "result";
        public static final String TB_COUNT = "tb_count";
        public static final String ORIGIN_TYPE_PARAM = "originType";    // 流式节点判断
        public static final String STREAMING_ID_PARAM = "streaming_id";
        public static final String CONFIG_PARAM = "config";
        public static final String IS_OUTPUT = "is_output";
        public static final String EMPTY = "empty";
        public static final String PARTITION = "partition";
        public static final String PARTITIONMODIFY = "partitionModify";
        public static final String USERBELONG = "userBelong";
        public static final String DATA_COUNT = "data_count";
        public static final String TB_NAME_PARAM = "tb_name";

        // 前端暂存的画面参数-- start --
        public static final String FRONT_POSITION = "position";
        public static final String FRONT_LINE_TYPE = "line_type";
        public static final String FRONT_ZOOM = "zoom";
        public static final String FRONT_IS_TRIGGER_AUTO_LAYOUT = "is_trigger_auto_layout";
        public static final String FRONT_IS_COMMENT = "is_comment";
        // -- end --
        public static final String FOLDER_ID_PARAM = "folder_id";
        public static final String FOLDER_NAME_PARAM = "folder_name";
        public static final String ELEMENT_INFO_PARAM = "element_info";
        public static final String IDENTIFY_FIELDS_PARAM = "identify_fields";
        public static final String SHOW_FIELDS = "show_fields";
        public static final String CATEGORY_ID_PARAM = "category_id";
        public static final String TAG_ID_PARAM = "tag_id";
        public static final String TAG_NAME_PARAM = "tag_name";
        public static final String HAVE_TAG_VALUE_PARAM = "have_tag_value";


        // 变更业务算子和模型的关系的参数
        public static final String MODIFY_PARAM = "modify";
        public static final String DELETE_PARAM = "delete";

        // SourceType
        public static final Integer FROM_DMC = 0;
        public static final Integer FROM_MODEL = 1;
        public static final Integer FROM_TAG = 2;

        // UPDATE_MODE值
        // 暂停更新
        public static final Integer MANUAL = 3;
        // 自动更新
        public static final Integer AUTOMATIC = 2;
        // 定时更新, 相对时间
        public static final Integer T_RELATIVELY = 1;
        // 定时更新, 自定义
        public static final Integer T_CUSTOMIZE = 0;

        public static final Integer REALTIME = 4;
        // DATAFLOW TYPE
        public static final Integer NORMAL_DATAFLOW = 0;
        public static final Integer STREAMING_DATAFLOW = 1;

        // LINE STATUS START
        // 实线
        public static final String LSTATUS_SOLID = "solid";
        // 虚线
        public static final String LSTATUS_DOT = "dot";


        // node type
        public static final Integer STREAMING_NODE = 1;
        public static final Integer NORMAL_NODE = 0;

        // STATUS 取值
        public static final Integer FLOWSTATUS_UPDATING = 3;
        public static final Integer FLOWSTATUS_ERROR = 2;
        public static final Integer FLOWSTATUS_NORMAL = 1;
        public static final Integer FLOWSTATUS_INIT = 0;
    }

    /**
     * 流式
     */
    public static final class Streaming {

        private Streaming() {
        }

        // STATUS
        public static final Integer STREAMING_OFF = 0;  // 停止
        public static final Integer STREAMING_RUNNING = 1;  // 运行中
        public static final Integer STREAMING_ERROR = 2; // 异常

        public static final Integer EXPORT_JOB_OFF = 0;  // 关闭
        public static final Integer EXPORT_JOB_ON = 1;  // 开启

        // 落盘内部表
        public static final String SINK_TO_HDFS = "hdfs";
        public static final String SINK_TO_KAFKA = "kafka";
        public static final String SINK_TO_MYSQL = "mysql";
        public static final String SINK_TO_HTTP = "http";
		public static final String SINK_TO_DATAHUB = "datahub";
		public static final String SINK_TO_RDS_MYSQL = "rds-mysql";
        public static final String SERVERS_PARAM = "servers";

        public static final int SINK_TO_MYSQL_NUM = 0;
        public static final int SINK_TO_HTTP_NUM = 3;
        public static final int SINK_TO_KAFKA_NUM = 4;
		public static final int SINK_TO_DATAHUB_NUM = 5;
		public static final int SINK_TO_RDS_MYSQL_NUM = 6;
		public static final int SINK_TO_HDFS_NUM = 7;
        public static final int SINK_TO_MAX_NUM = 999;
    }


    /**
     * DataFlowNode 相关
     */
    public static class DataFlowNode {

        // 输入表
        public static final String INPUT = "input";

        // 输出表
        public static final String NODE_OUTPUT = "output";

        // 流式输出
        public static final String NODE_STREAMING_OUTPUT = "streaming_output";
        // 业务算子
        public static final String CUSTOMIZE = "customize";
        //字典映射
        public static final String MAP_FIELD = "map_field";
        public static final String MACHINE_LEARNING = "machine_learning";	// 机器学习

        // 身份证15转18
        public static final String ID_TRANS = "id_trans";
        public static final String UNION = "union";
        public static final String JOIN = "join";
        public static final String FULL_JOIN = "full_join";
        public static final String ANTI_JOIN = "anti_join";
        public static final String LEFT_JOIN = "left_join";
        // sql
        public static final String SQL = "sql";

        public static final String PARENTS = "parents";
        public static final String INPUTS = "inputs";
        public static final String OPTTYPE = "opt_type";

        // NODE STATUS START
        public static final Integer NSTATUS_INIT = 0;
        public static final Integer NSTATUS_NORMAL = 1;
        public static final Integer NSTATUS_ERROR = 2;
        public static final Integer NSTATUS_UPDATING = 3;
        // 只有输入、输出节点 关联表被删除的状态
        public static final Integer NSTATUS_VACANCY = 4;
        // 没有表权限， 权限收回
        public static final Integer NSTATUS_NO_TB_PMSN = 10;

        // 只有输入、输出节点 关联表被临时的状态, 新增业务算子锁定状态-dmc_1.1.0
        public static final Integer NSTATUS_LOCK_TMP = TbPermission.DPTYPE_LOCK_TEMP;
        // 只有输入、输出节点 关联表被永久锁定的状态
        public static final Integer NSTATUS_LOCK_PERMANENT = TbPermission.DPTYPE_LOCK_PERMANENT;

        public static final String ORIGINAL_STREAMING_TYPE = "streaming";

        // 和前端交互参数定义
        public static final String FAILMSG = "failmsg";        // 调试模式下节点表错误信息
        public static final String FRAME_STATUS = "frame_status";        // dmc_1.2.7 节点边框线条颜色
        public static final String MARK_COLOR = "markColor";        // 节点标记颜色 added at dmc_1.2.8
    }

    /**
     * EtlFolder 相关
     */
    public static class EtlFolder {

        // tree_type 取值，通过tree_type取值区分不同文件夹展示
        public static final Integer TREE_TYPE_TB = 1;  // 建模模型结果表
        public static final Integer TREE_TYPE_MODEL = 2;  // 建模模型 文件夹关系
        // tree_type 不同类别关联项返回前端key前缀
        public static final String TB_PREFIX = "tb";
        public static final String FLOW_PREFIX = "flow";
    }

    /**
     * AdvViewRelation 相关
     */
    public static class AdvViewRelation {
        // 1:output_tb, 2:base_tb
        public static final Integer RELA_TYPE_OUTPUT = 1;  // 查询时，in 需要str, 数据库仍是int
        public static final Integer RELA_TYPE_BASE = 2;
        public static final Integer RELA_TYPE_TMP = 3;
    }

    public static class MachineLearning {
        // 算法定义
        public static final Integer LINEAR_REGRESSION = 1;
        public static final Integer KMEANS = 2;
        public static final Integer DECISION_TREE = 3;
        public static final Integer DECISION_TREE_REGRESSION = 4;
        public static final Integer GMM = 5;
        public static final Integer LOGISTIC_REGRESSION = 6;
        public static final Integer MPC = 7;
        public static final Integer NAIVE_BAYES = 8;
        public static final Integer RANDOM_FOREST_CLASSIFIER = 9;
        public static final Integer RANDOM_FOREST_REGRESSION = 10;
        public static final Integer SVM = 11;
        public static final Integer FP_GROWTH = 12;

        // 截取字符串开始的下标
        public static final Integer INDEX_O = 3;
        public static final Integer INDEX_T = 16;
        public static final Integer INDEX_S = 8;

        // 训练状态
        public static final Integer NORMAL = 0;
        public static final Integer QUEUE = 1;
        public static final Integer EXECUTE = 2;
        public static final Integer ERROR = 3;
        public static final Integer MODIFY = 4;
        public static final Integer CANCEL = 5;

        // 机器学习参数开发功能兼容老数据，为老模型补充默认参数
        public static final Integer REG_PARAM = 0;
        public static final Integer MAX_ITER = 100;
        public static final Integer SEED = 1234;
        public static final Double TOL_O = 0.0001;
        public static final Integer MAX_DEPTH = 5;
        public static final Integer MIN_INFO_GAIN = 0;
        public static final Integer MIN_INSTANCES_PER_NODE = 1;
        public static final Double ELASTIC_NET_PARAM = 0.0;
        public static final Double THRESHOLD = 0.5;
        public static final Double TOL_T = 0.1;

        // 模型类型
        public static final Integer FORECAST = 1;
        public static final Integer CLUSTER = 2;
        public static final Integer CLASSIFY = 3;


    }

    public static class Field {
        // 字段类型定义(修改后注意调整to_mobius_schema的mobius_type_map的匹配关系)
        public static final int INT_TYPE = 0;
        public static final int DOUBLE_TYPE = 1;
        public static final int STRING_TYPE = 2;
        public static final int DATETIME_TYPE = 3;
		public static final int BLOB_TYPE = 4;
        public static final int NULL_DATA_TYPE = 999;

//		// 用于匹配字段id的正则表达式
//		public static final String FIELD_PATTERN = "fk[a-f0-9]{8}";
//		// 用于匹配只有字段id的正则表达式
//		public static final String ONLY_FIELD_PATTERN = "fk[a-f0-9]{8}$";

        public static final Map<String, Integer> TYPE_MAPPER = new HashMap<String, Integer>() {{
            put("datetime", DATETIME_TYPE);
            put("date", DATETIME_TYPE);
            put("time", DATETIME_TYPE);
            put("timestamp", DATETIME_TYPE);
            put("int", INT_TYPE);
            put("long", INT_TYPE);
            put("tinyint", INT_TYPE);
            put("smallint", INT_TYPE);
            put("bigint", INT_TYPE);
            put("float", DOUBLE_TYPE);
            put("double", DOUBLE_TYPE);
            put("decimal", INT_TYPE);
            put("string", STRING_TYPE);
            put("number", DOUBLE_TYPE);
        }};

		// 前端展示转换, 数据预览
		public static final Map<Integer, String> FIELD_TYPE_NAME_MAP = new HashMap<Integer, String>() {{
			put(INT_TYPE, "number");
			put(DOUBLE_TYPE, "number");
			put(STRING_TYPE, "string");
			put(DATETIME_TYPE, "date");
			put(BLOB_TYPE, "blob");
		}};

        // 匹配字段id的正则表达式
        public static final Pattern FIELD_PATTERN = Pattern.compile("fk[a-f0-9]{8}");
        // 用于匹配只有字段id的正则
        public static final Pattern ONLY_FIELD_PATTERN = Pattern.compile("fk[a-f0-9]{8}$");

        public static final List<Integer> AS_DOUBLETYPE_LIST = Arrays.asList(INT_TYPE, DOUBLE_TYPE);
    }

    /**
     * 计算字段
     */
    public static class VField {
        public static final Integer CHART_VFIELD_FLAG = 0;  // 用于作图的计算字段
        public static final Integer TABLE_VFIELD_FLAG = 1;  // 用于工作表的计算字段

        // 字段是否需要物化落盘
        public static final Integer CAN_CONCRETE = 1;
        public static final Integer CAN_NOT_CONCRETE = 0;
        public static final Integer FORCE_CONCRETE = 2;    // 表示字段需要强制物化，用于高性能表中做分区的计算字段

        public static final Integer MODIFIED = 1;
        public static final Integer UNMODIFIED = 0;
    }

    /**
     * 工作表
     */
    public static class Tb {
        // id前缀
        public static final String PREFIX_TB = "tb";

        public static final String TB_ID = "tb_id";
        public static final String NAME = "name";
        public static final String TITLE = "title";
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
        public static final Integer DATAHUB_STREAMING_TYPE = 23;  // 对接用户datahub流式表
        public static final Integer DMC_KAFKA_STREAMING_TYPE = 24;  // dmc流式模型落盘的kafka表
        public static final Integer DMC_TB_STREAMING_TYPE = 25;  // dmc流式模型落盘的工作表
        public static final Integer RELATION_TYPE = 26;  // 关系表
        public static final Integer DMC_TB_COMBINE_RELATION_TYPE = 27;  // dmc聚合关系表落盘的工作表
		public static final Integer DMC_DATAHUB_STREAMING_TYPE = 28;     // dmc流式落盘的datahub表

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

		// 标签表暂停更新，同步到TB状态
		public static final Integer SYNC_SUSPEND = 20;

		// """分区设置"""
        public static final String PARTITION_BASE_FIELD = "base_field";
        public static final String PARTITION_FORMULA = "formula";
        // 如果设置分区时的版本和工作表的版本一样，说明表还没有更新，此时partition是无效的，查询时不应该引入分区字段
        public static final String PARTITION_SET_VERSION = "set_version";
        public static final String PARTITION_PARAM = "param";
        public static final String PARTITION_PARAM_TYPE = "type";
        public static final String PARTITION_PARAM_OPTION = "option";
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
		private static final Integer ELEMENT_MANAGE_TYPE_MOVE = 7;
        public static final Integer ELEMENT_MANAGE_TYPE = 1 << ELEMENT_MANAGE_TYPE_MOVE;


        public static final Integer TREE_TYPE_MAP = 3;

        // 限制计算字段长度
        public static final Integer CHART_VFIELD_LIMIT = 3000;
        public static final Integer TABLE_VFIELD_LIMIT = 3000;

        // 表的类型TB_TYPE 给前端展现用	common/tassadar/meta/tb.py:712
		public static final String FRONT_RELATION_TYPE = "relation";
		public static final String FRONT_RELATION_COMBINE_TYPE = "combine_relation";
    }

    public static final class Gen {
        @Getter
        public enum GenType {
            JOIN(0),
            AGGREGATOR(1),
            UNION(2),
            SCRIPT(3),
            EXTRACT(4),
            ETL(5),
            SHADOW(6),
            TRANSPOSE(7),
            ADS(15),
            TOPIC(20),
            RELATION(21),
            ELEMENT(22),
			OPERATOR(23),
			TAG(25),
			RELATION_UNION(27);

            private int code;

            GenType(int code) {
                this.code = code;
            }

            public boolean equals(int genType) {
                return this.code == genType;
            }
        }
    }

    /**
     * 业务算子
     */
    public static class OPT {
        public static final Integer STATUS_OFFLINE = 0;    // 待使用
        public static final Integer STATUS_ONLINE = 1;    // 使用中
        public static final Integer STATUS_BAN = 2;    // 禁用
        public static final Integer STATUS_TEST = 3;    // 测试中

        public static final Integer DEFAULT_ICON = 0;    // 默认图标，存于前端
        public static final Integer UPLOAD_ICON = 1;    // 用户上传，存于minio云
    }


	public static class TASK {
		public static final int ALL_UPDATE = 0;    // 全量更新
		public static final int APPEND_UPDATE = 1;    // 追加数据
		public static final int APPEND_BATCH_UPDATE = 2;    // 批量追加
		public static final int REPLACE_UPDATE = 3;    // 替换部分
		public static final int ALL_REPLACE_UPDATE = 4;    // 替换全部
		public static final int ALL_SCHEDULER = 5;    // 替换全部

		// type
		public static final int TASK_TYPE_GEN = 1;
		public static final int TASK_TYPE_TAG = 50;
		public static final int TASK_TYPE_DATA_FLOW = 51;
		public static final int TASK_TYPE_MAPPING_VIEW = 52;
		public static final int TASK_TYPE_GIS_TB_SYNC = 53;
		public static final int TASK_TYPE_JOB = 54;
		public static final int TASK_TYPE_STREAMING = 55;
	}

	public static class EXCEL {
		public static final int EXCEL_CREATE = 0;    // 创建
		public static final int EXCEL_ALL = 1;    // 追加数据
		public static final int EXCEL_APPEND = 2;    // 追加数据
		public static final int EXCEL_BATCH_APPEND = 7;    // 追加数据
		public static final int EXCEL_ALL_REPLACE = 3;    // 替换全部
		public static final int EXCEL_DELETE = 4;    // 删除单个excel
		public static final int EXCEL_REPLACE = 5;    // 替换部分
		public static final int EXCEL_REBUILD = 6;    // 重新构建
	}

    public static class ViewScheduler {
        // owner_type 取值
        public static final Integer NORMAL_TYPE = 2;    // 其他用
        public static final Integer FLOW_TYPE = 5;    // 模型用

		// 任务更新类型
        public static final Integer SCEDULER_TYPE = 2;
        public static final Integer USER_TYPE = 1;
        public static final Integer SYSTEM_TYPE = 3;
        public static final Integer ALL_TYPE = 0;

		public static final Integer JOB_TYPE = 7;    // 导出任务用
    }

    /**
     * 字典表和结果表表及模型的依赖
     */
    public static class DataflowRelation {
        // type 取值
        public static final Integer MAPTB_TYPE = 0;    // 字典表

    }

    /**
     * 规则相关  TagNewRule
     */
    public static class RuleAssign {

        public static final Integer RULE_TYPE_TB = 0;
        public static final Integer RULE_TYPE_OPT = 1;

        public static final Integer RULE_FILTER_NEW = 0;
        public static final Integer RULE_FILTER_EXP = 1;

        public static final Integer RULE_ASSIGN_TARGET_GROUP = 0;
        public static final Integer RULE_ASSIGN_TARGET_USER = 1;
        public static final Integer RULE_ASSIGN_TARGET_ROLE = 2;
        public static final Integer RULE_ASSIGN_TARGET_CHAT = 3;

        public static final Integer RULE_ASSIGN_OP_GE = 4;
        public static final Integer RULE_ASSIGN_OP_LE = 5;
        public static final Integer RULE_ASSIGN_DATE_RANGE = 10;

//        public static final Integer RULE_ASSIGN_RELATION_TB = 5;
//        public static final Integer RULE_ASSIGN_RELATIONDEF_TB = 31;

        public static final Integer RULE_TB_NAME = 0;		// 数据表名称
        public static final Integer RULE_TB_FIELD_NAME = 1;		// 数据表字段
        public static final Integer RULE_DICT = 2;		// 字典项
        public static final Integer RULE_FLOW = 3;		//	来源模型

		public static final String FLOW_RULE_TYPE_STR = "flow"; // rule_type 为3时返回前端的字段信息
    }

    /**
     * 算子权限
     */
    public static final class OptPermission {
        public static final Integer ROLE_TYPE_USER = 0;
        public static final Integer ROLE_TYPE_GROUP = 1;
        public static final Integer ROLE_TYPE_ROLE = 2;
        public static final Integer ROLE_TYPE_CHAT = 3;
    }

    /**
     * 算子管理表
     */
    public static class OptParamConf {
        // USE_TYPE 参数用法类型
        public static final Integer NORMAL_USE_TYPE = 0;
        public static final Integer ADVANCED_USE_TYPE = 1;

        // PARAM_TYPE 类型
        public static final Integer PARAM_TYPE_NUMBER = 1;
        public static final Integer PARAM_TYPE_STRING = 2;
        public static final Integer PARAM_TYPE_DATETIME = 3;
        public static final Integer PARAM_TYPE_ENUM = 4;

        // 日期类型:1:相对时间，2:绝对时间，3:相对当前时间
        public static final Integer ABSOLUTE_TIME = 15; // 某年某月 某时某分某秒
        public static final Integer RELATIVE_CURRENT_TIME_POINT = 16;  // 今天，最近三天，最近一个月
        public static final Integer RELATIVE_CURRENT_TIME = 17;  // 周一某时某刻某分

        public static final Integer EVERYDAY = 0; //  每天
    }

	/**
	 * 要素相关
	 */
	public static final class ElementConfig {
		public static final String CONFIG_ID = "conf";
	}

	public static class Job {
		public static final Integer AUTO_EXECUTE = 0;
		public static final Integer TIMING_EXECUTE = 1;

		public static final Integer JOB_SOURCE_FROM_OP = 0;
		public static final Integer JOB_SOURCE_FROM_DMC = 1;

	}

    /**
     * 标签相关
     */
    public static class TagNew {
        public static final Integer TAG_TYPE = 25;
        public static final Integer TAG_VALUE_COUNT = 100;
        public static final Integer TAG_VALUE_INDEX = 3;
        //标签算子 字段
		public static final String TAG_VALUE = "标签值";

		public static final Integer TAG_VALUE_ATTRIBUTE = 0;	// 属性类
		public static final Integer TAG_VALUE_STATISTIC = 1;	// 统计类
    }

    /** 关系表 */
	public static final class TbRelation {
		// 关系数据类型
		public static final Integer TB_RELATION_TYPE = 0;	// 普通数据关系
		public static final Integer TB_COMBINE_RELATION_TYPE = 1;	// 聚合关系

		public static final String FROM_ENTITY_ID = "from_entity_id";
		public static final String TO_ENTITY_ID = "to_entity_id";

		public static final String FIRST_FOLDER_FORMAT = "{0}-{1}";
		public static final String SECOND_FOLDER_FORMAT = "{0}-{1}-{2}";


		public static final Map<Integer, String> RELATION_TYPE_MAP = new HashMap<>();
		static {
			RELATION_TYPE_MAP.put(TB_RELATION_TYPE, "数据关系");
			RELATION_TYPE_MAP.put(TB_COMBINE_RELATION_TYPE, "聚合关系");
		}
	}

	/**
	 * 数据源类型管理
	 */
	public static class DsType {
		// 数据源类型
		public static final Integer MYSQL = 1;
		public static final Integer ORACLE = 2;
		public static final Integer POSTGRESQL = 50;
		public static final Integer ARANGO = 112;
		public static final Integer ELASTICSEARCH = 113;
		public static final Integer KAFKA = 114;
		public static final Integer DATAHUB = 115;
		public static final Integer GREENPLUM = 116;
	}
	/*
	 * 血缘相关
	 */
	public static final class Lineage {
		// 血缘信息，表类型
		public static final String RAW_TB_TYPE = "RAW";
		public static final String RESULT_TB_TYPE = "RESULT";
		public static final String STANDARD_TB_TYPE = "STANDARD";
		public static final String TOPIC_TB_TYPE = "TOPIC";
		public static final String ELEMENT_TB_TYPE = "ELEMENT";
		public static final String RELATION_TB_TYPE = "RELATION";
		public static final String TAG_TB_TYPE = "TAG";
		public static final String STREAMING_TB_TYPE = "STREAMINGTB";
		// 血缘信息，节点间链接类别
		public static final String LINEAGE_TYPE_PUSH = "PUSH";
		public static final String LINEAGE_TYPE_MODEL = "MODEL";
		public static final String LINEAGE_TYPE_MAP = "MAP";
		public static final String LINEAGE_TYPE_TAG = "TAG";
		public static final String LINEAGE_TYPE_RULE = "RULE";

		private static final Integer LINEAGE_TYPE_PUSH_CODE = 0;
		public static final Integer LINEAGE_TYPE_RULE_CODE = 1;
		private static final Integer LINEAGE_TYPE_MODEL_CODE = 2;
		private static final Integer LINEAGE_TYPE_MAP_CODE = 3;
		private static final Integer LINEAGE_TYPE_TAG_CODE = 4;

		public static final Integer NODE_POSITION_UP = 0;
		public static final Integer NODE_POSITION_SELF = 1;
		public static final Integer NODE_POSITION_DOWN = 2;

		public static final Integer DS_TYPE_MYSQL = 1;
		public static final Integer DS_TYPE_ORACLE = 2;
		public static final Integer DS_TYPE_SQLSERVER = 3;
		public static final Integer DS_TYPE_OPENDS = 4;

		public static final Map<Integer, String> MANAGE_TYPE_TO_TB_TYPE_MAP = new ImmutableMap.Builder<Integer, String>()
			.put(Tb.TOPIC_MANAGE_TYPE, TOPIC_TB_TYPE)
			.put(Tb.RELATION_MANAGE_TYPE, RELATION_TB_TYPE)
			.put(Tb.COMBINE_RELATION_MANAGE_TYPE, RELATION_TB_TYPE)
			.put(Tb.ELEMENT_MANAGE_TYPE, ELEMENT_TB_TYPE)
			.put(Tb.TAG_MANAGE_TYPE, TAG_TB_TYPE).build();

		public static final Map<Integer, String> LINEAGE_MAP = new ImmutableMap.Builder<Integer, String>()
			.put(LINEAGE_TYPE_PUSH_CODE, LINEAGE_TYPE_PUSH)
			.put(LINEAGE_TYPE_RULE_CODE, LINEAGE_TYPE_RULE)
			.put(LINEAGE_TYPE_MODEL_CODE, LINEAGE_TYPE_MODEL)
			.put(LINEAGE_TYPE_MAP_CODE, LINEAGE_TYPE_MAP)
			.put(LINEAGE_TYPE_TAG_CODE, LINEAGE_TYPE_TAG).build();

		public static final Map<String, String> TB_NAME_ROUTE_MAP = new ImmutableMap.Builder<String, String>()
			.put(RAW_TB_TYPE, "原始库配置")
			.put(RESULT_TB_TYPE, "原始库配置")
			.put(STANDARD_TB_TYPE, "标准库配置")
			.put(TOPIC_TB_TYPE, "主题库配置")
			.put(ELEMENT_TB_TYPE, "要素管理")
			.put(TAG_TB_TYPE, "标签管理")
			.put(RELATION_TB_TYPE, "关系管理")
			.build();
	}

	/** 数据源表 **/
	public static final class Ds {
		// 数据源类型定义
		public static final Integer ML_TYPE = 1;
		public static final Integer ORACLE_TYPE = 2;
		public static final Integer SQLSERVER_TYPE = 3;
		public static final Integer OPEN_DS_TYPE = 4;
		public static final Integer BAIDU_TUIGUANG_TYPE = 5;
		public static final Integer TALKINGDATA_TYPE = 6;
		public static final Integer SOGOU_TYPE = 7;
		public static final Integer PUBLIC_DATA_TYPE = 8;
		public static final Integer BAIDU_STATISTIC_TYPE = 9;
		public static final Integer UMENG_TYPE = 10;
		public static final Integer QIHU_TYPE = 11;
		public static final Integer SHENMA_TYPE = 12;
		public static final Integer FSKF_TYPE = 15;
		public static final Integer KST_TYPE = 16;
		public static final Integer BAIDU_BRIDGE_TYPE = 17;
		public static final Integer BAIDU_REALTIME_TYPE = 18;
		public static final Integer JINYIWEI_TYPE = 19;
		public static final Integer TQ_KEFU_TYPE = 20;
		public static final Integer BAIDU_WANGMENG_TYPE = 21;
		public static final Integer CNZZ_TYPE = 24;
		public static final Integer LIVE_800_TYPE = 23;
		public static final Integer SEM_TYPE = 22;
		public static final Integer STATISTIC400_TYPE = 26;
		public static final Integer HEADLINE_TYPE = 25;
		public static final Integer YOUDAO_TYPE = 27;
		public static final Integer CXT_TYPE = 28;
		public static final Integer SWT_TYPE = 32;
		public static final Integer SINA_TYPE = 31;
		public static final Integer WISDOM_PUSH_TYPE = 29;
		public static final Integer SOGOU_WM_TYPE = 30;
		public static final Integer SINA_FUYI_TYPE = 33;
		public static final Integer JIN_DATA_TYPE = 34;
		public static final Integer EC_SOFT_TYPE = 35;
		public static final Integer TENCENT_GDT_TYPE = 36;
		public static final Integer YOUKU_TYPE = 37;
		public static final Integer MINGDAO_TYPE = 38;
		public static final Integer SGSEARCH_TYPE = 39;
		public static final Integer PARTNER_CLOUD_TYPE = 40;
		public static final Integer SYCM_TYPE = 41;
		public static final Integer YOUDAOZHIXUAN_TYPE = 42;
		public static final Integer SGWMENG_TYPE = 44;
		public static final Integer BAIDU_SMALL_TYPE = 43;
		public static final Integer QIHU_DIANJING_TYPE = 45;
		public static final Integer GOOGLE_ANALYTICS_TYPE = 48;
		public static final Integer POSTGRESQL_TYPE = 50;
		public static final Integer TONGBUBAO = 52;
		public static final Integer NOAH = 54;
		public static final Integer BAIDU_INDEX_NUM_TYPE = 53;
		public static final Integer GROWING_IO_TYPE = 55;
		public static final Integer GUANYI_TYPE = 56;
		public static final Integer WEIXIN_SHOP_TYPE = 57;
		public static final Integer ZHIFUBAO_TYPE = 58;
		public static final Integer JOURNAL_TYPE = 59;
		public static final Integer ZFBQY_TYPE = 61;
		public static final Integer MECHAT_TYPE = 60;
		public static final Integer MTSJ_TYPE = 62;
		public static final Integer SQ2016_TYPE = 63;
		public static final Integer LEYU_TYPE = 65;
		public static final Integer WECHAT_INDEX_TYPE = 67;
		public static final Integer TOUTIAO_INDEX_TYPE = 68;
		public static final Integer WX_PUBLIC_TYPE = 66;
		public static final Integer A_BIT_COUNSEL_TYPE = 69;
		public static final Integer UC_INFORMATION_TYPE = 71;
		public static final Integer WEB_INDEX_TYPE = 70;
		public static final Integer ELE_ME_TYPE = 72;
		public static final Integer BAIDU_INFORMATION_TYPE = 73;
		public static final Integer WXSOFTWARE_TYPE = 74;
		public static final Integer CREATE_DATA_TYPE = 75;
		public static final Integer DDQY_TYPE = 76;
		public static final Integer WANGYI_NEWS_TYPE = 78;
		public static final Integer QIHU_MOBILE_TYPE = 82;
		public static final Integer CTRIP_TYPE = 79;
		public static final Integer MICRO_LIFE_TYPE = 81;
		public static final Integer FORM_BOSS_TYPE = 80;
		public static final Integer CJFST_TYPE = 83;
		public static final Integer RDS_TYPE = 85;
		public static final Integer JIN_ENT_TYPE = 86;
		public static final Integer BZY_TYPE = 87;
		public static final Integer MYSQL_GRAFT_TYPE = 88;
		public static final Integer KS_INFO_FLOW_TYPE = 89;
		public static final Integer XM_INFO_FLOW_TYPE = 100;
		public static final Integer SQLSERVER_GRAFT_TYPE = 101;
		public static final Integer ORACLE_GRAFT_TYPE = 102;
		public static final Integer POSTGRESQL_GRAFT_TYPE = 103;
		public static final Integer HIVE_GRAFT_TYPE = 104;
		public static final Integer TENTEC_TYPE = 105;
		public static final Integer MOMO_EXTENSION_TYPE = 111;
		public static final Integer ANALYTICDB_GRAFT_TYPE = 108;
	}

	public static final class TbSnapshot {
		// 表id前缀
		public static final String PREFIX_SNAPSHOT = "ST";

		public static final String VERSION_NAME = "V";

	}


	/**
	 * 数据资产表
	 */
	public static final class AssetTb {
		// publish_status
		public static final Integer PUBLISHED_STATUS = 1;
		public static final Integer NOT_PUBLISH_STATUS = 0;


		public static final Integer TODAY_PUBLISH = 1;
		public static final Integer TODAY_DOWN = 0;
	}
}
