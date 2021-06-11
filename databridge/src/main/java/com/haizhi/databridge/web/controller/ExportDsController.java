package com.haizhi.databridge.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.vo.ExportDsVo;
import com.haizhi.databridge.service.export.ExportDsService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.ExportDsForm;
import com.haizhi.dataclient.connection.dmc.client.pentagon.response.GetTableSchemaResp;

/**
 * @author zhaohuanhuan
 * @version 1.0
 * @create 12/8/20 3:46 下午
 **/
@Slf4j
@RestController
@RequestMapping("/api/export_ds")
@Api(tags = "export_ds")
public class ExportDsController extends BaseController {

	@Autowired
	private ExportDsService exportDsService;

	/**
	* @Description //创建导出数据源
	* @Date 2020/12/21 11:09 上午
	* @param form
	* @return String dsId
	**/
	@RequestMapping("create")
	public ExportDsVo.DsCreateVo create(ExportDsForm.ExportDsCreateForm form) throws UnsupportedEncodingException {
		return exportDsService.create(form);
	}

	/**
	* @Description //数据源列表展示
	* @Date 2020/12/21 3:03 下午
	* @param form
	* @return com.haizhi.hora.bean.vo.ExportDsVo.DsListRespVo
	**/
	@RequestMapping("list")
	public List<ExportDsVo.ExportDsListVo> list(ExportDsForm.ExportDsListForm form) throws UnsupportedEncodingException {
		return exportDsService.list(form);
	}

	/**
	* @Description //修改导出数据源连接信息
	* @Date 2020/12/21 3:26 下午
	* @param form
	* @return void
	**/
	@RequestMapping("modify")
	public void  modify(ExportDsForm.ExportDsModifyForm form) throws UnsupportedEncodingException {
		exportDsService.modify(form);
	}

	/**
	* @Description //删除数据源
	* @Date 2020/12/22 2:24 下午
	* @param dsId
	* @return void
	**/
	@RequestMapping("delete")
	public void  delete(@NotNull @NotBlank @ApiParam("ds_id") @RequestParam(name = "ds_id") String dsId) {
		exportDsService.delete(dsId);
	}

	/**
	* @Description //获取导出数据表
	* @Date 2020/12/21 4:09 下午
	* @param form
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportDsVo.tableVo>
	**/
	@RequestMapping("table/list")
	public List<ExportDsVo.TableVo> tableList(ExportDsForm.ExportDsTableListForm form) {
		return exportDsService.tableList(form);
	}

	/**
	* @Description // 连接数据源接入的数据库，获取数据库中的表
	* @Date 2020/12/21 5:02 下午
	* @param  form
	* @return void
	**/
	@ApiOperation("获取数据源中的表")
	@RequestMapping("connector/table/list")
	public List<String> getConnectorTableList(ExportDsForm.ConfigForm form) throws UnsupportedEncodingException {
		return exportDsService.getConnectorTableList(form);
	}

	/**
	* @Description //获取目的数据中所有的表
	* @Date 2021/1/12 1:55 下午
	* @param dsId
	* @return java.util.List<java.lang.String>
	**/
	@ApiOperation("获取数据源中的表")
	@RequestMapping("get/connector/tables")
	public List<String> getConnectorTables(@NotNull @NotBlank @ApiParam("ds_id") @RequestParam(name = "ds_id") String dsId) {
		return exportDsService.getConnectorTables(dsId);
	}

	/**
	* @Description //获取目的表中所有的字段
	* @Date 2021/1/12 1:57 下午
	* @param form
	* @return java.lang.Object
	**/
	@ApiOperation("获取数据源中的表")
	@RequestMapping("connector/Schema/list")
	public Object connectorTableSchemaList(ExportDsForm.ConfigForm form) throws UnsupportedEncodingException {
		return exportDsService.getConnectorTableSchemaList(form);
	}

	/**
	* @Description //获取目的表中所有的字段
	* @Date 2021/1/12 1:57 下午
	* @param xtbId
	* @return com.haizhi.sdk.pentagon.response.GetTableSchemaResp
	**/
	@ApiOperation("获取数据源中的表")
	@RequestMapping("field/list")
	public GetTableSchemaResp fieldList(@NotNull @NotBlank @ApiParam("xtb_id") @RequestParam(name = "xtb_id") String xtbId) {
		return exportDsService.fieldList(xtbId);
	}

	/**
	* @Description //更新有权限导出的表
	* @Date 2020/12/21 6:45 下午
	* @param form
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportDsVo.tableVo>
	**/
	@RequestMapping("table/update")
	public List<String> tableUpdate(ExportDsForm.ExportDsTableUpdateForm form) {
		return exportDsService.tableUpdate(form);
	}

	/**
	* @Description //编辑授权
	* @Date 2020/12/22 3:22 下午
	* @param form
	* @return void
	**/
	@RequestMapping("pms/modify")
	public void pmsModify(ExportDsForm.ExportDsPmsCreateForm form) {
		exportDsService.pmsModify(form);
	}

	/**
	* @Description //授权概况
	* @Date 2020/12/22 6:12 下午
	* @param dsId
	* @return com.haizhi.hora.bean.vo.ExportDsVo.ExportTbPmsListVo
	**/
	@RequestMapping("pms/list")
	public ExportDsVo.ExportTbPmsListVo pmsList(@NotNull @NotBlank @ApiParam("ds_id") @RequestParam(name = "ds_id") String dsId) {
		return exportDsService.pmsListByDsId(dsId);
	}

	/**
	* @Description //获取目的表被授权给了哪些角色
	* @Date 2021/1/12 1:51 下午
	* @param dsId
	* @return com.haizhi.hora.bean.vo.ExportDsVo.ExportTbPmsRoleListVo
	**/
	@RequestMapping("pms/role/list")
	public ExportDsVo.ExportTbPmsRoleListVo pmsRoleList(@NotNull @NotBlank @ApiParam("xtb_id") @RequestParam(name = "xtb_id") String dsId) {
		return exportDsService.pmsRoleList(dsId);
	}

	/**
	* @Description //对某一角色进行批量目的表授权
	* @Date 2021/1/12 1:53 下午
	* @param form
	* @return void
	**/
	@RequestMapping("pms/role/modify")
	public void pmsRoleModify(ExportDsForm.ExportDsPmsRoleModifyForm form) {
		exportDsService.pmsRoleModify(form);
	}

	/**
	* @Description //某一角色被授权目的表列表展示
	* @Date 2021/1/12 1:53 下午
	* @param dsId
	* @param roleId
	* @return java.util.List<com.haizhi.hora.bean.vo.ExportDsVo.XtbVo>
	**/
	@RequestMapping("pms/tb/list")
	public List<ExportDsVo.XtbVo> pmsTbList(
		@NotNull @NotBlank @ApiParam("ds_id") @RequestParam(name = "ds_id") String dsId,
		@NotNull @NotBlank @ApiParam("role_id") @RequestParam(name = "role_id") String roleId
	) {
		return exportDsService.pmsTbList(dsId, roleId);
	}
	/**
	* @Description //删除某一角色下的所以授权目的表
	* @Date 2021/1/12 1:53 下午
	* @param dsId
	* @param roleId
	* @return void
	**/
	@RequestMapping("pms/delete")
	public void pmsRoleList(@NotNull @NotBlank @ApiParam("ds_id") @RequestParam(name = "ds_id") String dsId,
							@NotNull @NotBlank @ApiParam("role_id") @RequestParam(name = "role_id") String roleId
	) {
		exportDsService.pmsRoleDelete(dsId, roleId);
	}

	/**
	* @Description //目的表关联的校验规则
	* @Date 2021/1/12 1:54 下午
	* @param form
	* @return com.haizhi.hora.bean.vo.ExportDsVo.FieldNameRelRuleVo
	**/
	@RequestMapping("tb/rule/list")
	public ExportDsVo.FieldNameRelRuleVo tbRuleList(ExportDsForm.TbRuleListForm form) {
		return exportDsService.tbRuleList(form);
	}
	/**
	* @Description //修改目的表关联的校验规则
	* @Date 2021/1/12 1:55 下午
	* @param form
	* @return void
	**/
	@RequestMapping("tb/rule/modify")
	public void tbRuleModify(ExportDsForm.TbRuleModifyForm form) {
		exportDsService.tbRuleModify(form);
	}

	/**
	 * @Description //获取表信息
	 * @Date 2021/1/12 1:55 下午
	 * @param xtbId
	 * @return void
	 **/
	@RequestMapping("tb/info")
	public ExportDsVo.XtbInfoVo getXtbInfo(@RequestParam("xtbId") String xtbId) {
		return exportDsService.getXtbInfo(xtbId);
	}
}
