package com.haizhi.databridge.client.xxljob;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haizhi.databridge.bean.domain.importdata.JobRelBean;
import com.haizhi.databridge.client.xxljob.request.DataTransJobParam;
import com.haizhi.databridge.client.xxljob.request.LogQueryParam;
import com.haizhi.databridge.client.xxljob.request.PageQueryParam;
import com.haizhi.databridge.client.xxljob.request.XxlJobInfo;
import com.haizhi.databridge.client.xxljob.response.ReturnT;
import com.haizhi.databridge.constants.DatabridgeConstant;
import com.haizhi.databridge.constants.DatabridgeConstants;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.importdata.SkdJobRelRepository;
import com.haizhi.databridge.util.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月21日 18:11:49
 */
@Component
@Slf4j
public class JobClientApi {
    private static final String DATA_FIELD = "data";
    private static final int SUCCESS_CODE = 200;
    private static final int PAGE_SIZE = 20000;
    private static final int LOG_STATUS_RUNNING = 3;
    private static final int DEFAULT_JOB_GROUP = 1;
    private static final String DEF_EXEC_HANDLER = "dataTransJobHandler";

    @Autowired
    XxlJobClient client;

    @Autowired
    SkdJobRelRepository jobRel;

    /**
     *
     * @param cronExpr
     * @param scheduleType: 调度类型 NONE/CRON/FIX_RATE
     * @param dataTransJobParam
     * @return
     */
    public String add(String jobId, String cronExpr, String scheduleType, DataTransJobParam dataTransJobParam) {
        ReturnT<String> result = client.add(XxlJobInfo.builder()
                .jobDesc(jobId)
                .author(MDC.get(DatabridgeConstant.USER_ID))
                .scheduleType(scheduleType)
                .scheduleConf(cronExpr)
                .executorParam(JsonUtils.toJson(dataTransJobParam))
                .executorBlockStrategy("SERIAL_EXECUTION")
                .misfireStrategy("DO_NOTHING")
                .executorRouteStrategy("FIRST")
                .glueType("BEAN")
                .jobGroup(DEFAULT_JOB_GROUP)
                .executorFailRetryCount(1)
                .executorTimeout(0)
                .executorHandler(DEF_EXEC_HANDLER)
                .build());

        String xxlJobId = handleResult(result);
        String userId = MDC.get(DatabridgeConstants.USER_ID);
        jobRel.save(JobRelBean.builder().jobId(jobId).distJobId(xxlJobId).owner(userId).build());

        return xxlJobId;
    }

    public String update(String jobId, String cronExpr, String scheduleType, DataTransJobParam dataTransJobParam) {
        XxlJobInfo xxlJobInfo = getJobInfo(jobId);

        xxlJobInfo.setScheduleConf(cronExpr);
        xxlJobInfo.setScheduleType(scheduleType);
        xxlJobInfo.setExecutorParam(JsonUtils.toJson(dataTransJobParam));
        log.info(JsonUtils.toJson(xxlJobInfo));
        return handleResult(client.update(xxlJobInfo));
    }

    public Map<String, Object> pageList(PageQueryParam queryParam) {
        return client.pageList(queryParam);
    }

    public String remove(String jobId) {
        String res = handleResult(client.remove(getXxlJobId(jobId)));
        jobRel.logicDeleteByJobId(jobId);
        return res;
    }

    public String start(String jobId) {
        return handleResult(client.start(getXxlJobId(jobId)));
    }

    public String stop(String jobId) {
        return handleResult(client.stop(getXxlJobId(jobId)));
    }

    public String trigger(String jobId, DataTransJobParam jobParam) {
//        XxlJobInfo xxlJobInfo = getJobInfo(jobId);
        return handleResult(client.trigger(getXxlJobId(jobId), JsonUtils.toJson(jobParam)));
    }

    public int cancel(String jobId) {
        XxlJobInfo xxlJobInfo = getJobInfo(jobId);
        Map<String, Object> logs = client.logList(LogQueryParam.builder()
                .start(0).length(PAGE_SIZE).jobGroup(DEFAULT_JOB_GROUP).jobId(xxlJobInfo.getId()).logStatus(LOG_STATUS_RUNNING).build());
        ((List<Map<String, Object>>) logs.get(DATA_FIELD))
                .forEach(log -> client.logKill(Integer.parseInt(String.valueOf(log.get("id")))));
        return ((List<Map<String, Object>>) logs.get(DATA_FIELD)).size();
    }

    public boolean isJobRunning(String jobId) {
        XxlJobInfo xxlJobInfo = getJobInfo(jobId);
        Map<String, Object> logs = client.logList(LogQueryParam.builder()
                .start(0).length(PAGE_SIZE).jobGroup(DEFAULT_JOB_GROUP).jobId(xxlJobInfo.getId()).logStatus(LOG_STATUS_RUNNING).build());

        return logs.size() > 0;
    }

    public Integer getXxlJobId(String jobId) {
        JobRelBean jobRelBean = jobRel.findByJobId(jobId).orElseThrow(() -> new DatabridgeException("xxljob not exist"));
        if (StringUtils.isEmpty(jobRelBean.getDistJobId())) {
            throw new DatabridgeException("invalid xxl job id");
        }

        return Integer.parseInt(jobRelBean.getDistJobId());
    }

    public boolean isXxljobExist(String jobId) {
        return jobRel.findByJobId(jobId).isPresent();
    }

    private String handleResult(ReturnT<String> returnT) {
        if (returnT.getCode() == SUCCESS_CODE) {
            return returnT.getContent();
        } else {
            log.error(returnT.getMsg());
            throw new DatabridgeException(String.format("call xxl-job create job error: .", returnT.getMsg()));
        }
    }

    private XxlJobInfo getJobInfo(String jobId) {
        int xxlJobId = getXxlJobId(jobId);

        Map<String, Object> jobMap = client.pageList(PageQueryParam.builder()
                .start(0)
                .length(PAGE_SIZE)
                .jobGroup(DEFAULT_JOB_GROUP)
                .jobDesc(jobId)  //databridge上的jobId对应xxljob上的jobDesc
                .triggerStatus(-1)
                .build());

        List<XxlJobInfo> matchJobList =
                ((List<Map<String, Object>>) jobMap.get(DATA_FIELD)).stream().filter(job -> job.get("id").equals(xxlJobId))
                        .map(x -> JsonUtils.toObject(JsonUtils.map2Json(x), XxlJobInfo.class)).collect(Collectors.toList());

        if (matchJobList.isEmpty()) {
            throw new DatabridgeException("xxljob not exist");
        }

        return matchJobList.get(0);
    }

//    public String genJobId(Integer id, String jobDesc) {
//        return String.format("%d-%s", id, jobDesc);
//    }
}
