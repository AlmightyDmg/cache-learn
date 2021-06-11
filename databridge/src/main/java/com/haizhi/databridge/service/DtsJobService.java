package com.haizhi.databridge.service;

import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

public interface DtsJobService {
    DataTransJobVo getJobExecInfo(String jobId, String jobType);
    String updateJobStatus(String jobId, String jobType, Integer jobStatus, Long startTime, Long endTime);
    String updateJobTask(JobUnitStateForm form);
    void updateJobTaskRel(String jobId, String fromTableId, String toTableId, String taskId);
}
