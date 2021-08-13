package com.haizhi.dataio.job.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataio.bean.OldDtsParam;
import com.haizhi.dataio.client.databridge.DatabridgeClient;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:39:31
 */

@Component
public class ExportAction implements IAction<OldDtsParam> {
    private static final int CHECK_INTERVAL = 10000;
    @Autowired
    private DatabridgeClient databridgeClient;

    @Override
    public void doAction(OldDtsParam actionInfo) throws InterruptedException {
        try {
            DmcConfig dmcConfig = JsonUtils.toObject(actionInfo.getEndpoint(), DmcConfig.class);
            DmcApiFactory.getDmcJobApi(dmcConfig).startExportJob(actionInfo.getJobId());
            Thread.sleep(CHECK_INTERVAL);
            while (true) {
                if (databridgeClient.jobFinished(actionInfo.getJobId(), "export")) {
                    break;
                } else {
                    Thread.sleep(CHECK_INTERVAL);
                }
            }
        } catch (InterruptedException e) {
            this.stop(actionInfo);
            throw e;
        }
    }

    public void stop(OldDtsParam actionInfo) {
        DmcConfig dmcConfig = JsonUtils.toObject(actionInfo.getEndpoint(), DmcConfig.class);
        DmcApiFactory.getDmcJobApi(dmcConfig).stopExportJob(actionInfo.getJobId());
    }
}
