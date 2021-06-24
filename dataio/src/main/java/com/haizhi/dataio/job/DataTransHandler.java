package com.haizhi.dataio.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.DataTransJobParam;
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
        return databridgeClient.getJobExecInfo(jobParam.getJobId(), jobParam.getJobType());
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

        DataTransJobDetail jobDetail = getJobDetail(jobParam);
        try {
            if (useFlink(jobDetail)) {
                flinkAction.doAction(jobParam);
            } else {
                if (jobDetail.getSyncUnits().isEmpty()) {
                    XxlJobHelper.handleFail("sync unit is empty");
                    return;
                }

                DmcConfig dmcConfig = null;
                // 使用旧的逻辑处理导入导出
                if ("import".equals(jobParam.getJobType())) {
                    // old import
                    importAction.doAction(OldDtsParam.builder()
                            .jobId(jobParam.getJobId())
                            .jobType(jobParam.getJobType())
                            .userId(jobDetail.getUserId())
                            .full(0)
                            .endpoint(jobDetail.getSyncUnits().get(0).getToSink().getUrl()).build());
                } else if ("export".equals(jobParam.getJobType())) {
                    // old export
                    exportAction.doAction(OldDtsParam.builder()
                            .jobId(jobParam.getJobId())
                            .endpoint(jobDetail.getSyncUnits().get(0).getFromSink().getUrl()).build());
                }
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }

        XxlJobHelper.log("end dataTransJobHandler. ");
        // default success
        XxlJobHelper.handleSuccess("success");
    }
}
