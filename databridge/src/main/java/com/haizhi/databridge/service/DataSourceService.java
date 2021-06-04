package com.haizhi.databridge.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.haizhi.databridge.bean.vo.DataBaseSourceVo;
import com.haizhi.databridge.web.controller.form.DataSourceForm;

public interface DataSourceService {

	/**
	 * @Description // 数据源创建接口
	 * @Date 2021/6/3 3:24 下午
	 * @param dataSourceCreateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
	 **/
	DataBaseSourceVo.CreateVo create(DataSourceForm.DataSourceCreateForm dataSourceCreateForm) throws IOException;

	/**
	 * @Description // 数据源删除
	 * @Date 2021/6/3 3:24 下午
	 * @param dbId
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.DeleteVo
	 **/
	DataBaseSourceVo.DeleteVo delete(String dbId) throws IOException;

	/**
	 * @Description // 数据源更新
	 * @Date 2021/6/3 3:27 下午
	 * @param dataSourceUpdateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.UpdateVo
	 **/
	DataBaseSourceVo.UpdateVo update(DataSourceForm.DataSourceUpdateForm dataSourceUpdateForm) throws IOException;

	/**
	 * @Description // 数据源展示
	 * @Date 2021/6/3 3:27 下午
	 * @param dataSourceRetrieveForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.RetrieveVo
	 **/
	DataBaseSourceVo.RetrieveVo retrieve(DataSourceForm.DataSourceRetrieveForm dataSourceRetrieveForm) throws UnsupportedEncodingException;

}
