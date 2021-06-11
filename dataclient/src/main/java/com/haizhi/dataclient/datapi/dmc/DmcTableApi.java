// CHECKSTYLE:OFF
package com.haizhi.dataclient.datapi.dmc;

import java.util.List;

import javax.validation.Valid;

import lombok.Data;
import lombok.NoArgsConstructor;
import retrofit2.http.QueryBean;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.connection.dmc.client.mobius.response.ExplainResp;
import com.haizhi.dataclient.connection.dmc.client.pentagon.dto.PentagonResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;
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
public class DmcTableApi extends DataApi<DmcConnection> implements TableApi<DmcTableConfig, DmcTableDetail, DmcTableId> {

    public DmcTableApi(DmcConnection dmcConnection) {
        super(dmcConnection);
    }

    @Override
    public void createTable(DmcTableConfig t) {

    }

    @Override
    public void modifyTable(DmcTableConfig t) {

    }

    @Override
    public DmcTableDetail describeTable(DmcTableId tableId) {
        return null;
    }

    @Override
    public void deleteTable(DmcTableId tableId) {

    }

    public String createDmcTableIfNotExist(String tableName) {
        return "";
    }

    public String getTableStorePath(String dmcTblId) {
        return "";
    }

    public String commitTbl(String tblStorPath, String dmcTblId) {
        return "";
    }

    public boolean checkTableRule() {
        return true;
    }

    public List<String> jobGetTables(String name, String url, String user, String password,
                                     String dbName, String dbType,
                                     Boolean isSecurity, Boolean isNetSSL, Integer version) {
        PentagonResult<List<String>> result = getDataConnection().getPentagonClient()
                .jobGetTables(name, url, user, password, dbName, dbType, isSecurity, isNetSSL, version);
        if (result.getStatus() != 200) {
            return result.getResult();
        }

        throw new SDKException("jobGetTableSchema error");
    }

    public GetTableSchemaResp jobGetTableSchema(String name, String url, String user, String password,
                                                       String dbName, String tbName, String dbType,
                                                       Boolean isSecurity, Boolean isNetSSL, Integer version) {
        PentagonResult<GetTableSchemaResp> result = getDataConnection().getPentagonClient()
                .jobGetTableSchema(name, url, user, password, dbName, tbName, dbType, isSecurity, isNetSSL, version);
        if (result.getStatus() == 200) {
            return result.getResult();
        }

        throw new SDKException("jobGetTableSchema error");
    }

    public ExplainResp explain(@Valid @QueryBean QueryExplainReq queryExplainReq) {
        return getDataConnection().getMobiusClient().explain(queryExplainReq);
    }

    public void updateTableStatus(String tableId, Integer status) {

    }
}
