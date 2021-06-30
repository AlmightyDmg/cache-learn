package com.haizhi.databridge.web.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.service.DataSchedulerService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.result.WebResult;

/**
 * @Description // 数据源相关接口
 * @Date 2021/6/3 3:25 下午
 * @Author zhaohuanhuan
 **/
@RestController
@RequestMapping("/api/scheduler")
public class DataSchedulerController extends BaseController {

    @Resource
    private DataSchedulerService dataSchedulerService;

    /**
     * @Description // 数据源创建接口
     * @Date 2021/6/3 3:24 下午
     * @param listForm
     * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
     **/
    @RequestMapping("/list")
    @ApiOperation("列表展示")
    WebResult<DataSchedulerVo.ListVo> list(DataSchedulerForm.ListForm listForm) throws IOException {
        return WebResult.of(dataSchedulerService.list(listForm));
    }

    @RequestMapping("/retrieve")
    @ApiOperation("列表展示")
    WebResult<DataSchedulerVo.RetrieveVo> retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException {
        return WebResult.of(dataSchedulerService.retrieve(retrieveForm));
    }

    @RequestMapping("/update")
    @ApiOperation("更新")
    void update(DataSchedulerForm.UpdateForm updateForm) throws UnsupportedEncodingException {
        dataSchedulerService.update(updateForm);
    }

    @RequestMapping("/delete")
    @ApiOperation("删除")
    void delete(DataSchedulerForm.DeleteForm deleteForm) {
        dataSchedulerService.delete(deleteForm);
    }

    @RequestMapping("/create")
    @ApiOperation("创建")
    WebResult<String> delete(DataSchedulerForm.CreateForm createForm) throws UnsupportedEncodingException {
        return WebResult.of(dataSchedulerService.create(createForm));
    }

    @RequestMapping("/trigger")
    @ApiOperation("触发任务")
    void trigger(DataSchedulerForm.TriggerForm triggerForm) throws UnsupportedEncodingException {
        dataSchedulerService.trigger(triggerForm);
    }

    @RequestMapping("/start")
    @ApiOperation("开始任务")
    void start(DataSchedulerForm.TriggerForm triggerForm) throws UnsupportedEncodingException {
        dataSchedulerService.trigger(triggerForm);
    }

    @RequestMapping("/stop")
    @ApiOperation("停止任务")
    void stop(DataSchedulerForm.StopForm stopForm) throws UnsupportedEncodingException {
        dataSchedulerService.stop(stopForm);
    }

}
