// CHECKSTYLE:OFF
package com.haizhi.dataclient.connection.dmc.client.pentagon;

import javax.validation.constraints.NotBlank;

import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.haizhi.dataclient.connection.dmc.client.pentagon.dto.PentagonResult;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;


/**
 * todo 新增接口，参数从request 中定义对象
 */
public interface PentagonClient {
	@POST("/job/getTables")
	public PentagonResult<List<String>> jobGetTables(@Query("name") String name,
													 @NotBlank @Query("url") String url,
													 @Query("user") String user,
													 @Query("password") String password,
													 @Query("dbName") String dbName,
													 @Query("dbType") String dbType,
													 @Query("isSecurity") Boolean isSecurity,
													 @Query("isNetSSL") Boolean isNetSSL,
													 @Query("version") Integer version);

	@POST("/job/getTableSchema")
	public PentagonResult<GetTableSchemaResp> jobGetTableSchema(@Query("name") String name,
																@NotBlank @Query("url") String url,
																@Query("user") String user,
																@Query("password") String password,
																@Query("dbName") String dbName,
																@Query("tbName") String tbName,
																@Query("dbType") String dbType,
																@Query("isSecurity") Boolean isSecurity,
																@Query("isNetSSL") Boolean isNetSSL,
																@Query("version") Integer version);

	@POST("/job/startExportJob")
	PentagonResult<String> startExportJob(@Field("jobId") String jobId);
}
