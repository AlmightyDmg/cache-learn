package com.haizhi.dataio.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haizhi.dataio.bean.DataTransJobParam;
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

    /**
     * data trans job
     */
    @XxlJob("dataTransJobHandler")
    public void dataTransJobHandler() {
        XxlJobHelper.log("begin dataTransJobHandler. ");
        DataTransJobParam jobParam = JsonUtils.toObject(XxlJobHelper.getJobParam(), DataTransJobParam.class);

        try {
            if (jobParam.getOldDataTrans() == 1) {
                flinkAction.doAction(jobParam);
            } else {
                // 使用旧的逻辑处理导入导出
                if ("import".equals(jobParam.getTaskType())) {
                    // old import
                    importAction.doAction(jobParam);
                } else if ("export".equals(jobParam.getTaskType())) {
                    // old export
                    exportAction.doAction(jobParam);
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
