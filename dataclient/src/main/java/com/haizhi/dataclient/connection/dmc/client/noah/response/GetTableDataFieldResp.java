// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.noah.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTableDataFieldResp {
    private String status;
    private TableDataField result;
    private String message;
    private String trace_id;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Field {
        private String remark;
        private String name;
        private String raw_type;
        private boolean uniq_index;
        private boolean query_index;
        private int position;
        private String type;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TableDataField {
        private List<Field> fields;
        private List<List<String>> data;
    }
}
