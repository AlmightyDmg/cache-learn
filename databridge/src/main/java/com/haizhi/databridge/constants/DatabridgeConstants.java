package com.haizhi.databridge.constants;

public final class DatabridgeConstants {
    private DatabridgeConstants() {
    }

    public static final String INNER_CALL = "inner_call";
    public static final String SESSION_ID = "session_id";
    public static final String ENTERPRISE_ID = "enterprise_id";
    public static final String USER_BELONG = "user_belong";
    public static final String DMC_ENT_ID = "dmc_ent_id";
    public static final String USER_ID = "user_id";
    public static final String TRACE_ID = "trace_id";
    public static final String DMC_REQUEST = "dmc_request";
    // 来自DMC请求
    public static final String DMC_REQUEST_DMC = "1";
    // 来自非DMC请求
    public static final String DMC_REQUEST_NOT_DMC = "0";
    public static final String ENTERPRISE_USER = "domain";

    public static final String REQUEST_URI = "request_uri";

    public static final String ENGINE_ALIYUN = "aliyun";
    public static final String ENGINE_SPARK = "spark";
    
    public static final String FOLDER_ROOT_ID = "folder_root";
    public static final String FOLDER_ROOT_NAME = "根目录";

    //自定义接口请求成功的状态码
    public static final int SUC_STATUS = 0;

    //sql字段拼接的间隔符
    public static final String SQL_DELIMITER = ",";

    public static final Integer TBRELATION_RECUSION_LIMIT = 3000;    // relation循环查询时的循环上线
    // 查询FIELD表默认条数
    public static final Integer FIELD_QUERYNUM = 10000;
    // 状态
    public static final String STATUS = "status";

    // 用户角色
    public static final String ROLE = "role";

    public static final double EXEC_PROGRESS = 20;

    public static final double TASK_PERCENT = 80;

    //当前模型进度
    public static final String MODEL_PROGRESS = "hora:model:progress";

    // 关系
    public static final String RELATION_TYPE_PREFIX = "RT";

    // Integer 常量 0
    public static final Integer INTEGER_0 = 0;
	// Long 常量 0
    public static final Long LONG_0 = 0L;
    // 主题表标识
    public static final String TOPIC = "topic";
    // 标准表标识
    public static final String STANDARD = "standard";
    // 关系表标识
    public static final String RELATION_DATA_TB = "Relation";
    //聚合关系表标识
    public static final String RELATION_COMBINE_DATA_TB = "CombineRelation";
    // 标准表根目录标识
    public static final String STANDARD_FOLDER_ROOT = "standard_folder_list";
    // 主题表根目录标识
    public static final String TOPIC_FOLDER_ROOT = "topic_folder_list";

    public static final String DEFAULT_BELONG_FIELD_ID = "all";

    public static final String DEFAULT_BELONG_FIELD_VALUE = "全部";

	public static final int RANDOM_PARAM = 10;

	public static final int MINUTE_TIME = 60;

	public static final int M_PER_S = 1000;

	public static final Long ONEDAYTIMESTAMP = 86400 * 1000L;
	// 报错信息重写fe的返回
	public static final String ERR_OVERWRITE = "OVERWRITE";

	// 模型处理流程中常量定义 com.haizhi.hora.dataflow.Dataflow
	public static final class DataFlowMain {
		public static final String OUTPUT_DEBUG_MODE = "debugMode";
		public static final String OUTPUT_NORMAL_MODE = "normalMode";
		public static final String OUTPUT_TAG_MODE = "tagMode";
	}


    public static final int EXPORT_STATUS_CREATE = 0;
    public static final int EXPORT_STATUS_SYNC = 1;
    public static final int EXPORT_STATUS_NORMAL = 2;
    public static final int EXPORT_STATUS_ERROR = 3;
    public static final int EXPORT_STATUS_QUEUE = 4;
    public static final int EXPORT_STATUS_STOP = 5;
}
