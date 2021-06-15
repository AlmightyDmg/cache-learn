package com.haizhi.dataio.job.action;

import org.springframework.stereotype.Component;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.dmc.DmcApiFactory;
import com.haizhi.dataio.bean.DataTransJobParam;
import com.haizhi.dataio.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:39:40
 */
@Component
public class ImportAction implements IAction<DataTransJobParam> {
    @Override
    public void doAction(DataTransJobParam actionInfo) {
        DmcConfig dmcConfig = JsonUtils.toObject(actionInfo.getDmcUrl(), DmcConfig.class);
        DmcApiFactory.getDmcJobApi(dmcConfig).startImportJob(actionInfo.getJobId());
    }
}
