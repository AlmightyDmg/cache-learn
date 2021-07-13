package com.haizhi.dataio.client.flinkx.response;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
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
        List<Vertice> vertices;
    }

    @Data
    public static class Vertice {
        String id;
        String name;

        @JsonProperty("read-bytes")
        Long readBytes;
        @JsonProperty("read-bytes-complete")
        Boolean readBytesComplete;

        @JsonProperty("write-bytes")
        Long writeBytes;
        @JsonProperty("write-bytes-complete")
        Boolean writeBytesComplete;

        @JsonProperty("read-records")
        Long readRecords;
        @JsonProperty("read-records-complete")
        Boolean readRecordsComplete;

        @JsonProperty("write-records")
        Long writeRecords;
        @JsonProperty("write-records-complete")
        Boolean writeRecordsComplete;
    }
}
