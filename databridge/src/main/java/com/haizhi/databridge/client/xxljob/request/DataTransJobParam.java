package com.haizhi.databridge.client.xxljob.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@ToString(callSuper = true)
public class DataTransJobParam {
    String jobType; // [import, export]
    String jobId;  // 导入schedulerId，导出为jobId
    List<String> readerTables;
    Integer full;
}
