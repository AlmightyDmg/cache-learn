package com.haizhi.databridge.constants;

import lombok.Getter;

import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.web.result.StatusCode;

/**
 * @author WangChengYu
 * @description: 字段枚举汇总
 * @param: ${tags}
 * @return: ${return_type}
 * @throws
 * @date 2020-02-11 16:02
 */
public final class DatabridgeEnums {

    /**
     * @author WangChengYu
     * @description: 库表字段类型枚举
     * @param: ${tags}
     * @return: ${return_type}
     * @throws
     * @date 2020-02-11 16:04
     */
    @Getter
    public enum FieldTypeEnum {
        INT(0, "int"),
        LONG(0, "long"),
        TINYINT(0, "tinyint"),
        SMALLINT(0, "smallint"),
        BIGINT(0, "bigint"),
        DECIMAL(0, "decimal"),
        FLOAT(1, "float"),
        DOUBLE(1, "double"),
        NUMBER(1, "number"),
        STRTING(2, "string"),
        DATETIME(3, "datetime"),
        DATE(3, "date"),
        TIME(3, "time"),
        TIMESTAMP(3, "timestamp");

        //字段类型代码
        private int code;
        //字段类型名称
        private String name;

        FieldTypeEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static FieldTypeEnum getByCode(int code) {
            for (FieldTypeEnum enums : FieldTypeEnum.values()) {
                if (enums.getCode() == code) {
                    return enums;
                }
            }
            throw new DatabridgeException("字段类型不存在，code:" + code);
        }

        public static FieldTypeEnum getByName(String name) {
            for (FieldTypeEnum enums : FieldTypeEnum.values()) {
                if (enums.getName().equals(name)) {
                    return enums;
                }
            }
            throw new DatabridgeException("字段类型不存在，name:" + name);
        }
    }


    /**
     * @author WangChengYu
     * @description: 数据过滤算子WhereType条件的枚举
     * @throws
     * @date 2020-02-13 10:25
     */
    @Getter
    public enum FilterWhereTypeEnum {
        CONDITION("condition", "固定类型条件"),
        SQL("sql", "写sql");

        //类型
        private String type;
        //类型描述
        private String des;

        FilterWhereTypeEnum(String type, String des) {
            this.type = type;
            this.des = des;
        }
    }

    @Getter
    public enum RelationDirectionEnum {
        /*
         * 单向关系
         * */
        SINGLE(1),

        /*
         * 双向关系
         * */
        DOUBLE(2);

        private final Integer code;

        RelationDirectionEnum(Integer code) {
            this.code = code;
        }

        public static RelationDirectionEnum getByCode(Integer code) {
            for (RelationDirectionEnum oneEnum : RelationDirectionEnum.values()) {
                if (oneEnum.getCode().equals(code)) {
                    return oneEnum;
                }
            }

            throw new DatabridgeException(StatusCode.ENUM_NOT_FOUND, "字段类型不存在，code:" + code);
        }
    }

    @Getter
    public enum UsedEnum {
        /*
         * 未被使用
         * */
        UNUSED(0),

        /*
         * 已使用
         * */
        USED(1);

        private final Integer code;

        UsedEnum(Integer code) {
            this.code = code;
        }

        public static UsedEnum getByCode(Integer code) {
            for (UsedEnum oneEnum : UsedEnum.values()) {
                if (oneEnum.getCode().equals(code)) {
                    return oneEnum;
                }
            }

            throw new DatabridgeException(StatusCode.ENUM_NOT_FOUND, "字段类型不存在，code:" + code);
        }
    }

    @Getter
    public enum RelationEnum {
        /*
         * 自定义关系
         * */
        CUSTOMIZE(0),

        /*
         * 内置关系
         * */
        DEFAULT(1);

        private final Integer code;

        RelationEnum(Integer code) {
            this.code = code;
        }

        public static RelationEnum getByCode(Integer code) {
            for (RelationEnum oneEnum : RelationEnum.values()) {
                if (oneEnum.getCode().equals(code)) {
                    return oneEnum;
                }
            }

            throw new DatabridgeException(StatusCode.ENUM_NOT_FOUND, "字段类型不存在，code:" + code);
        }
    }

    @Getter
    public enum RelationInfoEnum {
        /*
         * 同行关系
         * */
        SINGLE(0),

        /*
         * 跨行关系
         * */
        MULTI(1);

        private final Integer code;

        RelationInfoEnum(Integer code) {
            this.code = code;
        }

        public static RelationInfoEnum getByCode(Integer code) {
            for (RelationInfoEnum oneEnum : RelationInfoEnum.values()) {
                if (oneEnum.getCode().equals(code)) {
                    return oneEnum;
                }
            }

            throw new DatabridgeException(StatusCode.ENUM_NOT_FOUND, "字段类型不存在，code:" + code);
        }
    }

    @Getter
	public enum OpNameEnum {
		OP_EQ(0, "相等"),
		OP_NE(1, "不相等"),
		OP_GT(2, "大于"),
		OP_LT(3, "小于"),
		OP_GE(4, "大于等于"),
		OP_LE(5, "小于等于"),
		OP_CONTAINS(6, "包含"),
		OP_NOT_CONTAIN(7, "不包含"),
		OP_IS_EMPTY(8, "为空"),
		OP_IS_NOT_EMPTY(9, "不为空"),
		OP_BETWEEN(10, "between"),
		OP_IN(11, "in"),
		OP_NOT_IN(12, "not in"),
		OP_STARTSWITH(13, "likex"),
		OP_ENDSWITH(14, "like"),

		OP_FIELD_GT(18, "大于"),    // 两个字段比较大于
		OP_FIELD_NE(19, "不相同"),    // 两个字段不相同
		OP_FIELD_EQ(20, "相同"),    // 两个字段相同
		OP_DEVIATION(21, "偏移"), //两个时间段的偏移量小于value值 between %s and %s
		OP_INTERVAL_INTERSEC(22, "区间交集");
		private int code;
		private String name;

		OpNameEnum(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static OpNameEnum getByCode(int code) {
			for (OpNameEnum enums : OpNameEnum.values()) {
				if (enums.getCode() == code) {
					return enums;
				}
			}
			throw new DatabridgeException("sql关键字不存在，code:" + code);
		}
	}

	/**
	 * sql 拼接关键字
	 */
	@Getter
	public enum SqlOpEnum {
		OP_EQ(0, "="),
		OP_NE(1, "<>"),
		OP_GT(2, ">"),
		OP_LT(3, "<"),
		OP_GE(4, ">="),
		OP_LE(5, "<="),
		OP_CONTAINS(6, "like"),
		OP_NOT_CONTAIN(7, "not like"),
		OP_IS_EMPTY(8, "is null"),
		OP_IS_NOT_EMPTY(9, "is not null"),
		OP_BETWEEN(10, "between"),
		OP_IN(11, "in"),
		OP_NOT_IN(12, "not in"),
		OP_STARTSWITH(13, "like"),
		OP_ENDSWITH(14, "like"),

		OP_FIELD_GT(18, ">"),    // 两个字段比较大于
		OP_FIELD_NE(19, "<>"),    // 两个字段不相同
		OP_FIELD_EQ(20, "="),    // 两个字段相同
		OP_DEVIATION(21, "BETWEEN"),    //  两个时间段的偏移量小于value值 between %s and %s
		OP_INTERVAL_INTERSEC(22,
			" (CASE WHEN {2} < {0} AND {3} BETWEEN {0} AND {1} AND SECOND_DIFF({3}, {0}) >= {4} THEN 1 "
				+ " WHEN {2} < {0} AND {3} > {1} AND SECOND_DIFF({1}, {0}) >= {4} THEN 1 "
				+ " WHEN {2} BETWEEN {0} AND {1} AND {3} BETWEEN {0} AND {1} AND SECOND_DIFF({3}, {2}) >= {4} THEN 1 "
				+ " WHEN {2} BETWEEN {0} AND {1} AND {3} > {1} AND SECOND_DIFF({1}, {2}) >= {4} THEN 1 "
				+ "ELSE 0 END ) = 1");    // 两组时间段存在交集，且大于等于某个定值
		// 15, 16, 17, 23 已应用，见com.haizhi.hora.util.GranularityUtils

		private int code;
		private String value;

		SqlOpEnum(int code, String value) {
			this.code = code;
			this.value = value;
		}

		public static SqlOpEnum getByCode(int code) {
			for (SqlOpEnum enums : SqlOpEnum.values()) {
				if (enums.getCode() == code) {
					return enums;
				}
			}
			throw new DatabridgeException("sql关键字不存在，code:" + code);
		}

		public static SqlOpEnum getByValue(String name) {
			for (SqlOpEnum enums : SqlOpEnum.values()) {
				if (enums.getValue().equals(name)) {
					return enums;
				}
			}
			throw new DatabridgeException("sql关键字不存在，name:" + name);
		}
	}


	@Getter
	public enum TassadarFieldTypeEnum {
		INT_TYPE(0, "int"),
		DOUBLE_TYPE(1, "double"),
		STRING_TYPE(2, "string"),
		DATETIME_TYPE(3, "datetime");
		//字段类型代码
		private int code;
		//字段类型名称
		private String name;

		TassadarFieldTypeEnum(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static TassadarFieldTypeEnum getByCode(int code) {
			for (TassadarFieldTypeEnum enums : TassadarFieldTypeEnum.values()) {
				if (enums.getCode() == code) {
					return enums;
				}
			}
			return null;
		}
	}

	@Getter
	public enum DataFlowNodeNameEnum {
		ADD_FIELD("add_field", "添加字段"),
		LEFT_JOIN("left_join", "左连接"),
		JOIN("join", "交集"),
		UNION("union", "全部合并"),
		FULL_UNION("full_join", "去重合并"),
		ANTI_JOIN("anti_join", "差集"),
		ALTER_FIELD("alter_field", "修改字段"),
		DATA_AGGR("data_aggr", "数据聚合"),
		DATA_FILTER("data_filter", "数据过滤"),
		GROUP_FIELD("group_field", "分组字段"),
		SELECT("select", "选择列"),
		ID_TRANS("id_trans", "身份证15转18"),
		MAP_FIELD("map_field", "字典映射"),
		STREAMING_DATA_AGGR("streaming_data_aggr", "流式数据聚合"),
		SQL("sql", "SQL"),
		CUSTOMIZE("customize", "业务算子"),
		TABLE("table", "表结构处理"),
		DEDUPLICATION("deduplication", "数据去重"),
		MACHINE_LEARNING("machine_learning", "机器学习"),
		RELATION_CUS("relation_cus", "关系业务算子"),
		JSON_PARSE("json", "json解析算子"),
		NUMBER_HANDLE("number_handle", "数值处理算子"),
		DATE_HANDLE("date_handle", "日期处理算子"),
		STRING_HANDLE("string_handle", "字符串处理算子");

		// 字段编码
		private String code;

		// 字段名称
		private String name;

		DataFlowNodeNameEnum(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public static DataFlowNodeNameEnum getByCode(String code) {
			for (DataFlowNodeNameEnum enums : DataFlowNodeNameEnum.values()) {
				if (enums.getCode().equals(code)) {
					return enums;
				}
			}
			return null;
		}
	}

	@Getter
	public enum TbTypeEnum {
		EXCEL_TYPE(0, "excel"),
		OPENDS_TYPE(1, "opends"),
		DS_TYPE(2, "opends"),
		VIEW_TYPE(3, "view"),
		BUSINESS_TYPE(6, "ds"),
		GRAFT_TYPE(10, "graft"),
		HIVE_MAPPING_TYPE(11, "extrahive"),
		ODPS_MAPPING_TYPE(12, "extrahive"),
		SNAPSHOT_TYPE(13, "snapshot"),
		MAPTB_TYPE(15, "gen_maptb"),
		TOPIC_TYPE(18, "topic"),
		TOPIC_DEF_TYPE(19, "topicdef"),
		DATAFLOW_TYPE(16, "flow"),
		DMC_KAFKA_STREAMING_TYPE(24, "streamingkafka"),
		DMC_TB_STREAMING_TYPE(25, "streamingtb"),
		KAFKA_STREAMING_TYPE(21, "streaming"),
		OPENDS_STREAMING_TYPE(20, "streaming"),
		DATAHUB_STREAMING_TYPE(23, "streaming");

		//字段类型代码
		private int code;
		//字段类型名称
		private String name;

		TbTypeEnum(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public static TbTypeEnum getByCode(int code) {
			for (TbTypeEnum enums : TbTypeEnum.values()) {
				if (enums.getCode() == code) {
					return enums;
				}
			}
			throw new DatabridgeException("字段类型不存在，code:" + code);
		}
	}
}
