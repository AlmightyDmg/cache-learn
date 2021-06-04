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
        private String dsName;
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
}
