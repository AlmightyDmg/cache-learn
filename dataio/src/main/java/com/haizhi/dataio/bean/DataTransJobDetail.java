package com.haizhi.dataio.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月26日 16:01:06
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataTransJobDetail extends JobDetail {
    String taskType; // [0import, 1export]
    String jobId;
    Integer exportFailureStrategy;
    List<SyncUnit> syncUnits;

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
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Reader {
        String tableId;
        String tableName;
        List<Column> columns;
        Sync sync;
        Filter filter;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Writer {
        String tableId;
        String tableName;
        String tablePath;
        List<Column> columns;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Column {
        String name;
        String type;
        String value;
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
        String schema;
        String catalog;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Sync {
        String type; // [overwrite全量更新，increment增量更新]
        Integer isTruncate; // 增量时是否清空
        Integer fetchSize; // 每次获取的条数
        CheckRule checkRule;
        SyncCondition syncCondition;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class SyncCondition {
            String field;
            Conditon start;
            Conditon end;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class Conditon {
                String operator; // 算子，比如 >=, <=
                Integer enable;
                String value;
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
