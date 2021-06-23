package com.haizhi.databridge.client.xxljob;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haizhi.databridge.client.xxljob.request.DataTransJobParam;
import com.haizhi.databridge.client.xxljob.request.PageQueryParam;
import com.haizhi.databridge.client.xxljob.request.XxlJobInfo;
import com.haizhi.databridge.client.xxljob.response.ReturnT;
import com.haizhi.databridge.constants.DatabridgeConstant;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.util.IdUtils;
import com.haizhi.databridge.util.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月21日 18:11:49
 */
@Component
public class JobClientApi {
    private static final String DATA_FIELD = "data";
    private static final int SUCCESS_CODE = 200;
    private static final int PAGE_SIZE = 200;

    @Autowired
    XxlJobClient client;

    /**
     *
     * @param cronExpr
     * @param scheduleType: 调度类型 NONE/CRON/FIX_RATE
     * @param dataTransJobParam
     * @return
     */
    public String add(String cronExpr, String scheduleType, DataTransJobParam dataTransJobParam) {
        String jobDesc = IdUtils.genKey("databridge");
        ReturnT<String> result = client.add(XxlJobInfo.builder()
                .jobDesc(jobDesc)
                .author(MDC.get(DatabridgeConstant.USER_ID))
                .scheduleType(scheduleType)
                .scheduleConf(cronExpr)
                .executorParam(JsonUtils.toJson(dataTransJobParam))
                .executorBlockStrategy("SERIAL_EXECUTION")
                .misfireStrategy("DO_NOTHING")
                .executorRouteStrategy("FIRST")
                .glueType("BEAN")
                .jobGroup(1)
                .executorFailRetryCount(1)
                .executorTimeout(0)
                .executorHandler("dataTransJobHandler")
                .build());

        return genJobId(Integer.parseInt(handleResult(result)), jobDesc);
    }

    public String update(String jobId, String cronExpr, String scheduleType, DataTransJobParam dataTransJobParam) {
        String jobDesc = jobId.split("-")[1];
        int xxlJobId = getXxlJobId(jobId);

        Map<String, Object> jobMap = client.pageList(PageQueryParam.builder()
                .start(0)
                .length(PAGE_SIZE)
                .jobGroup(1)
                .jobDesc(jobDesc)
                .triggerStatus(-1)
                .build());

        List<XxlJobInfo> matchJobList =
                ((List<Map<String, Object>>) jobMap.get(DATA_FIELD)).stream().filter(job -> job.get("id").equals(xxlJobId))
                        .map(x -> JsonUtils.toObject(JsonUtils.map2Json(x), XxlJobInfo.class)).collect(Collectors.toList());

        if (matchJobList.isEmpty()) {
            throw new DatabridgeException("xxljob not exist");
        }

        matchJobList.get(0).setScheduleConf(cronExpr);
        matchJobList.get(0).setScheduleType(scheduleType);
        matchJobList.get(0).setJobDesc(JsonUtils.toJson(dataTransJobParam));
        return handleResult(client.update(matchJobList.get(0)));
    }

    public Map<String, Object> pageList(PageQueryParam queryParam) {
        return client.pageList(queryParam);
    }

    public String remove(String jobId) {
        return handleResult(client.remove(getXxlJobId(jobId)));
    }

    public String start(String jobId) {
        return handleResult(client.start(getXxlJobId(jobId)));
    }

    public String stop(String jobId) {
        return handleResult(client.stop(getXxlJobId(jobId)));
    }

    public String trigger(String jobId) {
        return handleResult(client.trigger(getXxlJobId(jobId)));
    }

    private int getXxlJobId(String jobId) {
        if (StringUtils.isEmpty(jobId)) {
            throw new DatabridgeException("invalid xxl job id");
        }

        return Integer.parseInt(jobId.split("-")[0]);
    }

    private String handleResult(ReturnT<String> returnT) {
        if (returnT.getCode() == SUCCESS_CODE) {
            return returnT.getContent();
        } else {
            throw new DatabridgeException(String.format("call xxl-job create job error: .", returnT.getContent()));
        }
    }

    public String genJobId(Integer id, String jobDesc) {
        return String.format("%d-%s", id, jobDesc);
    }
}
