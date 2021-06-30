package com.haizhi.databridge.web.controller.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 任务相关参数
 *
 * @author zhaohuanhuan
 * @create 01 27, 2021
 * @since 1.0.0
 */
@Data
public class DataSourceForm {
    @Data
    public static class DataSourceCreateForm {
        private String connectId;
        private String createTables;
        @JsonProperty("ds_name")
        private String dsName;
        @JsonProperty("ds_id")
        private String dsId;
        private String remark;
        private Integer sourceType;
        // upload table comments to bdp
        @JsonProperty("field_comments")
        private Integer fieldComments;
        // upload table comments to bdp
        @JsonProperty("table_comments")
        private Integer tableComments;
        private String dbId;
        private String owner;
        @JsonProperty("real_user")
        private String realUser;
        @JsonProperty("is_dmc")
        private Integer isDmc;
        private Object labels;

    }

    @Data
    public static class DataSourceUpdateForm {
            private String connectId;
            private String dbId;
            private String owner;
    }

    @Data
    public static class DataSourceRetrieveForm {
            private String dbId;
            private String owner;
    }

    @Data
    public static class DataSourceStatusForm {
        @JsonProperty("user_id")
        private String userId;
        private Integer limit;
        private Integer page;
        @JsonProperty("order_by")
        private String orderBy;
        @JsonProperty("source_type")
        private Integer sourceType;
        @JsonProperty("db_id")
        private String dbId;
        private String status;
        @JsonProperty("tb_name")
        private String tbName;
    }
}
