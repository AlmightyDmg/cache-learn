package com.haizhi.databridge.bean.dto;

import java.util.List;
import java.util.Map;

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

public final class DataTableDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class SyncConfigDto {
		private String type;
		private String model;
		private String ref;
		@JsonProperty("output_ref")
		private String outputRef;
		private Integer rows;
		private List<String> keys;
		private List<String> fields;
		@JsonProperty("auto_fields")
		private Integer autoFields;
		private Integer dereplication;
		private Integer clean;
		private String sql;
		private Object blobfield;
		private DataTableDto.IncreaseDto increase;
		private DataTableDto.FilterDto filter;
		// 日期可以选择格式化,默认yyyy/MM/dd HH/mm/ss
		private Map<String, DataTableDto.FieldDtoatterDto> formatter;

	}

	@Data
	public static class IncreaseDto {
		private String field;
		private String type;
		private DataTableDto.MaximumDto maximum;

	}

	@Data
	public static class MaximumDto {
		private DataTableDto.StartDto start;
		private DataTableDto.EndDto end;
	}

	@Data
	public static class StartDto {
		private String compare;
		private String enable;
		private String value;
	}

	@Data
	public static class EndDto {
		private Boolean enable;
		private String type;
		private String mode;
		private Integer value;
	}

	@Data
	public static class FilterDto {
		private List<DataTableDto.FieldFilterDto> list;
		private String type;
	}

	@Data
	public static class FieldFilterDto {
		private String type;
		private String name;
		private String value;
	}

	@Data
	public static class FieldDtoatterDto {
		private String fmt;
		private String type;
	}
}
