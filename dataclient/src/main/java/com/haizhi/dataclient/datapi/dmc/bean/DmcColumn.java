package com.haizhi.dataclient.datapi.dmc.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年06月05日 15:20:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmcColumn {
    private String name;
    private String type;
    private String desc;
}
