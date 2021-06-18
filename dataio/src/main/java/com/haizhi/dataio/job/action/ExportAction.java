package com.haizhi.dataio.job.action;

import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataio.bean.OldDtsParam;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:39:31
 */

@Component
public class ExportAction implements IAction<OldDtsParam> {

    @Override
    public void doAction(OldDtsParam actionInfo) {
        DmcConfig dmcConfig = JsonUtils.toObject(actionInfo.getEndpoint(), DmcConfig.class);
        DmcApiFactory.getDmcJobApi(dmcConfig).startExportJob(actionInfo.getJobId());
//        try {
////            Thread.sleep(3000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
