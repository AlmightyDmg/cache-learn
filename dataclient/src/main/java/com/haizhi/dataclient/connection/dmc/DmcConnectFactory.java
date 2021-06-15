package com.haizhi.dataclient.connection.dmc;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datasource.dmc.DmcDataSource;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 17:07:38
 */
public final class DmcConnectFactory {
    private DmcConnectFactory() {
    }

    public DmcConnection createConnection(DmcConfig dmcConfig) {
        return new DmcDataSource(dmcConfig).createConnection();
    }
}
