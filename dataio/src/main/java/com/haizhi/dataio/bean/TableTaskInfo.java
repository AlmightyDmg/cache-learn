package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月20日 16:38:45
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableTaskInfo {
    private String tableName;
    private Long taskId;
}
