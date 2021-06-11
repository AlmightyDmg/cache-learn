package com.haizhi.dataclient.datapi.dmc;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
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

    public void startImportJob(String jobId) {
        getDataConnection().getNoahClient().startImportJob(jobId);
    }
}
