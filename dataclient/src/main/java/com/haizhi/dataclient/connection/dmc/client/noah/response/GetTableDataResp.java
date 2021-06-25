// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.noah.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTableDataResp {
    private String status;
    private TableData result;
    private String trace_id;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TableData {
        private List<Map<String, String>> fields;
        private List<List<String>> data;
    }
}
