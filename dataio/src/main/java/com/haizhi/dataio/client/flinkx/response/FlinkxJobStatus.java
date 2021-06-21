package com.haizhi.dataio.client.flinkx.response;


import lombok.Data;

@Data
public class FlinkxJobStatus {
    String jobId;
    Status status;

    @Data
    public static class Status {
        String state;
        Long startTime;
        Long endTime;
        Long duration;
    }
}
