package com.haizhi.databridge.bean.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchedHisParamVo {
    String toucher = "";
    Integer dync = 0;
    Result result;
    Integer full = 0;
    String trigger = "web";

    @JsonProperty("real_user_id")
    String realUserId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Result {
        Integer total;
        Integer success;
    }
}
