package com.haizhi.databridge.bean.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 数据库相关
 *
 * @author zhaohuanhuan
 * @create 06 01, 2021
 * @since 1.0.0
 */
public class DataBaseSourceVo {
    @Data
    @Builder
    public static final class CreateVo {

        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("connect_id")
        private String connectId;
    }

    @Data
    @Builder
    public static final class DeleteVo {

        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("tb_count")
        private Integer tbCount;
    }

    @Data
    @Builder
    public static final class UpdateVo {

        @JsonProperty("db_id")
        private String dbId;
    }

    @Data
    @Builder
    public static final class RetrieveVo {

        @JsonProperty("connect_id")
        private String connectId;
        private String name;
        @JsonProperty("table_comments")
        private Integer tableComments;
        @JsonProperty("field_comments")
        private Integer fieldComments;
        private Object labels;
        @JsonProperty("db_id")
        private String dbId;
        @JsonProperty("db_type")
        private String dbType;

    }
}
