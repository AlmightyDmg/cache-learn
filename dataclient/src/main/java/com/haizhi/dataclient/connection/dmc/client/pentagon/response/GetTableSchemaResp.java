package com.haizhi.dataclient.connection.dmc.client.pentagon.response;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GetTableSchemaResp {
	private List<Map<String, Object>> fields;
}


