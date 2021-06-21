package com.haizhi.dataio.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OldDtsParam {
    String endpoint;
    String jobType; // [import, export]
    String jobId;  // 导入schedulerId，导出为jobId
}
