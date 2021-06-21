package com.haizhi.databridge.constants;

public final class DatabridgeConstant {
    private DatabridgeConstant() {

    }

    public static final String REQUEST_URI = "request_uri";

    public static final String TRACE_ID = "trace_id";
    public static final String USER_ID = "user_id";

    public static final String DMC_REQUEST = "dmc_request";
    // 来自DMC请求
    public static final String DMC_REQUEST_DMC = "1";
    // 来自非DMC请求
    public static final String DMC_REQUEST_NOT_DMC = "0";
    public static final String ENTERPRISE_USER = "domain";

    public static final String ENTERPRISE_ID = "enterprise_id";
}
