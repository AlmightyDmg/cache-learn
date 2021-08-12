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
 * @createTime 2021年05月27日 10:39:40
 */
@Component
public class ImportAction implements IAction<OldDtsParam> {
    private static final int CHECK_INTERVAL = 10000;
    @Autowired
    private DatabridgeClient databridgeClient;

    @Override
    public void doAction(OldDtsParam param) throws InterruptedException {
        try {
            DmcConfig dmcConfig = JsonUtils.toObject(param.getEndpoint(), DmcConfig.class);
            DmcApiFactory.getDmcJobApi(dmcConfig).startImportJob(param.getUserId(),
                    param.getJobId(), param.getTables(), param.getFull());
            while (true) {
                if (databridgeClient.jobFinished(param.getJobId(), "import")) {
                    break;
                } else {
                    Thread.sleep(CHECK_INTERVAL);
                }
            }
        } catch (InterruptedException e) {
            this.stop(param);
            throw e;
        }
    }

    public void stop(OldDtsParam param) {
        DmcConfig dmcConfig = JsonUtils.toObject(param.getEndpoint(), DmcConfig.class);
        DmcApiFactory.getDmcJobApi(dmcConfig).stopImportJob(param.getUserId(), param.getJobId());
    }
}
