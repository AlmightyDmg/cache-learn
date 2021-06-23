package com.haizhi.dataclient.connection.dmc.client.mobius.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFolderResp {
    String folder;
    String name;
}
