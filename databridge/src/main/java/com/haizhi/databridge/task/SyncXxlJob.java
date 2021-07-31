package com.haizhi.databridge.task;

//import static com.haizhi.databridge.constants.DataSourceConstants.TaskType.EXPORT;
//import static com.haizhi.databridge.constants.DataSourceConstants.TaskType.IMPORT;
//import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_QUEUE;
//import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_STOP;
//import static com.haizhi.databridge.constants.DatabridgeConstants.EXPORT_STATUS_SYNC;
//import static com.haizhi.databridge.constants.DatabridgeConstants.IMPORT_STATUS_PENDING;
//import static com.haizhi.databridge.constants.DatabridgeConstants.IMPORT_STATUS_SYNCING;
//import static com.haizhi.databridge.util.CronUtils.toQuartsCron;

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
import com.haizhi.databridge.repository.importdata.SkdJobRelRepository;
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

    @Autowired
    private SkdJobRelRepository jobRel;

    @PostConstruct
    void sync() {
        new Thread(() -> {
            List<JobBean> jobBeanList = jobRepository.findAll();
            if (!ObjectUtils.isEmpty(jobBeanList)) {
//                log.info("start check running export job");
//                jobBeanList.forEach(jobBean -> {
//                    String jobId = jobBean.getJobId();
//                    if ((ObjectUtils.nullSafeEquals(jobBean.getStatus(), EXPORT_STATUS_SYNC))) {
//                        Optional<JobRelBean> jobRelBean = jobRel.findByJobId(jobId);
//                        if (jobRelBean.isPresent()) {
//                            if (!jobClientApi.isJobRunning(jobId)) {
//                                jobClientApi.trigger(jobId, DataTransJobParam.builder().jobId(jobId).jobType(EXPORT).build());
//                            }
//                        } else {
//                            log.error("export job has no xxljob");
//                        }
//                    }
//                });
            }

            List<TSchedulerBean> schedulerBeanList = schedulerRepository.findAll();
            if (!ObjectUtils.isEmpty(schedulerBeanList)) {
//                log.info("start check running import job");
//                schedulerBeanList.forEach(schedulerBean -> {
//                    String schedulerId = schedulerBean.getSchedulerId();
//                    if (ObjectUtils.nullSafeEquals(schedulerBean.getStatus(), IMPORT_STATUS_SYNCING)
//                            || ObjectUtils.nullSafeEquals(schedulerBean.getStatus(), IMPORT_STATUS_PENDING)) {
//                        Optional<JobRelBean> jobRelBean = jobRel.findByJobId(schedulerId);
//                        if (jobRelBean.isPresent()) {
//                            // 如果任务状态为运行中，但是任务队列中没有该任务，且任务启动超过30min
//                            if (!jobClientApi.isJobRunning(schedulerId) && expire(schedulerBean.getStartAt().getTime())) {
//                                jobClientApi.trigger(schedulerId,
//                                        DataTransJobParam.builder().jobId(schedulerId).jobType(IMPORT).build());
//                            }
//                        } else {
//                            log.error("import job has no xxljob");
//                        }
//                    }
//                });
            }
        }).start();
    }

    private boolean expire(long startTime) {
        return System.currentTimeMillis() - startTime > 30 * 60 * 1000;
    }
}
