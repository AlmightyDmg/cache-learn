package com.haizhi.databridge.service;

import java.io.IOException;
import java.util.List;

import com.haizhi.databridge.bean.vo.DataTableVo;
import com.haizhi.databridge.web.controller.form.DataTableForm;

public interface DataTableService {

	/**
	 * @Description // 数据表创建接口
	 * @Date 2021/6/3 3:24 下午
	 * @param dataTableCreateForm
	 * @return com.haizhi.databridge.bean.vo.DataBaseSourceVo.CreateVo
	 **/
	DataTableVo.CreateVo create(DataTableForm.DataTableCreateForm dataTableCreateForm) throws IOException;
	DataTableVo.RetrieveVo retrieve(DataTableForm.DataTableRetrieveForm dataTableRetrieveForm) throws IOException;
	List<DataTableVo.RetrieveVo> listRetrieve(DataTableForm.DataTableListRetrieveForm dataTableListRetrieveForm) throws IOException;
	void update(DataTableForm.DataTableUpdateForm updateForm) throws IOException;
	void dependency(DataTableForm.DataTableDependencyForm dependencyForm) throws IOException;
	void listUpdate(DataTableForm.DataTableListUpdateForm listUpdateForm) throws IOException;
	DataTableVo.StatisticsVo statistics(DataTableForm.DataTableStatisticsForm statisticsForm) throws IOException;
//	DataTableVo.StatisticsVo all(DataTableForm.DataTableStatisticsForm statisticsForm) throws IOException;
}
