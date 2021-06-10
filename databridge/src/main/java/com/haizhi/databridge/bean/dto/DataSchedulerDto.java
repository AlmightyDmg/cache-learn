package com.haizhi.databridge.bean.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Desc :
 * author : xierong
 * createTime : 2020/11/16
 */

public final class DataSchedulerDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class OptionsDto {
		private List<String> tables;

	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class TimingDto {
		private Origin origin;
		private Boolean enable;
		private String type;
		private String crontab;
		private String minute;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static final class Origin {
		private String minute;
		private String hour;
	}
}
