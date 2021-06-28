package com.haizhi.databridge.task;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.exportdata.JobBean;
import com.haizhi.databridge.bean.domain.importdata.TSchedulerBean;
import com.haizhi.databridge.client.xxljob.JobClientApi;
import com.haizhi.databridge.repository.exportdata.JobRepository;
import com.haizhi.databridge.repository.importdata.TSchedulerRepository;

@Slf4j
@Component
public class SyncXxlJob {
    @Autowired
    private JobClientApi jobClientApi;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TSchedulerRepository schedulerRepository;

    @PostConstruct
    void sync() {
        new Thread(() -> {
            List<JobBean> jobBeanList = jobRepository.findAll();
            if (!ObjectUtils.isEmpty(jobBeanList)) {
                log.info("start sync export job to xxljob");
//                jobBeanList.forEach(jobBean -> {
//                    if (!jobClientApi.isXxljobExist(jobBean.getJobId())) {
//                        ExportJobVo.SchedulerConfVo schedulerConfVo = toObject(jobBean.getSyncConfigBack(), ExportJobVo.SchedulerConfVo.class);
//                        Integer mode = jobBean.getExecuteMode();
//                        String scheduleType = "";
//                        String cron = "";
//                        if (mode == 1 || mode == 0) {
//                            scheduleType = "CRON";
//                            cron = schedulerConfVo.getSyncConfig();
//                        } else {
//                            scheduleType = "NONE";
//                            cron = "";
//                        }
//
//                        jobClientApi.add(jobBean.getJobId(), cron, scheduleType,
//                                DataTransJobParam.builder().jobType(EXPORT).jobId(jobBean.getJobId()).build());
//                        if (scheduleType.equalsIgnoreCase(CRON)) {
//                            jobClientApi.start(jobBean.getJobId());
//                        }
//                    }
//                });
            }

            List<TSchedulerBean> schedulerBeanList = schedulerRepository.findAll();
            if (!ObjectUtils.isEmpty(schedulerBeanList)) {
                log.info("start sync import job to xxljob");
//                schedulerBeanList.forEach(schedulerBean -> {
//                    if (jobClientApi.isXxljobExist(schedulerBean.getSchedulerId())) {
//                        DataSchedulerDto.TimingDto timingDto =
//                                JsonUtils.toObject(schedulerBean.getTiming(), DataSchedulerDto.TimingDto.class);
//
//                        String cronExpr = "";
//                        String schedulerId = schedulerBean.getSchedulerId();
//                        DataTransJobParam dataTransJobParam = new DataTransJobParam();
//                        dataTransJobParam.setJobId(schedulerId);
//                        dataTransJobParam.setJobType(IMPORT);
//
//
//                        String cronType = "".equalsIgnoreCase(cronExpr) ? NORMAL : CRON;
//                        if (jobClientApi.isXxljobExist(schedulerId)) {
//                            jobClientApi.add(schedulerId, cronExpr, cronType,
//                                    DataTransJobParam.builder().jobType(EXPORT).jobId(schedulerId).build());
//                            if (cronType.equalsIgnoreCase(CRON)) {
//                                jobClientApi.start(schedulerId);
//                            }
//                        }
//                    }
//                });
            }
        }).start();
    }
}
