package com.haizhi.databridge.bean.vo;

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
public class FlinkCheckPointVo {

    private LatestVo latest;

    @Data
    public static class LatestVo {
        private String completed;
        private String savepoint;
        private String failed;
        private RestoredVo restored;
    }

    @Data
    public static class RestoredVo {
        private Integer id;
        @JsonProperty("restore_timestamp")
        private Integer restoreTimestamp;
        @JsonProperty("is_savepoint")
        private boolean isSavepoint;
        @JsonProperty("external_path")
        private String externalPath;
    }
}
