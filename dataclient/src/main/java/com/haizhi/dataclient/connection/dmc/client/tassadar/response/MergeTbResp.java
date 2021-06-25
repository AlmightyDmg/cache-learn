package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MergeTbResp {
    @JsonProperty("insert_count")
    Integer insertCount;

    @JsonProperty("delete_count")
    Integer deleteCount;

    @JsonProperty("data_changed")
    Boolean dataChanged;

    @JsonProperty("data_count")
    Integer dataCount;

    @JsonProperty("update_count")
    Integer updateCount;
}
