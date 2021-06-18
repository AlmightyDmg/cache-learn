package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月27日 10:19:31
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataTransJobParam extends JobParam {
    String jobType; // [import, export]
    String jobId;  // 导入schedulerId，导出为jobId
}
