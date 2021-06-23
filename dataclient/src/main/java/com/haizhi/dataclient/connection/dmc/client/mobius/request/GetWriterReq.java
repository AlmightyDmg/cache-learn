package com.haizhi.dataclient.connection.dmc.client.mobius.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;
import retrofit2.http.JsonBeanParam;

import com.haizhi.dataclient.connection.dmc.client.mobius.common.TbCol;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetWriterReq {

    @Field("jobId")
    String jobId;

    @Field("storageId")
    String storageId;

    @Field("column")
    @JsonBeanParam
    List<TbCol> column;
}
