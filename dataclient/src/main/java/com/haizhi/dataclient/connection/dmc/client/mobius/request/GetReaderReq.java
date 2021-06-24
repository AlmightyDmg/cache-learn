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
public class GetReaderReq {

    @Field("jobId")
    String jobId;

    @Field("storageId")
    String storageId;

    @Field("tmpSql")
    String tmpSql;

    @Field("maxSql")
    String maxSql;
}
