package com.haizhi.dataclient.connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:23:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DataConnection {
    String endpoint;
}
