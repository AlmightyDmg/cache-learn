package com.haizhi.dataclient.connection.dmc.client.noah.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartResp {
    private String status;
    private String result;
    private String message;

    @JsonProperty("trace_id")
    private String traceId;
}
