package com.haizhi.dataclient.connection.dmc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.haizhi.dataclient.connection.DataConnection;
import com.haizhi.dataclient.connection.dmc.client.mobius.MobiusClient;
import com.haizhi.dataclient.connection.dmc.client.noah.NoahClient;
import com.haizhi.dataclient.connection.dmc.client.pentagon.PentagonClient;
import com.haizhi.dataclient.connection.dmc.client.tassadar.TassadarClient;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:26:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DmcConnection extends DataConnection {
    private MobiusClient mobiusClient;
    private NoahClient noahClient;
    private PentagonClient pentagonClient;
    private TassadarClient tassadarClient;
}
