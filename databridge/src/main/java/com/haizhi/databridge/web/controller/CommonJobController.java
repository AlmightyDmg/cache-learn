package com.haizhi.databridge.web.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.service.DtsJobService;
import com.haizhi.databridge.web.controller.form.JobStateForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

@Slf4j
@RestController
@RequestMapping("/api/job")
@Api(tags = "job")
public class CommonJobController {
    @Autowired
    private DtsJobService dtsJobService;

    @RequestMapping("/execute_info")
    public DataTransJobVo getJobExecInfo(String jobId, String jobType) {
        return dtsJobService.getJobExecInfo(jobId, jobType);
    }

    @RequestMapping("/update_job")
    public String updateJobStatus(JobStateForm jobStateForm) {
        return dtsJobService.updateJobStatus(jobStateForm);
    }

    @RequestMapping("/update_job_task")
    public String updateJobTask(JobUnitStateForm jobUnitStateForm) {
        return dtsJobService.updateJobTask(jobUnitStateForm);
    }

    @PostMapping("/update_job_task_rel")
    public void updateJobTaskRel(String jobId, String taskId, String fromTableId, String toTableId, String owner) {
        dtsJobService.updateJobTaskRel(jobId, fromTableId, toTableId, taskId, owner);
    }
}
