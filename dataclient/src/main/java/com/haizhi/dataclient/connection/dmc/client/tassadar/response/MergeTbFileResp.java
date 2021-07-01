package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MergeTbFileResp {
    @JsonProperty("data_changed")
    private Boolean dataChanged;

    @JsonProperty("data_count")
    private Integer dataCount;
}
