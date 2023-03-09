package cn.com.dmg.cache;
import java.sql.*;
import java.util.Date;
import java.util.Properties;


public class MyApplication {
    public static Connection connection = null;
    public static Statement statement = null;
    public static void main(String[] args) throws Exception {
        connection = getConnection1();
        statement = connection.createStatement();

        //SELECT * FROM Sample.MyTest
        //testQuery("SELECT \"ID\",\"Age\",\"DOB\",\"FavoriteColors\",\"Name\",\"SSN\",\"Spouse\",\"Home_City\",\"Home_State\",\"Home_Street\",\"Home_Zip\",\"Office_City\",\"Office_State\",\"Office_Street\",\"Office_Zip\",now() as \"BDP_AUDIT\" FROM \"Sample\".\"Person\" WHERE 1=1  AND ( ( \"ID\" > 200  and  (1=1) )  and  \"ID\" <= 202 )\n");
        testQuery("");
        //getAllSchemas();
        //createTable();
        //listTables();
    }

    public static void testQuery(String sql) throws Exception{
        //https://docs.intersystems.com/latest/csp/docbook/DocBook.UI.Page.cls?KEY=GSQL_queries
        if(sql.equals("") || sql == null){
            sql = "SELECT TimeOfBirth FROM Sample.MyTest1";
        }
        ResultSet resultSet = statement.executeQuery(sql);


        while (resultSet.next()){
             Object obj = resultSet.getObject(1);
            //Object timeOfBirth = resultSet.getObject("TimeOfBirth");
            Time timeOfBirth1 = resultSet.getTime("TimeOfBirth");
            //Object obj = resultSet.getObject("TimeOfBirth");

            if(obj instanceof Date){
                if(obj instanceof Time){
                    System.out.printf("这是 time");
                }
            }

            System.out.println(resultSet.getString("TimeOfBirth"));
        }
    }

    public static void getAllSchemas() throws Exception{
        String sql = "SELECT SCHEMA_NAME \n" +
                "FROM INFORMATION_SCHEMA.SCHEMATA WHERE NOT SCHEMA_NAME %STARTSWITH '%'";
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            System.out.println(metaData.getColumnName(i));
        }
    }

    /**
     * https://docs.intersystems.com/latest/csp/docbook/DocBook.UI.Page.cls?KEY=GSQL_tables#GSQL_tables_list
     * @author zhum
     * @date 2022/10/18 17:55
     * @param
     * @return void
     */
    public static void listTables() throws Exception{
        String sql = "SELECT Table_Type,Table_Schema,Table_Name,Owner FROM INFORMATION_SCHEMA.TABLES";
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            System.out.println(metaData.getColumnName(i));
        }
    }


    /**
     * 创建表
     * https://docs.intersystems.com/latest/csp/docbook/DocBook.UI.Page.cls?KEY=GSQL_tables#GSQL_tables_via_classes
     * @author zhum
     * @date 2022/10/18 17:50
     * @param
     * @return void
     */
    public static void createTable() throws Exception{
        String sql = "CREATE TABLE Sample.MyTest1 (\n" +
                "    Name VARCHAR(50) NOT NULL,\n" +
                "    SSN VARCHAR(15) DEFAULT 'Unknown',\n" +
                "    DateOfBirth DATE,\n" +
                "    TimeOfBirth TIME,\n" +
                "    Sex VARCHAR(1)\n" +
                ")";
        statement.execute(sql);
    }


    public static Connection getConnection1(){
        Connection dbconnection = null;
        try {
            //Class.forName ("com.intersys.jdbc.CacheDriver").newInstance();
            String  url="jdbc:Cache://192.168.9.6:1972/Samples";
            java.util.Properties info = new java.util.Properties();

            String  username = "_SYSTEM";
            String  password = "SYS";

            info.put("password", password);
            info.put("user", username);

            dbconnection = DriverManager.getConnection(url, info);
            System.out.println(dbconnection.isValid(10));
        } catch (Exception e){
            e.printStackTrace();
        }
        return dbconnection;
    }

//    public static Connection getConnection2(){
//        Connection dbconnection = null;
//        try{
//            CacheDataSource ds = new CacheDataSource();
//            ds.setURL("jdbc:Cache://127.0.0.1:1972/Samples");
//            ds.setUser("_system");
//            ds.setPassword("SYS");
//            dbconnection = ds.getConnection();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return dbconnection;
//    }

    public static Connection getConnection3(){
        Connection dbconnection = null;
        try {
            String  url="jdbc:Cache://127.0.0.1:1972/SAMPLES";
            java.sql.Driver drv = java.sql.DriverManager.getDriver(url);
            java.util.Properties props = new Properties();
            props.put("user","_system");
            props.put("password","SYS");
            dbconnection = drv.connect(url, props);
        } catch (Exception e){
            e.printStackTrace();
        }
        return dbconnection;
    }

}
