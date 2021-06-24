package com.haizhi.databridge.web.controller.form;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.haizhi.databridge.bean.dto.DataTableDto;

/**
 * 任务相关参数
 *
 * @author zhaohuanhuan
 * @create 01 27, 2021
 * @since 1.0.0
 */
@Data
public class DataTableForm {

    @Data
    public static class DataTableCreateBaseForm {
        @JsonProperty("tb_name")
        private String tbName;
        private String type;
        private String model;
        private String remark;
        private String ref;
    }

    @Data
    public static class DataTableCreateForm extends DataTableCreateBaseForm {
        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("user_id")
        private String userId;
    }

    @Data
    public static class DataTableRetrieveForm {
        @JsonProperty("table_id")
        private String tableId;
        @JsonProperty("user_id")
        private String userId;
    }

    @Data
    public static class DataTableListRetrieveForm {
        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("user_id")
        private String userId;
    }

    @Data
    public static class DataTableUpdateForm extends DataTableUpdateBaseForm {
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("table_id")
        private String tableId;
    }

    @Data
    public static class DataTableUpdateBaseForm {
        @JsonProperty("tb_name")
        private String tbName;
        private String ref;
        private DataTableDto.IncreaseDto increase;
        private Integer rows;
        private Map<Object, Object> blobfield;
        private List<String> keys;
        private List<String> fields;
        @JsonProperty("auto_fields")
        private Integer autoFields;
        private DataTableDto.FilterDto filter;
        private Integer clean;
        private String sql;
        private Integer dereplication;
//        @ApiModelProperty(value = "格式化字段，key是字段名称")
        private Map<String, DataTableDto.FieldDtoatterDto> formatter;
        private String type;
//        @JsonProperty("table_id")
//        private String tableId;
        private List<Map<Object, Object>> transformList;
        private Map<Object, Object> transform;
        private String cleanType;
        private Integer isView;


    }

    @Data
    public static class DataTableDependencyForm {
        @JsonProperty("table_id")
        private String tableId;
        @JsonProperty("user_id")
        private String userId;
    }

    @Data
    public static class DataTableListUpdateForm {
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("db_id")
        private String dbId;
//        @JsonProperty("update_tables")
//        private List<Object> updateTables;
        @JsonProperty("update_tables")
        private List<DataTableUpdateBaseForm> updateTables;
        @JsonProperty("create_tables")
        private List<DataTableCreateBaseForm> createTables;
        @JsonProperty("delete_tables")
        private List<String> deleteTables;
    }


    @Data
    public static class DataTableStatisticsForm {
        @JsonProperty("user_id")
        private String userId;
    }

}
