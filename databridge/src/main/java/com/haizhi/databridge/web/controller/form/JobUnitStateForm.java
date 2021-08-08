package com.haizhi.databridge.web.controller.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月03日 20:58:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobUnitStateForm {
    private String jobId;
    private String fromTableId;
    private String toTableId;
    private Integer tableStatus;
    private Long startTime;
    private Long endTime;
    private String jobType;
    private String userId;
    private String toFolderId;
    private String increateValue;

    private Integer appendCount = 0;
    private Integer updateCount = 0;
    private Integer deleteCount = 0;
    private Integer failedCount = 0;
    private Integer allCount = 0;
    private Integer filterCount = 0;

    private String errorMsg = "";
}
