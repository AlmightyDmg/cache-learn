package com.haizhi.dataclient.datapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.haizhi.dataclient.connection.DataConnection;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月07日 14:14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataApi<T extends DataConnection> {
    private T dataConnection;
}
