package com.haizhi.databridge.web.controller;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.DataBaseSourceVo;
import com.haizhi.databridge.service.DataSourceService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.DataSourceForm;

/**
* @Description // 数据源相关接口
* @Date 2021/6/3 3:25 下午
* @Author zhaohuanhuan
**/
@RestController
@RequestMapping("/api/datasource")
public class DataSourceController extends BaseController {

    @Resource
    private DataSourceService dataSourceService;

    /**
    * @Description // 数据源创建接口
    * @Date 2021/6/3 3:24 下午
    * @param dataSourceCreateForm
    * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
    **/
    @RequestMapping("/create")
    @ApiOperation("创建用户信息")
    public DataBaseSourceVo.CreateVo create(DataSourceForm.DataSourceCreateForm dataSourceCreateForm) throws Exception {
        return dataSourceService.create(dataSourceCreateForm);
    }

    /**
    * @Description // 数据源删除
    * @Date 2021/6/3 3:24 下午
    * @param dbId
    * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.DeleteVo
    **/
    @RequestMapping("/delete")
    @ApiOperation("创建用户信息")
    public DataBaseSourceVo.DeleteVo create(String dbId) throws Exception {
        return dataSourceService.delete(dbId);
    }

    /**
    * @Description // 数据源更新
    * @Date 2021/6/3 3:27 下午
    * @param dataSourceUpdateForm
    * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.UpdateVo
    **/
    @RequestMapping("/update")
    @ApiOperation("创建用户信息")
    public DataBaseSourceVo.UpdateVo update(DataSourceForm.DataSourceUpdateForm dataSourceUpdateForm) throws Exception {
        return dataSourceService.update(dataSourceUpdateForm);
    }

    /**
    * @Description // 数据源展示
    * @Date 2021/6/3 3:27 下午
    * @param dataSourceRetrieveForm
    * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.RetrieveVo
    **/
    @RequestMapping("/retrieve")
    @ApiOperation("创建用户信息")
    public DataBaseSourceVo.RetrieveVo retrieve(DataSourceForm.DataSourceRetrieveForm dataSourceRetrieveForm) throws UnsupportedEncodingException {
        return dataSourceService.retrieve(dataSourceRetrieveForm);
    }

    @RequestMapping("/status")
    @ApiOperation("创建用户信息")
    public DataBaseSourceVo.DataSourceStatusVo status(DataSourceForm.DataSourceStatusForm sourceStatusForm) throws UnsupportedEncodingException {
        return dataSourceService.status(sourceStatusForm);
    }
}
