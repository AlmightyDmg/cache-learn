package com.haizhi.databridge.web.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.service.DataSchedulerService;
import com.haizhi.databridge.web.controller.base.BaseController;

/**
* @Description // 数据源相关接口
* @Date 2021/6/3 3:25 下午
* @Author zhaohuanhuan
**/
@RestController
@RequestMapping("/scheduler")
public class DataSchedulerController extends BaseController {

    @Resource
    private DataSchedulerService dataSchedulerService;

    /**
    * @Description // 数据源创建接口
    * @Date 2021/6/3 3:24 下午
    * @param listForm
    * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
    **/
//    @RequestMapping("/list")
//    @ApiOperation("表创建")
//    public DataSchedulerVo.ListVo list(DataSchedulerForm.DataTableListForm listForm) throws Exception {
//        return dataSchedulerService.list(listForm);
//    }



}
