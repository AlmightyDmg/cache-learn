package com.haizhi.dataclient.datapi.dmc;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
import com.haizhi.dataclient.connection.dmc.client.noah.request.StartReq;
import com.haizhi.dataclient.connection.dmc.client.noah.request.StopReq;
import com.haizhi.dataclient.datapi.DataApi;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 16:45:56
 */
@Data
@NoArgsConstructor
public class DmcJobApi extends DataApi<DmcConnection> {
    public void startExportJob(String jobId) {
        getDataConnection().getPentagonClient().startExportJob(jobId);
    }

    public void stopExportJob(String jobId) {
        getDataConnection().getPentagonClient().stopExportJob(jobId);
    }

    public void startImportJob(String userId, String jobId, List<String> tables, Integer full) {
        getDataConnection().getNoahClient().startImportJob(StartReq.builder()
                .userId(userId)
                .jobId(jobId)
                .tables(tables)
                .full(full)
                .build());
    }

    public void stopImportJob(String userId, String jobId) {
        getDataConnection().getNoahClient().stopImportJob(StopReq.builder()
                .userId(userId)
                .jobId(jobId)
                .build());
    }
}
