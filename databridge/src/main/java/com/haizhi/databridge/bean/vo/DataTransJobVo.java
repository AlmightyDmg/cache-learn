package com.haizhi.databridge.bean.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;


/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月25日 19:55:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
public class DataTransJobVo {
    String jobType; // [0import, 1export]
    String jobId;
    Integer exportFailureStrategy;
    List<SyncUnit> syncUnits;
    String userId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SyncUnit {
        String taskId;
        Sink fromSink;
        Sink toSink;
        Reader reader;
        Writer writer;
        String userId;
        String errorMsg;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Reader {
        String tableId;
        String tableName;
        String realName;
        List<Column> columns;
        Sync sync;
        Filter filter;
        String connectId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Writer {
        String tableId;
        String tableName;
        String realName;
        String tablePath;
        List<Column> columns;
        String connectId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Column {
        String name;
        String realName;
        String type;
        String realType;
        String value;

        @JsonProperty("uniq_index")
        Boolean uniqIndex = false;
        String remark;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sink {
        String url;
        String username;
        String password;
        String type;
        String subType;
        String schema;
        String catalog;
        String otherConfig;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sync {
        String type; // [overwrite全量更新，increment增量更新]
        Integer isTruncate; // 增量时是否清空
        Integer fetchSize; // 每次获取的条数
        Integer dereplication = 0; // 去重
        CheckRule checkRule;
        SyncCondition syncCondition;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SyncCondition {
            String field;
            String fieldType;
            Conditon start;
            Conditon end;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class Conditon {
                String operator; // 算子，比如 >=, <=
                Integer enable;
                Object value;
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckRule {
        Integer failureStrategy; // [0 否, 1继续导出符合条件的]
        List<String> rules;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Filter {
        FilterCondition filterConditions;
        List<String> sqlConditions;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class FilterCondition {
            String relationType; // [and, or]
            List<Condition> conditions;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class Condition {
                String name;
                String type;
                String value;
            }
        }
    }
}
