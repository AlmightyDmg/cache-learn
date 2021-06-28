package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.Field;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 14:05:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobUnitStateForm {
    @Field("jobId")
    private String jobId;

    @Field("fromTableId")
    private String fromTableId;

    @Field("toTableId")
    private String toTableId;

    @Field("tableStatus")
    private Integer tableStatus;

    @Field("startTime")
    private Long startTime;

    @Field("endTime")
    private Long endTime;

    @Field("jobType")
    private String jobType;

    @Field("userId")
    private String userId;

    @Field("toFolderId")
    private String toFolderId;

    @Field("increateValue")
    private String increateValue;

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
