// CHECKSTYLE:OFF
package com.haizhi.dataclient.datapi.dmc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.DbQueryReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.DelOldDataReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetReaderReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.GetWriterReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcReader;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.DmcWriter;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.ExplainResp;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataFieldResp;
import com.haizhi.dataclient.connection.dmc.client.noah.response.GetTableDataResp;
import com.haizhi.dataclient.connection.dmc.client.pentagon.dto.PentagonResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ChangeFolderReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CheckTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.DeleteMapTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.DeleteTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.InfoTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbFileReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ModifyTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbFileResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;
import com.haizhi.dataclient.datapi.DataApi;
import com.haizhi.dataclient.exception.SDKException;
import com.haizhi.dataclient.utils.JsonUtils;
import com.haizhi.dataclient.utils.ObjectUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:10:09
 */
@Data
@NoArgsConstructor
@Slf4j
public class DmcTableApi extends DataApi<DmcConnection> {

    public static Map<Integer, String> fieldTypeMap = new HashMap<>();


    static {
        fieldTypeMap.put(0, "bigint");
        fieldTypeMap.put(1, "double");
        fieldTypeMap.put(2, "string");
        fieldTypeMap.put(3, "date");
    }

    public DmcTableApi(DmcConnection dmcConnection) {
        super(dmcConnection);
    }

    public Integer getDataCount(String whereSql, String storageId) {
        StringBuilder execSql = new StringBuilder();
        execSql.append(String.format("select count(*) from %s", storageId));
        if (!StringUtils.isEmpty(whereSql)) {
            execSql.append(String.format(" where %s", whereSql));
        }
        return Integer.valueOf(getDataConnection().getMobiusClient()
                .query(DbQueryReq.builder().sql(execSql.toString()).build()).getData().get(0).get(0));
    }

    public List<String> jobGetTables(String name, String url, String user, String password,
                                     String dbName, String dbType,
                                     Boolean isSecurity, Boolean isNetSSL, Integer version) {
        log.info(String.format("%s %s %s %s %s %s %s %s %s", name, url,
                user, password, dbName, dbType, isSecurity, isNetSSL, version));
        PentagonResult<List<String>> result = getDataConnection().getPentagonClient()
                .jobGetTables(name, url, user, password, dbName, dbType, isSecurity, isNetSSL, version);
        if (result.getStatus() == 0) {
            return result.getResult();
        }

        throw new SDKException("jobGetTables error");
    }

    public GetTableSchemaResp jobGetTableSchema(String name, String url, String user, String password,
                                                       String dbName, String tbName, String dbType,
                                                       Boolean isSecurity, Boolean isNetSSL, Integer version) {
        log.info(String.format("%s %s %s %s %s %s %s %s %s %s", name, url,
                user, password, dbName, tbName, dbType, isSecurity, isNetSSL, version));
        PentagonResult<GetTableSchemaResp> result = getDataConnection().getPentagonClient()
                .jobGetTableSchema(name, url, user, password, dbName, tbName, dbType, isSecurity, isNetSSL, version);
        if (result.getStatus() == 0) {
            return result.getResult();
        }

        throw new SDKException("jobGetTableSchema error");
    }

    public ExplainResp explain(@Valid @QueryBean QueryExplainReq queryExplainReq) {
        return getDataConnection().getMobiusClient().explain(queryExplainReq);
    }

    public void updateTableStatus(String tableId, Integer status, String userId) {
        getDataConnection().getTassadarClient().modifyTb(ModifyTbReq.builder()
                .tbId(tableId)
                .status(status)
                .userId(userId)
                .build());
    }

    public InfoTbResp getDmcTbInfo(String tableId) {
        return getDataConnection().getTassadarClient()
                .infoTb(InfoTbReq.builder().tbId(tableId).build()).getResult();
    }

    public List<String> createTb(String tableName, List<CreateTbReq.TbField> fields, String userId,
                                 String dbId, String dbName) {
        String folder = getDataConnection().getTassadarClient()
                .createFolderIfNotExist(1, userId, dbName, 1).getResult().getFolder();
        CreateTbReq createTbReq = CreateTbReq.builder()
                .name(tableName).userId(userId).manageType(1).treeType(0)
                .dmcRequest(1).fields(fields).type(2)
                .dbId(dbId)
                .build();
        String tbId = getDataConnection().getTassadarClient().createTb(createTbReq).getResult().getTbId();

        /* 使用真实的UserId */
        getDataConnection().getTassadarClient()
                .changeFolder(ChangeFolderReq.builder().userId(userId).role(0).toFolder(folder).tbId(tbId).dmcRequest(1).build());

        return Arrays.asList(folder, tbId);
    }

    public TassadarResult<MergeTbResp> mergeTb(String tableId, String userId) {
        log.info(String.format("/tb/commit, tableId: %s, userId: %s", tableId, userId));
        MergeTbReq request = MergeTbReq.builder().tbId(tableId).userId(userId).forceMerge(1).build();
        return getDataConnection().getTassadarClient().mergeTb(request);
    }

    public TassadarResult<MergeTbFileResp> mergeTbFile(String tableId, String userId, List<String> fields) {
        log.info(String.format("/tb/commit, tableId: %s, userId: %s", tableId, userId));
        MergeTbFileReq request = MergeTbFileReq.builder().tbId(tableId)
                .userId(userId).separator("\u0001").nullHolder("\\N").fields(fields).build();
        return getDataConnection().getTassadarClient().mergeTbFile(request);
    }

    public PentagonResult<String> viewCascade(String userId, List<String> tbIds) {
        return getDataConnection().getPentagonClient().viewCascade(userId, JsonUtils.toJson(tbIds));
    }


    public GetTableDataFieldResp getTableDataField(String connectId, String ref, String tableId, String userId) {
        return getDataConnection().getNoahClient().getTableDataField(connectId, ref, tableId, userId);
    }

    public DmcReader getDmcReader(GetReaderReq getReaderReq) {
        log.info(String.format("/getDmcReader, jobId: %s, storageId: %s, tmpSql: %s, maxSql: %s", getReaderReq.getJobId(),
                getReaderReq.getStorageId(), getReaderReq.getTmpSql(), getReaderReq.getMaxSql()));
        return JsonUtils.toObject(getDataConnection().getMobiusClient().getDmcReader(getReaderReq).getResult(), DmcReader.class);
    }

    public DmcWriter getDmcWriter(GetWriterReq getWriterReq) {
        log.info(String.format("/getDmcWriter, jobId: %s, storageId: %s, columns: %s", getWriterReq.getJobId(),
                getWriterReq.getStorageId(), JsonUtils.toJson(getWriterReq.getColumn())));
        return JsonUtils.toObject(getDataConnection().getMobiusClient().getDmcWriter(getWriterReq).getResult(), DmcWriter.class);
    }

    public void deleteOldData(String storageId, String jobId) {
        getDataConnection().getMobiusClient().deleteOldData(DelOldDataReq.builder().tbName(storageId).tag(jobId).build());
    }

    public void truncateData(String storageId) {
        getDataConnection().getMobiusClient().tbTruncate(storageId);
    }

    public GetTableDataResp getTableDataQuery(String connectId, String sql, String userId) {
        return getDataConnection().getNoahClient().getTableDataQuery(connectId, sql, userId);
    }

    public void deleteTb(DeleteTbReq deleteTbReq) {
        getDataConnection().getTassadarClient().deleteTb(deleteTbReq);
    }

    public void deleteMapTb(DeleteMapTbReq deleteMapTbReq) {
        getDataConnection().getTassadarClient().deleteMapTb(deleteMapTbReq);
    }

    public boolean checkTableDep(String tbId, String userId) {
        if (getDataConnection().getTassadarClient().checkTb(CheckTbReq.builder().tbId(tbId).build()).getResult()) {
            return true;
        }


        if (getDataConnection().getTassadarClient().standartableInfo(tbId, userId).getResult() != null) {
            return true;
        }


        if (!ObjectUtils.isEmpty(getDataConnection().getPandoraClient()
                .chartGetListByTb(userId, tbId, 0, 1, 0).getResult().getChart())) {
            return true;
        }

        return false;
    }
}
