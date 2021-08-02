package com.haizhi.dataclient.datasource.dmc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataclient.connection.dmc.DmcConnection;
import com.haizhi.dataclient.connection.dmc.client.DmcClientUtils;
import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datasource.DataSource;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 11:37:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmcDataSource implements DataSource<DmcConnection> {
    private DmcConfig dmcConfig;
    private static ThreadLocal<DmcConnection> cntThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> hashCodeThreadLocal = new ThreadLocal<>();

    @Override
    public DmcConnection createConnection() {
        Integer cntHashCode = hashCodeThreadLocal.get();
        if (cntHashCode == null || !cntHashCode.equals(dmcConfig.hashCode())) {
            DmcConnection dmcConnection = DmcConnection.builder()
                    .mobiusClient(DmcClientUtils.mobiusClient(dmcConfig.getMobiusProp()))
                    .noahClient(DmcClientUtils.noahClient(dmcConfig.getNoahProp()))
                    .pentagonClient(DmcClientUtils.pentagonClient(dmcConfig.getPentagonProp()))
                    .tassadarClient(DmcClientUtils.tassadarClient(dmcConfig.getTassadarProp()))
                    .pandoraClient(DmcClientUtils.pandoraClient(dmcConfig.getPandoraProp()))
                    .build();

            cntThreadLocal.set(dmcConnection);
            hashCodeThreadLocal.set(dmcConfig.hashCode());

            return dmcConnection;
        } else {
            return cntThreadLocal.get();
        }
    }
}
