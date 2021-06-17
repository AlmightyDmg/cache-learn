package com.haizhi.databridge.bean.vo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import com.haizhi.databridge.bean.dto.DataTableDto;

/**
 * 数据库相关
 *
 * @author zhaohuanhuan
 * @create 06 01, 2021
 * @since 1.0.0
 */
public class DataTableVo {
    @Data
    @Builder
    public static final class CreateVo {

        @JsonProperty("table_id")
        private String tableId;
        private String name;
    }

    @Data
    @Builder
    public static final class RetrieveVo {

        @JsonProperty("table_id")
        private String tableId;
        @JsonProperty("tb_name")
        private String tbName;
        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("scheduler_id")
        private String schedulerId;
        private Boolean synced;
        private DataTableDto.FilterDto filter;
        private Object blobfield;
        private List<String> keys;
        private List<String> fields;
        private DataTableDto.IncreaseDto increase;
        private String ref;
        @JsonProperty("output_ref")
        private String outputRef;
        private Integer dereplication;
        private Integer clean;
        private List<String> schema;
        private String sql;
        private Integer rows;
        private String model;
        private Map<String, DataTableDto.FieldDtoatterDto> formatter;
        private String type;
        @JsonProperty("is_view")
        private Integer isView;
        @JsonProperty("auto_fields")
        private Integer autoFields;
        private Map<Object, Object> transform;
    }

    @Data
    @Builder
    public static final class StatisticsVo {
        private Integer finished;
        private Integer syncing;
        private Integer error;
        private Integer terminated;
        private Integer total;
        @JsonProperty("total_database")
        private Integer totalDatabase;
        @JsonProperty("total_scheduler")
        private Integer totalScheduler;
    }

    @Data
    @Builder
    public static final class TableVo {

        @JsonProperty("connect_id")
        private String connectId;
        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("db_type")
        private String dbType;
        @JsonProperty("ds_name")
        private String dsName;
        private String exception;
        private Integer fetched;
        private Integer posted;
        private String ref;
        private String remark;
        private List<String> schema;
        @JsonProperty("start_at")
        private String startAt;
        private String status;
        @JsonProperty("sync_type")
        private String syncType;
        @JsonProperty("table_id")
        private String tableId;
        @JsonProperty("tb_name")
        private String tbName;

    }

}
