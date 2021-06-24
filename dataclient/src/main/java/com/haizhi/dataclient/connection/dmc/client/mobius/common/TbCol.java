package com.haizhi.dataclient.connection.dmc.client.mobius.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TbCol {
    String name;
    String index;
    String type;
}
