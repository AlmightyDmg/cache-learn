package com.haizhi.dataclient.connection.dmc.client.mobius.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DelOldDataReq {
    @Field("tbName")
    String tbName;

    @Field("tag")
    String tag;
}
