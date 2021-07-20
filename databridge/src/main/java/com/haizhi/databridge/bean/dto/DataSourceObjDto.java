package com.haizhi.databridge.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc :
 * author : xierong
 * createTime : 2020/11/16
 */

public final class DataSourceObjDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class SetUp {
//		private String output;
		private String type;
		private String uid;
		@JsonProperty("conn_str")
		private String connStr;
		private String server;

		@JsonProperty("access_id")
		private String accessId;
		@JsonProperty("access_key")
		private String accessKey;
		private String project;
		private String endpoint;
		private String pwd;
		private Integer port;


		@JsonProperty("pg_database")
		private String pgDatabase;

		private Boolean crypter;
		private String sid;
		private String subtype;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class Options {

		@JsonProperty("table_comments")
		private Integer tableComments;

		@JsonProperty("field_comments")
		private Integer fieldComments;

		private Output output;

		private Object labels;

		@JsonProperty("real_user")
		private String realUser;

		@JsonProperty("is_dmc")
		private Integer isDmc;

		@JsonProperty("ds_id")
		private String dsId;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class Output {

		private String type;
		private String database;

	}
}
