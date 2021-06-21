package com.haizhi.dataclient.connection.dmc.client.tassadar.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoTbResp {
	private Long id;
	@JsonProperty("tb_id")
	private String tbId;

	private List<TbField> fields;
	@JsonProperty("db_id")
	private String dbId;
	@JsonProperty("storage_id")
	private String storageId;
	@JsonProperty("data_count")
	private Long dataCount;
	@JsonProperty("cache_in_mobius")
	private Integer cacheInMobius;
	private String owner;
	@JsonProperty("ent_id")
	private String entId;
	private String name;
	private String title;
	private Integer type;
	@JsonProperty("commit_limit")
	private Integer commitLimit;
	@JsonProperty("update_mode")
	private Integer updateMode;
	@JsonProperty("force_merge")
	private Integer forceMerge;
	private String partition;
	private Integer status;
	@JsonProperty("error_msg")
	private String errorMsg;
	private Integer version;
	@JsonProperty("update_version")
	private Integer updateVersion;
	@JsonProperty("iVersion")
	private Integer iVersion;
	@JsonProperty("data_utime")
	private String dataUtime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("data_size")
	private Long dataSize;
	@JsonProperty("patch_size")
	private Long patchSize;
	@JsonProperty("repartition_size")
	private Long repartitionSize;
	private String usage;
	private String tag;
	@JsonProperty("special_status")
	private Integer specialStatus;
	private String label;
	private String comment;
	@JsonProperty("is_visible")
	private Integer isVisible;
	@JsonProperty("type_fixed")
	private Integer typeFixed;
	@JsonProperty("raw_size")
	private Long rawSize;
	@JsonProperty("is_etl_tb")
	private Integer isEtlTb;
	@JsonProperty("etl_visible")
	private Integer etlVisible;
	@JsonProperty("url_config")
	private String urlConfig;
	@JsonProperty("manage_type")
	private Integer manageType;
	@JsonProperty("row_filter")
	private List<String> rowFilter;
	@JsonProperty("ctime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String ctime;
	@JsonProperty("utime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private String utime;
	private List<String> operator;
	@JsonProperty("real_id")
	private Integer realId;
	@JsonProperty("permission_type")
	private Integer permissionType;
	@JsonProperty("can_edit")
	private Integer canEdit;
	private Integer viewType;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class TbField {
		private String remark;
		private String name;
		private String title;

		@JsonProperty("field_id")
		private String fieldId;

		@JsonProperty("real_type")
		private String realType;

		@JsonProperty("seq_no")
		private int seqNo;

		@JsonProperty("uniq_index")
		private int uniqIndex;

		private int type;
		private long id;
		private String ctime;
	}

}
