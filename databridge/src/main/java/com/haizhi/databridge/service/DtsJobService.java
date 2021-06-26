package com.haizhi.databridge.service;

import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.web.controller.form.JobStateForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

public interface DtsJobService {
    DataTransJobVo getJobExecInfo(String jobId, String jobType);
    String updateJobStatus(JobStateForm jobStateForm);
    String updateJobTask(JobUnitStateForm form);
    void updateJobTaskRel(String jobId, String fromTableId, String toTableId, String taskId, String owner);
}
