package com.haizhi.databridge.bean.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import com.haizhi.databridge.bean.dto.DataSchedulerDto;

/**
 * 数据库相关
 *
 * @author zhaohuanhuan
 * @create 06 01, 2021
 * @since 1.0.0
 */
public class DataSchedulerVo {
    @Data
    @Builder
    public static final class ListVo {

        private Integer pagecount;
        private QueryVo query;
        private List<SchedulerVo> schedulers;
        private StatusVo status;
        private Integer totalitems;

    }

    @Data
    @Builder
    public static final class QueryVo {

        private String keyword;
        private Integer limit;
        private Integer page;
    }

    @Data
    @Builder
    public static final class SchedulerVo {

        private List<String> database;
        private String exception;
        private Integer fetched;
        @JsonProperty("finish_tb_count")
        private Integer finishTbCount;
        private Integer posted;
        @JsonProperty("scheduler_desc")
        private String schedulerDesc;
        @JsonProperty("scheduler_id")
        private String schedulerId;
        @JsonProperty("scheduler_name")
        private String schedulerName;
        @JsonProperty("start_at")
        private String startAt;
        private String status;
        @JsonProperty("sync_cycle")
        private String syncCycle;
        private List<DataTableVo.TableVo> tables;
        @JsonProperty("tb_count")
        private Integer tbCount;

    }

    @Data
    @Builder
    public static final class StatusVo {
        private Integer total;
    }


    @Data
    @Builder
    public static final class RetrieveVo {
        @JsonProperty("scheduler_id")
        private String schedulerId;
//        private List<String> schema;
        @JsonProperty("next_time")
        private String nextTime;
        private String status;
        private DataSchedulerDto.TimingDto timing;
        @JsonProperty("update_at")
        private String updateAt;
        private List<DataTableVo.RetrieveVo> tables;


    }

}
