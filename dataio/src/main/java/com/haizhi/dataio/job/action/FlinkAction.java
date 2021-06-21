package com.haizhi.dataio.job.action;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.haizhi.dataclient.connection.dmc.client.tassadar.response.CreateTbResp;
import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;
import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.DataTransJobParam;
import com.haizhi.dataio.bean.JobUnitStateForm;
import com.haizhi.dataio.client.databridge.DatabridgeClient;
import com.haizhi.dataio.client.flinkx.FlinkxClient;
import com.haizhi.dataio.exception.DataioException;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description use flinkx to trans table data
 * @createTime 2021年05月27日 10:38:30
 */
@Slf4j
@Component
public class FlinkAction extends AbstractFlinkAction<DataTransJobParam, DataTransJobDetail, FlinkAction.FlinkActionParam> {
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Long>> unitStartTime = new ThreadLocal<>();
    public static final long SLEEP_TIME = 2000L;

    static {
        unitStartTime.set(new HashMap<>());
    }
    private static Set<String> useFlinkDbType = new HashSet<>();
    static {
        useFlinkDbType.add("greenplum");
    }

    @Autowired
    private FlinkxClient flinkxClient;

    @Autowired
    private DatabridgeClient databridgeClient;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    @FieldNameConstants
    public static class FlinkActionParam {
        String taskType;
        String jobId;
        String taskId;
        DataTransJobDetail.Sink fromSink;
        DataTransJobDetail.Sink toSink;
        DataTransJobDetail.Reader reader;
        DataTransJobDetail.Writer writer;
        String userId;
    }

    public static boolean isSupportedDb(String fromDbType, String toDbTYpe) {
        return useFlinkDbType.contains(fromDbType) || useFlinkDbType.contains(toDbTYpe);
    }

    @Override
    protected void begin(DataTransJobDetail info) {
        // 更新同步任务的状态
        startTime.set(new Date().getTime());
        databridgeClient.updateJobStatus(info.getJobId(), info.getJobType(), 0, startTime.get(), null);
    }

    @Override
    protected void end(DataTransJobDetail info, boolean success) {
        Long endTime = new Date().getTime();
        databridgeClient.updateJobStatus(info.getJobId(), info.getJobType(), success ? 2 : 1, startTime.get(), endTime);
    }

    @Override
    protected void beforeExec(FlinkActionParam unit) throws DataioException {
        String fromDbType = unit.getFromSink().getType();
        String toDbType = unit.getToSink().getType();
        if (!FlinkAction.isSupportedDb(fromDbType, toDbType)) {
            String errInfo = String.format("flink not support from %s to %s", fromDbType, toDbType);
            log.error(errInfo);
            return;
        }

        DmcConfig dmcConfig = null;
        // 同步表状态
        if ("DMC".equals(unit.getToSink().getType())) {
            dmcConfig = JsonUtils.toObject(unit.getToSink().getUrl(), DmcConfig.class);
            DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(dmcConfig);
            String tableId = unit.getWriter().getTableId();
            if (StringUtils.isEmpty(tableId)) {
                String fields = unit.getWriter().getColumns().stream()
                        .map(DataTransJobDetail.Column::getName).collect(Collectors.joining(","));
                CreateTbResp resp = dmcTableApi.createTb(unit.getWriter().getTableName(), fields, unit.getWriter().getTableName());
                tableId = resp.getTbId();
            }

            String tableStorPath = dmcTableApi.getDmcWriterPath(tableId, unit.getUserId());
            unit.getWriter().setTableId(tableId);
            unit.getWriter().setTablePath(tableStorPath);

            // 0:CREATE|1:SYNCING|2:SYNC_FINISH|3:SYNC_ERROR|4:MIGRATE|5:MIGRATE_ERROR|6:MERGE_ERROR
            dmcTableApi.updateTableStatus(tableId, 1, unit.getUserId());
        }

        if (!"DMC".equals(unit.getFromSink().getType())) {
            if (unitStartTime.get() == null) {
                unitStartTime.set(new HashMap<>());
            }
            unitStartTime.get().put(unit.getReader().getTableId(), new Date().getTime());
            databridgeClient.updateJobExecUnit(JobUnitStateForm.builder().jobId(unit.getJobId())
                    .fromTableId(unit.getReader().getTableId()).toTableId(unit.getWriter().getTableId())
                    .startTime(unitStartTime.get().get(unit.getReader().getTableId())).endTime(new Date().getTime())
                    .jobType(unit.getTaskType()).tableStatus(0).userId(unit.getUserId()).build());
        } else {
            dmcConfig = JsonUtils.toObject(unit.getFromSink().getUrl(), DmcConfig.class);
            DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(dmcConfig);
            if (unit.getReader().getSync().getCheckRule().getFailureStrategy() == 0) {
                if (!dmcTableApi.checkTableRule()) {
                    throw new DataioException("check failed.");
                }
            }

            if (unit.getReader().getFilter() == null) {
                unit.getReader().setFilter(DataTransJobDetail.Filter.builder()
                        .sqlConditions(unit.getReader().getSync().getCheckRule().getRules()).build());
            } else {
                List<String> checkSqlList = unit.getReader().getSync().getCheckRule().getRules();
                unit.getReader().getFilter().getSqlConditions().addAll(checkSqlList);
            }
        }
    }

    @Override
    protected String execute(FlinkActionParam unit) {
        String taskId = unit.getTaskId();
        try {
            // 同步操作
            if (StringUtils.isEmpty(taskId)) {
                taskId = flinkxClient.startJob(unit).getResult();

                unit.setTaskId(taskId);
                // 更新task
                databridgeClient.updateJobTaskRel(unit.getJobId(), taskId, unit.getReader().getTableId(),
                        unit.getWriter().getTableId());
            }
        } catch (DataioException e) {
            log.error("flink sync failed", e);
        }

        return taskId;
    }

    @Override
    protected String checkResult(FlinkActionParam unitParam) {
        String taskId = unitParam.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            return "failed";
        }

        String state = flinkxClient.getStatus(taskId).getResult().getStatus().getState();

        return state.toLowerCase();
    }

    @Override
    protected void afterExec(FlinkActionParam unit, boolean success) {
        // 同步表状态
        if ("dmc".equals(unit.getToSink().getType())) {
            DmcConfig dmcConfig = JsonUtils.toObject(unit.getToSink().getUrl(), DmcConfig.class);
            DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(dmcConfig);

            /* 合表 */
            dmcTableApi.mergeTbFile(unit.getWriter().getTableId(), unit.getWriter().getTableId());

            /* 更新表状态 */
            dmcTableApi.updateTableStatus(unit.getWriter().getTableId(), 1, unit.getUserId());
        }

        if (!"dmc".equals(unit.getFromSink().getType())) {
            databridgeClient.updateJobExecUnit(JobUnitStateForm.builder()
                    .jobId(unit.getJobId())
                    .fromTableId(unit.getReader().getTableId())
                    .toTableId(unit.getWriter().getTableId())
                    .startTime(unitStartTime.get().get(unit.getReader().getTableId()))
                    .endTime(new Date().getTime())
                    .jobType(unit.getTaskType())
                    .tableStatus(success ? 2 : 1)
                    .build());
        }
    }

    @Override
    protected List<FlinkActionParam> getExecUnitList(DataTransJobDetail jobDetail) {
        return jobDetail.getSyncUnits().stream().map(syncUnit -> FlinkActionParam.builder()
                    .fromSink(syncUnit.getFromSink())
                    .toSink(syncUnit.getToSink())
                    .reader(syncUnit.getReader())
                    .writer(syncUnit.getWriter())
                    .jobId(jobDetail.getJobId())
                    .taskType(jobDetail.getJobType())
                    .userId(jobDetail.getUserId())
                    .build()).collect(Collectors.toList());
    }

    @Override
    public DataTransJobDetail getJobDetail(DataTransJobParam jobParam) {
        return databridgeClient.getJobExecInfo(jobParam.getJobId(), jobParam.getJobType());
    }
}


