package com.haizhi.databridge.web.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobStateForm {
    String jobId;
    String jobTaskId;
    Integer jobStatus;
    String jobType;
    Long startTime;
    Long endTime;
    String errmsg;
    Integer tbSuccess;
    Integer tbTotal;

    private Integer appendCount = 0;
    private Integer updateCount = 0;
    private Integer deleteCount = 0;
    private Integer failedCount = 0;
    private Integer allCount = 0;
    private Integer filterCount = 0;
}
