package com.haizhi.dataclient.datapi;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:06:18
 */
public interface TableApi<TableConfig, TableDetail, ID> {
    void createTable(TableConfig t);
    void modifyTable(TableConfig t);
    TableDetail describeTable(ID tableId);
    void deleteTable(ID tableId);
}
