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
    String taskType; // [import, export]
    String jobId;  // 导入schedulerId，导出为jobId
    Integer oldDataTrans;  // [0 gp相关的任务使用flinkx; 1 旧的数据传输]
    String dmcUrl; // 导入为noah的url，导出为pentagon的url
}
