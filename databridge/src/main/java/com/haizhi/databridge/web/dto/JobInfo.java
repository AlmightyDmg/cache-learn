package com.haizhi.databridge.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfo {
    int id;				// 主键ID
    int jobGroup;		// 执行器主键ID
    String jobDesc;
    String author;		// 负责人
    String alarmEmail;	// 报警邮件
    String scheduleType;			// 调度类型
    String scheduleConf;			// 调度配置，值含义取决于调度类型
    String misfireStrategy;			// 调度过期策略
    String executorRouteStrategy;	// 执行器路由策略
    String executorHandler;		    // 执行器，任务Handler名称
    String executorParam;		    // 执行器，任务参数
    String executorBlockStrategy;	// 阻塞处理策略
    int executorTimeout;     		// 任务执行超时时间，单位秒
    int executorFailRetryCount;		// 失败重试次数
    String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
    String glueSource;		// GLUE源代码
    String glueRemark;		// GLUE备注
    String childJobId;	// 子任务ID，多个逗号分隔
}
