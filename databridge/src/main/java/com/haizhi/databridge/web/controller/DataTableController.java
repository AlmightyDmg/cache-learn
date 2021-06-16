package com.haizhi.databridge.web.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.service.DataTableService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.DataTableForm;

/**
 * @Description // 数据源相关接口
 * @Date 2021/6/3 3:25 下午
 * @Author zhaohuanhuan
 **/
@RestController
@RequestMapping("/api/table")
public class DataTableController extends BaseController {

    @Resource
    private DataTableService dataTableService;

    /**
     * @Description // 数据源创建接口
     * @Date 2021/6/3 3:24 下午
     * @param createForm
     * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
     **/
    @RequestMapping("/create")
    @ApiOperation("表创建")
    public DataTableVo.CreateVo create(DataTableForm.DataTableCreateForm createForm) throws Exception {
        return dataTableService.create(createForm);
    }

    /**
     * @Description // 表展示
     * @Date 2021/6/3 3:27 下午
     * @param retrieveForm
     * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.RetrieveVo
     **/
    @RequestMapping("/retrieve")
    @ApiOperation("表展示")
    public DataTableVo.RetrieveVo retrieve(DataTableForm.DataTableRetrieveForm retrieveForm) throws IOException {
        return dataTableService.retrieve(retrieveForm);
    }

    /**
     * @Description // 展示数据源下接入的表
     * @Date 2021/6/7 4:50 下午
     * @param listRetrieveForm
     * @return void
     **/
    @RequestMapping("/list/retrieve")
    @ApiOperation("表修改")
    public List<DataTableVo.RetrieveVo> listRetrieve(DataTableForm.DataTableListRetrieveForm listRetrieveForm) throws IOException {
        return dataTableService.listRetrieve(listRetrieveForm);
    }

    /**
     * @Description // 修改表
     * @Date 2021/6/7 4:30 下午
     * @param updateForm
     * @return void
     **/
    @RequestMapping("/update")
    @ApiOperation("表修改")
    public void update(DataTableForm.DataTableUpdateForm updateForm) throws IOException {
        dataTableService.update(updateForm);
    }

    /**
     * @Description // 校验依赖并删除表
     * @Date 2021/6/7 8:04 下午
     * @param dependencyForm
     * @return void
     **/
    @RequestMapping("/dependency")
    @ApiOperation("表删除")
    public void dependency(DataTableForm.DataTableDependencyForm dependencyForm) throws IOException {
        dataTableService.dependency(dependencyForm);
    }

    @RequestMapping("/list/update")
    @ApiOperation("表修改")
    public void listUpdate(DataTableForm.DataTableListUpdateForm listUpdateForm) throws IOException {
        dataTableService.listUpdate(listUpdateForm);
    }

    @RequestMapping("/statistics")
    @ApiOperation("获取所有表的同步状态")
    public DataTableVo.StatisticsVo statistics(DataTableForm.DataTableStatisticsForm statisticsForm) throws IOException {
        return dataTableService.statistics(statisticsForm);
    }

    // 我觉的这个接口应该放在数据源，这个需要调jdbc驱动
//    @RequestMapping("/all")
//    @ApiOperation("表修改")
//    public DataTableVo.StatisticsVo all(DataTableForm.DataTableStatisticsForm statisticsForm) throws IOException {
//        return dataTableService.all(statisticsForm);
//    }


}
