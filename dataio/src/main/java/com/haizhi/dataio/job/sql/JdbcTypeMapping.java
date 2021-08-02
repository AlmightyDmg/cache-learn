package com.haizhi.dataio.job.sql;

import java.util.HashMap;
import java.util.Map;

public class JdbcTypeMapping {
    private JdbcTypeMapping() { }
    private static Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("TINYINT", "int2");
        typeMap.put("SMALLINT", "int2");
        typeMap.put("INT", "int4");
        typeMap.put("INTEGER", "int4");
        typeMap.put("BIGINT", "int8");
        typeMap.put("LONG", "int8");
        typeMap.put("BOOLEAN", "boolean");
        typeMap.put("FLOAT", "float4");
        typeMap.put("DOUBLE", "float8");
        typeMap.put("STRING", "text");
        typeMap.put("BINARY", "bytea");
        typeMap.put("TIMESTAMP", "timestamp");
        typeMap.put("DECIMAL", "decimal");
        typeMap.put("CHAR", "character");
        typeMap.put("VARCHAR", "text");
        typeMap.put("DATE", "timestamp");
    }

    public static String getType(String hiveType) {
        return typeMap.getOrDefault(hiveType.toUpperCase(), "text");
    }
}
