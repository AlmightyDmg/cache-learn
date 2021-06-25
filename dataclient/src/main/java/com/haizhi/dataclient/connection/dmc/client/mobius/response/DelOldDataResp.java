package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelOldDataResp {
    Integer status;
    String result;
}
