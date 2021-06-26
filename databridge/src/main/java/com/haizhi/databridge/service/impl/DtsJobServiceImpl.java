package com.haizhi.databridge.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haizhi.databridge.bean.domain.importdata.TblTransTaskRelBean;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.repository.importdata.TblTransTaskRelRepository;
import com.haizhi.databridge.service.DataSchedulerService;
import com.haizhi.databridge.service.DtsJobService;
import com.haizhi.databridge.service.export.ExportJobService;
import com.haizhi.databridge.web.controller.form.JobStateForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

@Service
public class DtsJobServiceImpl implements DtsJobService {
    @Autowired
    private ExportJobService exportJobService;

    @Autowired
    private TblTransTaskRelRepository tblTransTaskRelRepo;

    @Autowired
    private DataSchedulerService dataSchedulerService;

    /**
     * @Description // 获取任务执行需要的信息
     * @Date 2021/5/276 7:10 下午
     * @param jobId
     * @return JobParam
     **/
    public DataTransJobVo getJobExecInfo(String jobId, String jobType) {
        if ("import".equals(jobType)) {
            return dataSchedulerService.getJobExecInfo(jobId);
        } else {
            return exportJobService.getJobExecInfo(jobId);
        }
    }

    @Transactional
    public String updateJobStatus(JobStateForm jobStateForm) {
        if ("import".equals(jobStateForm.getJobType())) {
            dataSchedulerService.updateJobStatus(jobStateForm);
        } else {
            exportJobService.updateJobStatus(jobStateForm);
        }

        return "";
    }

    @Transactional
    public String updateJobTask(JobUnitStateForm form) {
        if ("import".equals(form.getJobType())) {
            dataSchedulerService.updateJobExecUnit(form);
        } else {
            exportJobService.updateJobTask(form);
        }

        return "";
    }

    public void updateJobTaskRel(String jobId, String fromTableId, String toTableId, String taskId, String owner) {
        TblTransTaskRelBean tblTransTaskRelBean = tblTransTaskRelRepo.findTransTask(jobId, fromTableId, toTableId)
                .orElse(TblTransTaskRelBean.builder()
                        .jobId(jobId)
                        .fromTableId(fromTableId)
                        .toTableId(toTableId)
                        .transTaskId(taskId)
                        .owner(owner)
                        .build());
        tblTransTaskRelRepo.save(tblTransTaskRelBean);
    }
}
