package com.haizhi.dataio.job.sql;

import java.util.HashMap;
import java.util.Map;

public final class JdbcTypeMapping {
    private JdbcTypeMapping() { }
    private static Map<String, String> hive2GpTypeMap = new HashMap<>();
    private static Map<String, String> gp2DmcTypeMap = new HashMap<>();

    static {
        hive2GpTypeMap.put("TINYINT", "int2");
        hive2GpTypeMap.put("SMALLINT", "int2");
        hive2GpTypeMap.put("INT", "int4");
        hive2GpTypeMap.put("INTEGER", "int4");
        hive2GpTypeMap.put("BIGINT", "int8");
        hive2GpTypeMap.put("LONG", "int8");
        hive2GpTypeMap.put("BOOLEAN", "boolean");
        hive2GpTypeMap.put("FLOAT", "float4");
        hive2GpTypeMap.put("DOUBLE", "float8");
        hive2GpTypeMap.put("STRING", "text");
        hive2GpTypeMap.put("BINARY", "bytea");
        hive2GpTypeMap.put("TIMESTAMP", "timestamp");
        hive2GpTypeMap.put("DECIMAL", "decimal");
        hive2GpTypeMap.put("CHAR", "character");
        hive2GpTypeMap.put("VARCHAR", "text");
        hive2GpTypeMap.put("DATE", "timestamp");
    }

    static {
        gp2DmcTypeMap.put("int2", "number");
        gp2DmcTypeMap.put("int4", "number");
        gp2DmcTypeMap.put("int8", "number");
        gp2DmcTypeMap.put("boolean", "number");
        gp2DmcTypeMap.put("float4", "double");
        gp2DmcTypeMap.put("float8", "double");
        gp2DmcTypeMap.put("text", "string");
        gp2DmcTypeMap.put("bytea", "float4");
        gp2DmcTypeMap.put("timestamp", "date");
        gp2DmcTypeMap.put("decimal", "double");
        gp2DmcTypeMap.put("character", "string");
        gp2DmcTypeMap.put("time", "date");
        gp2DmcTypeMap.put("numeric", "number");
        gp2DmcTypeMap.put("real", "double");
        gp2DmcTypeMap.put("date", "date");


        gp2DmcTypeMap.put("DATETIME", "date");
        gp2DmcTypeMap.put("DATETIME2", "date");
        gp2DmcTypeMap.put("DATE", "date");
        gp2DmcTypeMap.put("TIMESTAMP(6)", "date");
        gp2DmcTypeMap.put("TIMESTAMP", "date");
        gp2DmcTypeMap.put("SMALLDATETIME", "date");

        gp2DmcTypeMap.put("INT", "number");
        gp2DmcTypeMap.put("TINYINT", "number");
        gp2DmcTypeMap.put("SMALLINT", "number");
        gp2DmcTypeMap.put("BIGINT", "number");
        gp2DmcTypeMap.put("FLOAT", "number");
        gp2DmcTypeMap.put("DOUBLE", "number");
        gp2DmcTypeMap.put("DECIMAL", "number");
        gp2DmcTypeMap.put("NUMBER", "number");
        gp2DmcTypeMap.put("LONG", "number");
        gp2DmcTypeMap.put("MEDIUMINT", "number");
        gp2DmcTypeMap.put("NUMERIC", "number");
        gp2DmcTypeMap.put("REAL", "number");
        gp2DmcTypeMap.put("MONEY", "number");
        gp2DmcTypeMap.put("BINARY_FLOAT", "number");
        gp2DmcTypeMap.put("BINARY_DOUBLE", "number");

        gp2DmcTypeMap.put("CHAR", "string");
        gp2DmcTypeMap.put("VARCHAR", "string");
        gp2DmcTypeMap.put("LONGTEXT", "string");
        gp2DmcTypeMap.put("TINYTEXT", "string");
        gp2DmcTypeMap.put("MEDIUMTEXT", "string");
        gp2DmcTypeMap.put("NCHAR", "string");
        gp2DmcTypeMap.put("NVCHAR2", "string");
        gp2DmcTypeMap.put("VARCHAR2", "string");
        
        // for bdp  
        gp2DmcTypeMap.put("1", "number");
        gp2DmcTypeMap.put("2", "string");
        gp2DmcTypeMap.put("3", "date");
        gp2DmcTypeMap.put("4", "blob");

        // for firebird database  
        gp2DmcTypeMap.put("7", "number");
        gp2DmcTypeMap.put("8", "number");
        gp2DmcTypeMap.put("9", "number");
        gp2DmcTypeMap.put("10", "number");
        gp2DmcTypeMap.put("11", "number");
        gp2DmcTypeMap.put("12", "date");
        gp2DmcTypeMap.put("13", "date");
        gp2DmcTypeMap.put("14", "string");
        gp2DmcTypeMap.put("16", "number");
        gp2DmcTypeMap.put("27", "number");
        gp2DmcTypeMap.put("35", "date");
        gp2DmcTypeMap.put("37", "string");
        gp2DmcTypeMap.put("40", "string");
        // for ms access database 
        gp2DmcTypeMap.put("COUNTER", "number");
        gp2DmcTypeMap.put("INTEGER", "number");
        gp2DmcTypeMap.put("LONGCHAR", "string");
        gp2DmcTypeMap.put("CURRENCY", "number");
        gp2DmcTypeMap.put("GUID", "string");
                
        // for sqlite             
        gp2DmcTypeMap.put("DOUBL", "number");
        gp2DmcTypeMap.put("UNSIGNED BIG INT", "number");
        gp2DmcTypeMap.put("INT2", "number");
        gp2DmcTypeMap.put("INT8", "number");

        // for postgresql         
        gp2DmcTypeMap.put("TIMESTAMP WITHOUT TIME ZONE", "date");
        gp2DmcTypeMap.put("INT4", "number");
        gp2DmcTypeMap.put("FLOAT4", "number");
        gp2DmcTypeMap.put("FLOAT8", "number");
        
        // for db2                
        gp2DmcTypeMap.put("TIMESTMP", "date");

       // for sybase             
        gp2DmcTypeMap.put("UBIGINT", "number");
        gp2DmcTypeMap.put("USMALLINT", "number");
        gp2DmcTypeMap.put("UINT", "number");

       // ##### for variety blob 
        // mysql                  
        gp2DmcTypeMap.put("TINYBLOB", "blob");
        gp2DmcTypeMap.put("BLOB", "blob");
        gp2DmcTypeMap.put("MEDIUMBLOB", "blob");
        gp2DmcTypeMap.put("LONGBLOB", "blob");
        
        // mssql sqlserver, sybase
        gp2DmcTypeMap.put("IMAGE", "blob");
        
        // MS Access              
        gp2DmcTypeMap.put("OLE OBJECTS", "blob");
        
        // sql lite
        gp2DmcTypeMap.put("GENERAL", "blob");
        // postgresql             
        gp2DmcTypeMap.put("BYTEA", "blob");

        gp2DmcTypeMap.put("BINARY", "blob");
        gp2DmcTypeMap.put("VARBINARY", "blob");

    }



    public static String getGpType(String hiveType) {
        return hive2GpTypeMap.getOrDefault(hiveType.toUpperCase(), "text");
    }

    public static String getDmcType(String jdbcType) {
        return gp2DmcTypeMap.getOrDefault(jdbcType.toUpperCase().split("[\\( ]")[0], "string");
    }
}
