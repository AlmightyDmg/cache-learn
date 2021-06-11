package com.haizhi.dataclient.datapi.dmc.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:11:01
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmcTableDetail {
    String tableId;
    String tableName;
    String storageId;
    String status;
    List<DmcColumn> columns;
}
