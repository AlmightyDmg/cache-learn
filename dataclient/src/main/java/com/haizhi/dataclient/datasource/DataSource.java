package com.haizhi.dataclient.datasource;

import com.haizhi.dataclient.connection.DataConnection;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 11:27:31
 */
public interface DataSource<T extends DataConnection> {
    T createConnection();
}
