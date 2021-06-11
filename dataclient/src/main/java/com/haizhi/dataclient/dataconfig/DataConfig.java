package com.haizhi.dataclient.dataconfig;

import com.haizhi.dataclient.datasource.DataSource;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 14:13:10
 */
public interface DataConfig<T extends DataSource> {
    T buildDataSource();
}
