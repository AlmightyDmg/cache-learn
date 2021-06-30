package com.haizhi.dataio.job;

import java.util.Date;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.DataTransJobParam;
import com.haizhi.dataio.bean.JobExecCountDto;
import com.haizhi.dataio.bean.JobStateForm;
import com.haizhi.dataio.bean.OldDtsParam;
import com.haizhi.dataio.client.databridge.DatabridgeClient;
import com.haizhi.dataio.job.action.ExportAction;
import com.haizhi.dataio.job.action.FlinkAction;
import com.haizhi.dataio.job.action.ImportAction;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * Data Trans Job Handler
 */
@Component
public class DataTransHandler {
    private static Logger logger = LoggerFactory.getLogger(DataTransHandler.class);

    @Autowired
    private FlinkAction flinkAction;

    @Autowired
    private ExportAction exportAction;

    @Autowired
    private ImportAction importAction;

    @Autowired
    private DatabridgeClient databridgeClient;

    public DataTransJobDetail getJobDetail(DataTransJobParam jobParam) {
        DataTransJobDetail detail =  databridgeClient.getJobExecInfo(jobParam.getJobId(), jobParam.getJobType());

        if (ObjectUtils.isEmpty(jobParam.getReaderTables())) {
            return detail;
        } else {
            detail.getSyncUnits().removeIf(syncUnit ->
                    !jobParam.getReaderTables().contains(syncUnit.getReader().getTableId()));
            return detail;
        }
    }

    private boolean useFlink(DataTransJobDetail detail) {
        for (DataTransJobDetail.SyncUnit syncUnit : detail.getSyncUnits()) {
            if (FlinkAction.isSupportedDb(syncUnit.getFromSink().getType(), syncUnit.getToSink().getType())) {
                return true;
            }
        }

        return false;
    }
    /**
     * data trans job
     */
    @XxlJob("dataTransJobHandler")
    public void dataTransJobHandler() {
        XxlJobHelper.log("begin dataTransJobHandler. ");
        DataTransJobParam jobParam = JsonUtils.toObject(XxlJobHelper.getJobParam(), DataTransJobParam.class);

        long startTime = new Date().getTime();
        try {
            DataTransJobDetail jobDetail = getJobDetail(jobParam);
            if (useFlink(jobDetail)) {
                logger.info(String.format("jobid: %s, dbtype: %s, use flink to sync", jobParam.getJobId(), jobParam.getJobType()));
                flinkAction.doAction(jobDetail);
            } else {
                if (jobDetail.getSyncUnits().isEmpty()) {
                    XxlJobHelper.handleFail("sync unit is empty");
                    return;
                }

                // 使用旧的逻辑处理导入导出
                if ("import".equals(jobParam.getJobType())) {
                    // old import
                    logger.info(String.format("jobid: %s, dbtype: %s, use noah to import", jobParam.getJobId(), jobParam.getJobType()));
                    importAction.doAction(OldDtsParam.builder()
                            .jobId(jobParam.getJobId())
                            .jobType(jobParam.getJobType())
                            .userId(jobDetail.getSyncUnits().get(0).getUserId())
                            .tables(jobParam.getReaderTables())
                            .full(jobParam.getFull())
                            .endpoint(jobDetail.getSyncUnits().get(0).getToSink().getUrl()).build());
                } else if ("export".equals(jobParam.getJobType())) {
                    // old export
                    logger.info(String.format("jobid: %s, dbtype: %s, use pentagon to export", jobParam.getJobId(), jobParam.getJobType()));
                    exportAction.doAction(OldDtsParam.builder()
                            .jobId(jobParam.getJobId())
                            .endpoint(jobDetail.getSyncUnits().get(0).getFromSink().getUrl()).build());
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            JobExecCountDto jobExecCountDto = new JobExecCountDto();
            databridgeClient.updateJobStatus(JobStateForm.builder().jobId(jobParam.getJobId()).jobType(jobParam.getJobType())
                    .jobStatus(1).startTime(startTime).endTime(new Date().getTime())
                    .allCount(jobExecCountDto.getAllCount())
                    .appendCount(jobExecCountDto.getAppendCount())
                    .deleteCount(jobExecCountDto.getDeleteCount())
                    .failedCount(jobExecCountDto.getFailedCount())
                    .updateCount(jobExecCountDto.getUpdateCount())
                    .filterCount(jobExecCountDto.getFilterCount()).build());

            XxlJobHelper.handleFail(e.getMessage());
        }

        XxlJobHelper.log("end dataTransJobHandler. ");
        // default success
        XxlJobHelper.handleSuccess("success");
    }
}
