package com.haizhi.databridge.bean.po;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FieldNewBean {
	private String tbId;
	private String fid;
	private String newFieldName;
	private String formula;
	private String userId;
	private List groups;
	private String dataType;
	private Map<String, String> param;
	private String flag;
	private String isDisplay;
	private String queryForCt;
	private Integer needParam;
}
