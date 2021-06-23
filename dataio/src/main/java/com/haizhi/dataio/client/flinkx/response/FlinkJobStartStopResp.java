package com.haizhi.dataio.client.flinkx.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlinkJobStartStopResp {
    String jobId;
    String status;
}
