package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String jobId;
    private String fromTableId;
    private String toTableId;
    private Integer tableStatus;
    private Long startTime;
    private Long endTime;
    private String jobType;
    private String startLocation;
}
