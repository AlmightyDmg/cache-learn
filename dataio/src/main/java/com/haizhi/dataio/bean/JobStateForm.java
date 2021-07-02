package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobStateForm {
    @Field("jobId")
    String jobId;
    @Field("jobType")
    String jobType;

    @Field("jobStatus")
    Integer jobStatus;

    @Field("startTime")
    Long startTime;

    @Field("endTime")
    Long endTime;

    @Field("errmsg")
    String errmsg;

    @Field("appendCount")
    private Integer appendCount = 0;
    @Field("updateCount")
    private Integer updateCount = 0;
    @Field("deleteCount")
    private Integer deleteCount = 0;
    @Field("failedCount")
    private Integer failedCount = 0;
    @Field("allCount")
    private Integer allCount = 0;
    @Field("filterCount")
    private Integer filterCount = 0;
}
