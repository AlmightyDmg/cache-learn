// CHECKSTYLE:OFF
package com.haizhi.dataio.job.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.haizhi.dataclient.connection.dmc.client.mobius.common.TbCol;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetReaderReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetWriterReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcReader;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcWriter;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;
import com.haizhi.dataio.bean.DataTransJobDetail;
import com.haizhi.dataio.bean.JobExecCountDto;
import com.haizhi.dataio.bean.JobStateForm;
import com.haizhi.dataio.bean.JobUnitStateForm;
import com.haizhi.dataio.client.databridge.DatabridgeClient;
import com.haizhi.dataio.client.flinkx.FlinkxClient;
import com.haizhi.dataio.client.flinkx.response.FlinkxJobStatus;
import com.haizhi.dataio.exception.DataioException;
import com.haizhi.dataio.job.column.ReaderConnection;
import com.haizhi.dataio.job.column.MetaColumn;
import com.haizhi.dataio.job.column.WriterConnection;
import com.haizhi.dataio.job.reader.JdbcReader;
import com.haizhi.dataio.job.reader.Reader;
import com.haizhi.dataio.job.sql.BaseSqlOperator;
import com.haizhi.dataio.job.sql.CompareOperator;
import com.haizhi.dataio.job.sql.EmptyOperator;
import com.haizhi.dataio.job.sql.JdbcTypeMapping;
import com.haizhi.dataio.job.sql.NotOperator;
import com.haizhi.dataio.job.sql.RelOperator;
import com.haizhi.dataio.job.sql.SqlOperator;
import com.haizhi.dataio.job.writer.JdbcWriter;
import com.haizhi.dataio.job.writer.Writer;
import com.haizhi.dataio.utils.JobUtils;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description use flinkx to trans table data
 * @createTime 2021年05月27日 10:38:30
 */
@Slf4j
@Component
public class FlinkAction extends AbstractFlinkAction<DataTransJobDetail, DataTransJobDetail, FlinkAction.FlinkActionParam> {
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Long>> unitStartTime = new ThreadLocal<>();
    private static ThreadLocal<Map<String, JobExecCountDto>> countRes = new ThreadLocal<>();
    public static final long SLEEP_TIME = 2000L;
    public static final int DEFAULT_FETCH_SIZE = 20000;

    private static Map<Integer, String> fieldTypeMap = new HashMap<>();
    private static Map<String, Integer> fieldTypeIndexMap = new HashMap<>();

    private static final int NUMBER_TYPE = 0;
    private static final int DOUBLE_TYPE = 1;
    private static final int STRING_TYPE = 2;
    private static final int DATE_TYPE = 3;
    private static final int BLOB_TYPE = 4;

    static {
        unitStartTime.set(new HashMap<>());

        fieldTypeMap.put(NUMBER_TYPE, "number");
        fieldTypeMap.put(DOUBLE_TYPE, "double");
        fieldTypeMap.put(STRING_TYPE, "string");
        fieldTypeMap.put(DATE_TYPE, "date");
        fieldTypeMap.put(BLOB_TYPE, "blob");

        fieldTypeIndexMap.put("number", NUMBER_TYPE);
        fieldTypeIndexMap.put("double", DOUBLE_TYPE);
        fieldTypeIndexMap.put("string", STRING_TYPE);
        fieldTypeIndexMap.put("date", DATE_TYPE);
        fieldTypeIndexMap.put("blob", BLOB_TYPE);
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
        String jobType;
        String jobId;
        String taskId;
        DataTransJobDetail.Sink fromSink;
        DataTransJobDetail.Sink toSink;
        DataTransJobDetail.Reader reader;
        DataTransJobDetail.Writer writer;
        String userId;

        int readRecords = 0;
        int writeRecords = 0;
        String errorMsg = "success";
    }

    public static boolean isSupportedDb(String fromDbType, String toDbTYpe) {
        return useFlinkDbType.contains(fromDbType.toLowerCase()) || useFlinkDbType.contains(toDbTYpe.toLowerCase());
    }

    @Override
    protected void begin(DataTransJobDetail info) {
        // 更新同步任务的状态
        startTime.set(new Date().getTime());
        JobExecCountDto jobExecCountDto = new JobExecCountDto();
        log.info(String.format("jobid: %s, begin to sync", info.getJobId()));
        databridgeClient.updateJobStatus(JobStateForm.builder().jobId(info.getJobId()).jobType(info.getJobType())
                .jobStatus(0).startTime(startTime.get()).endTime(null).jobTaskId(JobUtils.cntx().getJobTaskId())
                .allCount(jobExecCountDto.getAllCount())
                .appendCount(jobExecCountDto.getAppendCount())
                .deleteCount(jobExecCountDto.getDeleteCount())
                .failedCount(jobExecCountDto.getFailedCount())
                .updateCount(jobExecCountDto.getUpdateCount())
                .filterCount(jobExecCountDto.getFilterCount())
                .build());
    }

    @Override
    protected void end(DataTransJobDetail info, int status, String errmsg) {
        JobExecCountDto jobExecCountDto = new JobExecCountDto();
        log.info(String.format("jobid: %s, end to sync", info.getJobId()));
        if (countRes.get() != null) {
            countRes.get().forEach((key, value) -> {
                jobExecCountDto.setAllCount(jobExecCountDto.getAllCount() + value.getAllCount());
                jobExecCountDto.setDeleteCount(jobExecCountDto.getDeleteCount() + value.getDeleteCount());
                jobExecCountDto.setAppendCount(jobExecCountDto.getAppendCount() + value.getAppendCount());
                jobExecCountDto.setUpdateCount(jobExecCountDto.getUpdateCount() + value.getUpdateCount());
                jobExecCountDto.setFailedCount(jobExecCountDto.getFailedCount() + value.getFailedCount());
                jobExecCountDto.setFilterCount(jobExecCountDto.getFilterCount() + value.getFilterCount());
            });
        }
        databridgeClient.updateJobStatus(JobStateForm.builder().jobId(info.getJobId()).jobType(info.getJobType())
                .jobStatus(status).startTime(startTime.get()).endTime(new Date().getTime()).jobTaskId(JobUtils.cntx().getJobTaskId())
                .tbTotal(JobUtils.cntx().getTotal()).tbSuccess(JobUtils.cntx().getSuccess())
                .allCount(jobExecCountDto.getAllCount())
                .appendCount(jobExecCountDto.getAppendCount())
                .deleteCount(jobExecCountDto.getDeleteCount())
                .failedCount(jobExecCountDto.getFailedCount())
                .updateCount(jobExecCountDto.getUpdateCount())
                .filterCount(jobExecCountDto.getFilterCount())
                .errmsg(errmsg)
                .build());
    }

    private List<String> createDmcTable(FlinkActionParam unit) {
        DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getToSink().getUrl());
        List<CreateTbReq.TbField> fields = unit.getWriter().getColumns().stream()
                .map(col -> CreateTbReq.TbField.builder().name(col.getName())
                        .type(fieldTypeIndexMap.getOrDefault(col.getType(), 2))
                        .remark(col.getRemark()).uniqIndex(col.getUniqIndex()).build()).collect(Collectors.toList());
        List<String> folderTb = dmcTableApi.createTb(unit.getWriter().getTableName(), fields,
                unit.getUserId(), unit.getToSink().getSchema(), unit.getToSink().getCatalog());

        return folderTb;
    }

    @Override
    protected void beforeExec(FlinkActionParam unit) throws DataioException {
        String fromDbType = unit.getFromSink().getType();
        String toDbType = unit.getToSink().getType();
        if (!FlinkAction.isSupportedDb(fromDbType, toDbType)) {
            log.error(String.format("flink not support from %s to %s", fromDbType, toDbType));
            return;
        }

        log.info(String.format("jobid: %s, begin from %s to %s",
                unit.getJobId(), unit.getReader().getTableName(), unit.getWriter().getTableName()));

        unitStartTime.set(Optional.ofNullable(unitStartTime.get()).orElse(new HashMap<>()));
        unitStartTime.get().put(unit.getReader().getTableName(), new Date().getTime());

        // 同步表状态
        if ("DMC".equalsIgnoreCase(unit.getToSink().getType())) {
            DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getToSink().getUrl());
            String tableId = unit.getWriter().getTableId();
            if (StringUtils.isEmpty(unit.getWriter().getTableId())) {
                List<String> folderTb = createDmcTable(unit);
                tableId = folderTb.get(1);
                unit.getWriter().setTablePath(folderTb.get(0));
                unit.getWriter().setTableId(folderTb.get(1));
            }

            InfoTbResp dmcTbInfo = dmcTableApi.getDmcTbInfo(tableId);
            Map<String, InfoTbResp.TbField> fieldMap = dmcTbInfo.getFields().stream()
                    .collect(Collectors.toMap(InfoTbResp.TbField::getName, x -> x));

            unit.getWriter().getColumns().forEach(col -> {
                col.setRealName(fieldMap.get(col.getName()).getFieldId());
                col.setType(fieldTypeMap.get(fieldMap.get(col.getName()).getType()));
            });
            unit.getWriter().setTableId(tableId);
            unit.getWriter().setRealName(dmcTbInfo.getStorageId());

            // 0:CREATE|1:SYNCING|2:SYNC_FINISH|3:SYNC_ERROR|4:MIGRATE|5:MIGRATE_ERROR|6:MERGE_ERROR
            dmcTableApi.updateTableStatus(tableId, 1, unit.getUserId());
            if (Integer.valueOf(1).equals(unit.getReader().getSync().getIsTruncate())) {
                dmcTableApi.truncateData(unit.getWriter().getRealName());
            }

        } else {
            DmcTableApi dmcTblApi = DmcApiFactory.getDmcTableApi(unit.getFromSink().getUrl());
            if (unit.getReader().getSync().getCheckRule().getFailureStrategy() == 0) {
                String whereSql = genCheckSql(genWhere(unit), unit.getReader().getSync().getCheckRule()).generate();
                Integer filterCount = dmcTblApi.getDataCount(whereSql, unit.getReader().getRealName());
                if (filterCount > 0) {
                    throw new DataioException("规则校验失败.");
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

        databridgeClient.updateJobExecUnit(JobUnitStateForm.builder().jobId(unit.getJobId())
                .jobTaskId(JobUtils.cntx().getJobTaskId())
                .fromTableId(unit.getReader().getTableId()).toTableId(unit.getWriter().getTableId())
                .toFolderId(unit.getWriter().getTablePath())
                .startTime(unitStartTime.get().get(unit.getReader().getTableName()))
                .jobType(unit.getJobType()).tableStatus(0).userId(unit.getUserId()).build());
    }

    private SqlOperator genCheckSql(SqlOperator baseSqlOp, DataTransJobDetail.CheckRule checkRule) {
        SqlOperator checkOp = new NotOperator(new RelOperator("and",
                checkRule.getRules().stream().map(BaseSqlOperator::new).collect(Collectors.toList())));
        return new RelOperator("and", Arrays.asList(baseSqlOp, checkOp));
    }

    private Reader<JdbcReader> buildJdbcReader(FlinkActionParam unit) {
        Reader<JdbcReader> jdbcReader = new Reader<>();
        jdbcReader.setName(unit.getFromSink().getType().toLowerCase() + "reader");
        jdbcReader.setParameter(JdbcReader.builder()
                .username(unit.getFromSink().getUsername())
                .password(unit.getFromSink().getPassword())
                .fetchSize(Optional.ofNullable(unit.getReader().getSync().getFetchSize()).orElse(DEFAULT_FETCH_SIZE))
                .connection(Arrays.asList(ReaderConnection.builder()
                        .jdbcUrl(Arrays.asList(String.format(ReaderConnection.TYPE_PATTERN.get(unit.getFromSink().getType().toLowerCase()),
                                unit.getFromSink().getUrl(),
                                unit.getFromSink().getCatalog())))
                        .table(Arrays.asList(unit.getReader().getTableName()))
                        .schema(unit.getFromSink().getSchema())
                        .build()))
                .column(unit.getReader().getColumns().stream().map(col ->
                        MetaColumn.builder()
                                .name(col.getName())
                                .value("BDP_AUDIT".equals(col.getName()) ? "now()" : col.getValue()).build())
                        .collect(Collectors.toList()))
                .where(genWhere(unit).generate())
                .build());
        return jdbcReader;
    }

    private Writer<JdbcWriter> buildJdbcWriter(FlinkActionParam unit) {
        Writer<JdbcWriter> jdbcWriter = new Writer<>();

        List<String> preSql = new ArrayList<>();
        if (unit.getReader().getSync().getIsTruncate() == 1) {
            preSql.add(String.format("truncate table %s", unit.getWriter().getTableName()));
        }

        jdbcWriter.setName(unit.getToSink().getType().toLowerCase() + "writer");
        jdbcWriter.setParameter(JdbcWriter.builder()
                .username(unit.getToSink().getUsername())
                .password(unit.getToSink().getPassword())
                .connection(Arrays.asList(WriterConnection.builder()
                        .jdbcUrl(String.format(ReaderConnection.TYPE_PATTERN.get(unit.getToSink().getType().toLowerCase()),
                                unit.getToSink().getUrl(),
                                unit.getToSink().getCatalog()))
                        .table(Arrays.asList(unit.getWriter().getTableName()))
                        .build()))
                .column(unit.getWriter().getColumns().stream().map(col ->
                        MetaColumn.builder().name(col.getName()).type(JdbcTypeMapping.getGpType(col.getType())).build())
                        .collect(Collectors.toList()))
                .insertSqlMode("copy")
                .writeMode("insert")
                .preSql(preSql)
                .build());

        return jdbcWriter;
    }

    private String getHdfsType(String dmcType) {
        switch (dmcType) {
            case "date": return "timestamp";
            default: return "string";
        }
    }

    @Override
    protected String execute(FlinkActionParam unit) {
        log.info(String.format("jobid: %s, exec from %s to %s",
                unit.getJobId(), unit.getReader().getTableName(), unit.getWriter().getTableName()));
        String taskId = unit.getTaskId();
        try {
            // 同步操作
            if (!StringUtils.isEmpty(taskId)) {
                return taskId;
            }

            Object reader;
            Object writer;
            if ("import".equalsIgnoreCase(unit.getJobType())) {
                DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getToSink().getUrl());

                Reader<JdbcReader> jdbcReader = buildJdbcReader(unit);

                GetWriterReq req = GetWriterReq.builder()
                        .jobId(unit.getJobId()).storageId(unit.getWriter().getRealName())
                        .column(unit.getWriter().getColumns().stream().map(col ->
                                TbCol.builder().name(col.getRealName()).type(getHdfsType(col.getType())).build())
                                .collect(Collectors.toList())).build();
                DmcWriter dmcWriter = dmcTableApi.getDmcWriter(req);
                writer = dmcWriter.getWriter();
                reader = jdbcReader;
            } else {
                Writer<JdbcWriter> jdbcWriter = buildJdbcWriter(unit);

                GetReaderReq req = new GetReaderReq();
                req.setJobId(unit.getJobId());
                req.setStorageId(unit.getReader().getRealName());
                String selCols = unit.getReader().getColumns().stream()
                        .map(DataTransJobDetail.Column::getRealName).collect(Collectors.joining(","));
                req.setTmpSql(String.format("select %s from %s where %s", selCols, unit.getReader().getRealName(),
                        genWhere(unit).generate()));

                String maxSql = "";
                if (unit.getReader().getSync() != null && unit.getReader().getSync().getSyncCondition() != null) {
                    maxSql = String.format("select max(%s) from %s_%s",
                            unit.getReader().getSync().getSyncCondition().getField(), unit.getReader().getRealName(), unit.getJobId());
                    log.info("max_sql: (%s)", maxSql);
                }
                req.setMaxSql(maxSql);

                DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getFromSink().getUrl());
                DmcReader dmcReader = dmcTableApi.getDmcReader(req);

                log.info("max_value: (%s)", dmcReader.getMaxValue());
                unit.getReader().setRealName(dmcReader.getTabName().split("_")[0]);

                setNextMaxValue(unit, dmcReader.getMaxValue());

                writer = jdbcWriter;
                reader = dmcReader.getReader();
            }

            String readerStr = JsonUtils.toJson(reader);
            String writerStr = JsonUtils.toJson(writer);
            log.info(String.format("jobid : %s; reader: %s, writer: %s", unit.getJobId(), readerStr, writerStr));
            taskId = flinkxClient.startJob(unit.getJobId(), readerStr, writerStr).getResult().getJobId();

            unit.setTaskId(taskId);
            // 更新task
            databridgeClient.updateJobTaskRel(unit.getJobId(), taskId, unit.getReader().getTableId(),
                    unit.getWriter().getTableId(), unit.getUserId());
        } catch (DataioException e) {
            log.error("flink sync failed", e);
            throw e;
        }

        return taskId;
    }

    private void setNextMaxValue(FlinkActionParam unit, String maxValue) {
        if ("increment".equalsIgnoreCase(unit.getReader().getSync().getType())
                && unit.getReader().getSync().getSyncCondition() != null) {
            if (!ObjectUtils.isEmpty(unit.getReader().getSync().getSyncCondition())
                    || !ObjectUtils.isEmpty(unit.getReader().getSync().getSyncCondition().getField())) {
                DataTransJobDetail.Sync.SyncCondition.Conditon endCondition =
                        DataTransJobDetail.Sync.SyncCondition.Conditon.builder()
                                .operator(">").enable(1).value(maxValue).build();
                unit.getReader().getSync().getSyncCondition().setEnd(endCondition);
            }
        }
    }



    private SqlOperator genWhere(FlinkActionParam unit) {
        List<SqlOperator> sqlOperatorList = new ArrayList<>();
        if (unit.getReader().getSync().getSyncCondition() != null) {
            String field = unit.getReader().getSync().getSyncCondition().getField();
            String fieldType = unit.getReader().getSync().getSyncCondition().getFieldType();
            Object value = unit.getReader().getSync().getSyncCondition().getStart().getValue();
            if (ObjectUtils.isEmpty(value)) {
                return new EmptyOperator();
            }

            if ("increment".equalsIgnoreCase(unit.getReader().getSync().getType())) {
                CompareOperator fromOp = new CompareOperator(field,
                        unit.getReader().getSync().getSyncCondition().getStart().getOperator(),
                        fieldType,
                        unit.getReader().getSync().getSyncCondition().getStart().getValue());

                CompareOperator toOp = new CompareOperator(field,
                        fieldType,
                        unit.getReader().getSync().getSyncCondition().getEnd().getOperator(),
                        unit.getReader().getSync().getSyncCondition().getEnd().getValue());

                sqlOperatorList.add(fromOp);
                sqlOperatorList.add(toOp);
            }
        }

        if (unit.getReader().getFilter() != null) {
            Map<String, DataTransJobDetail.Column> columnTypeMap = unit.getReader().getColumns().stream()
                    .collect(Collectors.toMap(DataTransJobDetail.Column::getName, x -> x));
            DataTransJobDetail.Filter.FilterCondition filterCon = unit.getReader().getFilter().getFilterConditions();
            if (!ObjectUtils.isEmpty(filterCon)) {
                List<SqlOperator> subList = new ArrayList<>();
                for (DataTransJobDetail.Filter.FilterCondition.Condition cond : filterCon.getConditions()) {
                    subList.add(new CompareOperator("\"" + cond.getName() + "\"", cond.getType(),
                            columnTypeMap.get(cond.getName()).getType(),
                            cond.getValue(), columnTypeMap.get(cond.getName()).getRealType()));
                }

                sqlOperatorList.add(new RelOperator(filterCon.getRelationType(), subList));
            }

            if (!ObjectUtils.isEmpty(unit.getReader().getFilter().getSqlConditions())) {
                for (String sqlCondition : unit.getReader().getFilter().getSqlConditions()) {
                    if (!StringUtils.isEmpty(sqlCondition)) {
                        sqlOperatorList.add(new BaseSqlOperator(sqlCondition));
                    }
                }
            }
        }

        return new RelOperator("and", sqlOperatorList);
    }

    @Override
    protected boolean checkResult(FlinkActionParam unitParam) throws DataioException {
        String taskId = unitParam.getTaskId();
        if (StringUtils.isEmpty(taskId)) {
            throw new DataioException("flink task id is empty.");
        }

        FlinkxJobStatus flinkxJobStatus = flinkxClient.getStatus(taskId).getResult();

        String state = flinkxJobStatus.getStatus().getState();
        if ("finished".equalsIgnoreCase(state)) {
            Optional.ofNullable(flinkxJobStatus.getStatus().getVertices()).orElse(new ArrayList<>()).forEach(ver -> {
                unitParam.readRecords += ver.getReadRecords();
                unitParam.writeRecords += ver.getWriteRecords();
            });
            return true;
        }

        if ("failed".equalsIgnoreCase(state)) {
            log.error("flink task failed.");
            throw new DataioException("flink task failed.");
        }

        // 实时更新同步数量
        databridgeClient.updateJobExecUnit(JobUnitStateForm.builder()
                .jobId(unitParam.getJobId()).jobTaskId(JobUtils.cntx().getJobTaskId())
                .fromTableId(unitParam.getReader().getTableId())
                .toTableId(unitParam.getWriter().getTableId())
                .startTime(unitStartTime.get().get(unitParam.getReader().getTableName()))
                .jobType(unitParam.getJobType())
                .tableStatus(0)
                .userId(unitParam.getUserId())
                .appendCount(unitParam.getWriteRecords())
                .filterCount(unitParam.getReadRecords())
                .failedCount(unitParam.getReadRecords() - unitParam.getWriteRecords())
                .allCount(0)
                .updateCount(0)
                .deleteCount(0)
                .errorMsg("success")
                .build());

        return false;
    }

    private JobExecCountDto getExecCount(String tableName) {
        if (countRes.get() == null) {
            countRes.set(new HashMap<>());
        }

        if (countRes.get().get(tableName) == null) {
            countRes.get().put(tableName, new JobExecCountDto());
        }
        return countRes.get().get(tableName);
    }

    @Override
    protected void afterExec(FlinkActionParam unit, boolean success, String errorMsg) {
        // 同步表状态
        String nextIncrValue = null;
        if (success) {
            log.info(String.format("jobid: %s, after from %s to %s",
                    unit.getJobId(), unit.getReader().getTableName(), unit.getWriter().getTableName()));
            try {
                if ("dmc".equalsIgnoreCase(unit.getToSink().getType())) {
                    DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getToSink().getUrl());

                    dmcTableApi.mergeTbFile(unit.getWriter().getTableId(), unit.getUserId(),
                            unit.getWriter().getColumns().stream()
                                    .map(DataTransJobDetail.Column::getRealName).collect(Collectors.toList()));
                    dmcTableApi.viewCascade(unit.getUserId(), Lists.newArrayList(unit.getWriter().getRealName()));
//                    /* 更新表状态 */
//                    dmcTableApi.updateTableStatus(unit.getWriter().getTableId(), 1, unit.getUserId());

                    TassadarResult<MergeTbResp> merge = dmcTableApi.mergeTb(unit.getWriter().getTableId(), unit.getUserId());
                } else {

                    // 删除导出时生成的临时文件
                    DmcTableApi dmcTableApi = DmcApiFactory.getDmcTableApi(unit.getFromSink().getUrl());
                    dmcTableApi.deleteOldData(unit.getReader().getRealName(), unit.getJobId());
                }

                if (unit.getReader().getSync() != null
                        && unit.getReader().getSync().getSyncCondition() != null
                        && unit.getReader().getSync().getSyncCondition().getEnd() != null) {
                    nextIncrValue = unit.getReader().getSync().getSyncCondition().getEnd().getValue();
                }

                String tableName = unit.getReader().getTableName();
                getExecCount(tableName).setUpdateCount(0);
                getExecCount(tableName).setAllCount(0);
                getExecCount(tableName).setDeleteCount(0);
                getExecCount(tableName).setAppendCount(getExecCount(tableName).getAppendCount() + unit.getReadRecords());
                getExecCount(tableName).setFilterCount(getExecCount(tableName).getFilterCount() + unit.getWriteRecords());
                getExecCount(tableName).setFailedCount(getExecCount(tableName).getFailedCount()
                        + (unit.getReadRecords() - unit.getWriteRecords()));
            } catch (Exception e) {
                success = false;
                errorMsg = "数据同步完成，合表失败.";
                log.error(errorMsg, e);
            }
        }

        if (success) {
            JobUtils.cntx().addSuccess();
        }

        databridgeClient.updateJobExecUnit(JobUnitStateForm.builder()
                .jobId(unit.getJobId()).jobTaskId(JobUtils.cntx().getJobTaskId())
                .fromTableId(unit.getReader().getTableId())
                .toTableId(unit.getWriter().getTableId())
                .startTime(unitStartTime.get().get(unit.getReader().getTableName()))
                .endTime(new Date().getTime())
                .jobType(unit.getJobType())
                .tableStatus(success ? 2 : 1)
                .increateValue(nextIncrValue)
                .userId(unit.getUserId())
                .appendCount(unit.getWriteRecords())
                .filterCount(unit.getReadRecords())
                .failedCount(unit.getReadRecords() - unit.getWriteRecords())
                .allCount(0)
                .updateCount(0)
                .deleteCount(0)
                .errorMsg(errorMsg)
                .build());
    }

    @Override
    protected String cancel(FlinkActionParam unit) {
        if (!ObjectUtils.isEmpty(unit.getTaskId())) {
            try {
                flinkxClient.stopJob(unit.getTaskId());
            } catch (Exception e) {
                log.error("flink task stop failed: ", e);
            }
        }

        return null;
    }

    @Override
    protected List<FlinkActionParam> getExecUnitList(DataTransJobDetail jobDetail) {
        return jobDetail.getSyncUnits().stream().map(syncUnit -> FlinkActionParam.builder()
                    .fromSink(syncUnit.getFromSink())
                    .toSink(syncUnit.getToSink())
                    .reader(syncUnit.getReader())
                    .writer(syncUnit.getWriter())
                    .jobId(jobDetail.getJobId())
                    .jobType(jobDetail.getJobType())
                    .userId(StringUtils.isEmpty(syncUnit.getUserId()) ? jobDetail.getUserId() : syncUnit.getUserId())
                    .build()).collect(Collectors.toList());
    }

    @Override
    public DataTransJobDetail getJobDetail(DataTransJobDetail jobParam) {
        return jobParam;
    }
}


