package com.haizhi.dataio.job.action;

import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataio.bean.OldDtsParam;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:39:40
 */
@Component
public class ImportAction implements IAction<OldDtsParam> {
    @Override
    public void doAction(OldDtsParam param) {
        DmcConfig dmcConfig = JsonUtils.toObject(param.getEndpoint(), DmcConfig.class);
        DmcApiFactory.getDmcJobApi(dmcConfig).startImportJob(param.getUserId(),
                param.getJobId(), param.getTables(), param.getFull());
    }
}
