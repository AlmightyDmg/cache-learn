package com.haizhi.dataclient.datapi.dmc;

import com.haizhi.dataclient.dataconfig.dmc.DmcConfig;
import com.haizhi.dataclient.datapi.DataApiFactory;
import com.haizhi.dataclient.utils.JsonUtils;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 16:11:27
 */
public final class DmcApiFactory {
    private DmcApiFactory() {

    }

    public static DmcTableApi getDmcTableApi(DmcConfig dmcConfig) {
        return DataApiFactory.getApi(dmcConfig, DmcTableApi.class);
    }

    public static DmcTableApi getDmcTableApi(String dmcConfigStr) {
        DmcConfig dmcConfig = JsonUtils.toObject(dmcConfigStr, DmcConfig.class);
        return DataApiFactory.getApi(dmcConfig, DmcTableApi.class);
    }

    public static DmcJobApi getDmcJobApi(DmcConfig dmcConfig) {
        return DataApiFactory.getApi(dmcConfig, DmcJobApi.class);
    }
}
