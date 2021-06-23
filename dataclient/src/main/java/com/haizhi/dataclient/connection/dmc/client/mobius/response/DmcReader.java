package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmcReader {
    private JsonNode reader;
    private String tabName;
    private String maxValue;
}
