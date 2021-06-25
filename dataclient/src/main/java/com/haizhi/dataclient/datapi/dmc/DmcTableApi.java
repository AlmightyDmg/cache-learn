// CHECKSTYLE:OFF
package com.haizhi.dataclient.datapi.dmc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;
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
import com.haizhi.dataclient.connection.dmc.client.pentagon.dto.PentagonResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ChangeFolderReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.InfoTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbFileReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ModifyTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.InfoTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;
import com.haizhi.dataclient.datapi.DataApi;
import com.haizhi.dataclient.exception.SDKException;
import com.haizhi.dataclient.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:10:09
 */
@Data
@NoArgsConstructor
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

    public boolean checkTableRule(String whereSql, String storageId) {
        String execSql = String.format("select count(*) from %s where %s", storageId, whereSql);
        Integer count = Integer.valueOf(getDataConnection().getMobiusClient()
                .query(DbQueryReq.builder().sql(execSql).build()).getData().get(0).get(0));

        return Integer.valueOf(0).equals(count);
    }

    public List<String> jobGetTables(String name, String url, String user, String password,
                                     String dbName, String dbType,
                                     Boolean isSecurity, Boolean isNetSSL, Integer version) {
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
                .createFolderIfNotExist(1, userId, dbName, userId.substring(7)).getResult().getFolder();
        CreateTbReq createTbReq = CreateTbReq.builder()
                .name(tableName).userId("3f821e3f30fe2055f8c1794c38bfbc54").manageType(1).treeType(0)
                .dmcRequest(1).fields(fields).type(2)
                .dbId(dbId)
                .build();
        String tbId = getDataConnection().getTassadarClient().createTb(createTbReq).getResult().getTbId();

        getDataConnection().getTassadarClient()
                .changeFolder(ChangeFolderReq.builder().userId(userId).role(0).toFolder(folder).tbId(tbId).build());

        return Arrays.asList(folder, tbId);
    }

    public TassadarResult<MergeTbResp> mergeTbFile(String tableId, String userId) {
        MergeTbFileReq request = MergeTbFileReq.builder().tbId(tableId).userId(userId).forceMerge(1).build();
        return getDataConnection().getTassadarClient().mergeTb(request);
    }

    public GetTableDataFieldResp getTableDataField(String connectId, String ref, String tableId, String userId) {
        return getDataConnection().getNoahClient().getTableDataField(connectId, ref, tableId, userId);
    }

    public DmcReader getDmcReader(GetReaderReq getReaderReq) {
        return JsonUtils.toObject(getDataConnection().getMobiusClient().getDmcReader(getReaderReq).getResult(), DmcReader.class);
    }

    public DmcWriter getDmcWriter(GetWriterReq getWriterReq) {

        return JsonUtils.toObject(getDataConnection().getMobiusClient().getDmcWriter(getWriterReq).getResult(), DmcWriter.class);
    }

    public void deleteOldData(String storageId, String jobId) {
        getDataConnection().getMobiusClient().deleteOldData(DelOldDataReq.builder().tbName(storageId).tag(jobId).build());
    }
}
