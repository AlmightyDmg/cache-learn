package com.haizhi.databridge.web.controller;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.ExportJobVo;
import com.haizhi.databridge.service.export.ExportJobService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.ExportJobForm;

/**
 * @author zhaohuanhuan
 * @version 1.0
 * @create 12/8/20 3:46 下午
 **/
@Slf4j
@RestController
@RequestMapping("/api/export_job")
@Api(tags = "export_job")
public class ExportJobController extends BaseController {

	@Autowired
	private ExportJobService exportJobService;

	/**
	* @Description //导出任务列表
	* @Date 2021/1/12 1:58 下午
	* @param
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportJobVo.JobInfoVo>
	**/
	@RequestMapping("job/list")
	public List<ExportJobVo.JobInfoVo> jobList() {
		return exportJobService.jobList();
	}

	/**
	* @Description //导出任务创建
	* @Date 2021/1/12 1:59 下午
	* @param form
	* @return void
	**/
	@RequestMapping("job/create")
	public void jobCreate(ExportJobForm.ExportJobCreateForm form) {
		exportJobService.jobCreate(form);
	}

	/**
	* @Description //修改任务
	* @Date 2021/1/12 5:59 下午
	* @param form
	* @return void
	**/
	@RequestMapping("job/modify")
	public void jobModify(ExportJobForm.ExportJobModifyForm form) {
		exportJobService.jobModify(form);
	}

	/**
	* @Description //删除任务
	* @Date 2021/1/12 5:59 下午
	* @param jobId
	* @return void
	**/
	@RequestMapping("job/delete")
	public void jobDelete(@NotNull @NotBlank @ApiParam("job_id") @RequestParam(name = "job_id") String jobId) {
		exportJobService.jobDelete(jobId);
	}
	/**
	* @Description //启动任务，这个启动的意思是把调度任务激活，不仅仅是让这个任务跑一遍
	* @Date 2021/1/12 6:00 下午
	* @param jobId
	* @return void
	**/
	@RequestMapping("job/start")
	public void jobStart(@NotNull @NotBlank @ApiParam("job_id") @RequestParam(name = "job_id") String jobId) {
		exportJobService.jobStart(jobId);
	}

	/**
	* @Description //不是让正在运行的任务cancel掉，而是让这个任务的调度停止
	* @Date 2021/1/12 6:00 下午
	* @param jobId
	* @return void
	**/
	@RequestMapping("job/stop")
	public void jobStop(@NotNull @NotBlank @ApiParam("job_id") @RequestParam(name = "job_id") String jobId) {
		exportJobService.jobStop(jobId);
	}

	/**
	 * @Description //执行任务
	 * @Date 2021/1/12 6:00 下午
	 * @param jobId
	 * @return void
	 **/
	@RequestMapping("job/exec")
	public void jobExec(@NotNull @NotBlank @ApiParam("job_id") @RequestParam(name = "job_id") String jobId,
						@ApiParam("is_auto") @RequestParam(name = "is_auto", defaultValue = "0", required = false) Integer isAuto) {
		exportJobService.jobExec(jobId, isAuto);
	}

	/**
	* @Description //任务的历史记录
	* @Date 2021/1/12 6:01 下午
	* @param jobId
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportJobVo.HistoryVo>
	**/
	@RequestMapping("job/history")
	public List<ExportJobVo.HistoryVo> jobHistory(@NotNull @NotBlank @ApiParam("job_id") @RequestParam(name = "job_id") String jobId) {
		return exportJobService.jobHistory(jobId);
	}

	/**
	* @Description //通过判断前端传过来的状态与数据库中的状态对比，通知前端是否需要刷新列表
	* @Date 2021/1/19 9:34 下午
	* @param jobStatusMap
	* @return boolean
	**/
	@RequestMapping("job/is_refresh")
	public boolean jobStatus(
		@NotNull @NotBlank @ApiParam("job_status_map") @RequestParam(name = "job_status_map") Map<String, Integer> jobStatusMap) {
		return exportJobService.checkJobStatus(jobStatusMap);
	}
}
