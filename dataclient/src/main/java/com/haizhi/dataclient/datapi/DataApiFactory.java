package com.haizhi.dataclient.datapi;

import lombok.extern.slf4j.Slf4j;

import com.haizhi.dataclient.dataconfig.DataConfig;
import com.haizhi.dataclient.exception.SDKException;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 14:11:36
 */
@Slf4j
public final class DataApiFactory {
    private DataApiFactory() {

    }

    public static <API extends DataApi, CONFIG extends DataConfig> API getApi(CONFIG config, Class<API> apiClazz) {
        try {
            API api = apiClazz.newInstance();
            api.setDataConnection(config.buildDataSource().createConnection());
            return api;
        } catch (Exception e) {
            log.error("create connection error.", e);
            throw new SDKException("create connection error.");
        }
    }
}
