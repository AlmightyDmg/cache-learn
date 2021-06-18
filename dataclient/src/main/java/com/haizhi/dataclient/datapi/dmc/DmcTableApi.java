// CHECKSTYLE:OFF
package com.haizhi.dataclient.datapi.dmc;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.TableCreateReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.CreateTableResp;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.ExplainResp;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.MobiusResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.dto.PentagonResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.CreateTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.InfoTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.MergeTbFileReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.request.ModifyTbReq;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.CreateTbResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.MergeTbFileResp;
import com.haizhi.dataclient.connection.dmc.client.tassadar.response.TassadarResult;
import com.haizhi.dataclient.datapi.DataApi;
import com.haizhi.dataclient.datapi.TableApi;
import com.haizhi.dataclient.datapi.dmc.bean.DmcTableConfig;
import com.haizhi.dataclient.datapi.dmc.bean.DmcTableDetail;
import com.haizhi.dataclient.datapi.dmc.bean.DmcTableId;
import com.haizhi.dataclient.exception.SDKException;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:10:09
 */
@Data
@NoArgsConstructor
public class DmcTableApi extends DataApi<DmcConnection> {

    public DmcTableApi(DmcConnection dmcConnection) {
        super(dmcConnection);
    }

    public boolean checkTableRule() {
        return true;
    }

    public List<String> jobGetTables(String name, String url, String user, String password,
                                     String dbName, String dbType,
                                     Boolean isSecurity, Boolean isNetSSL, Integer version) {
        PentagonResult<List<String>> result = getDataConnection().getPentagonClient()
                .jobGetTables(name, url, user, password, dbName, dbType, isSecurity, isNetSSL, version);
        if (result.getStatus() != 0) {
            return result.getResult();
        }

        throw new SDKException("jobGetTableSchema error");
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

    public String getDmcWriterPath(String tableId, String userId) {
        String storageId = getDataConnection().getTassadarClient()
                .infoTb(InfoTbReq.builder().tbId(tableId).userId(userId).build()).getResult().getStorageId();
        return getDataConnection().getMobiusClient().getDmcWriterPath(storageId).getResult();
    }

    public CreateTbResp createTb(String tableName, String fields, String userId) {
        CreateTbReq createTbReq = CreateTbReq.builder().name(tableName)
                .fields(fields).dmcRequest(0).dataName(tableName).build();
        return getDataConnection().getTassadarClient().createTb(createTbReq).getResult();
    }

    public TassadarResult<MergeTbFileResp> mergeTbFile(String tableId, String userId) {
        MergeTbFileReq request = MergeTbFileReq.builder().tbId(tableId).userId(userId).build();
        return getDataConnection().getTassadarClient().mergeTbFile(request);
    }
}
