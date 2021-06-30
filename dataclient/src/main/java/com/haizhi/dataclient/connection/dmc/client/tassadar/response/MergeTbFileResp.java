package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MergeTbFileResp {
    @Field("data_changed")
    private Boolean dataChanged;

    @Field("data_count")
    private Integer dataCount;
}
