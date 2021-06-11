package com.haizhi.databridge.web.controller;

import java.util.List;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haizhi.databridge.bean.domain.exportdata.ExportTbRuleBean;
import com.haizhi.databridge.bean.vo.ExportJobVo;
import com.haizhi.databridge.service.export.RuleService;
import com.haizhi.databridge.web.controller.base.BaseController;
import com.haizhi.databridge.web.controller.form.RuleForm;

/**
 * @ClassName RuleController
 * @Description TODO
 * @Author daiaoqi
 * @Date 2020/12/22 3:27 下午
 * @Version1.0
 **/


@RestController
@RequestMapping("/api/export_ds/rule/")
@Api(tags = "export_rule", description = "校验规则表")
public class ExportRuleController extends BaseController {

	@Resource
	private RuleService ruleService;


	@ApiOperation("查询系统内置和用户自建的校验规则")
	@RequestMapping("list")
	public List<ExportTbRuleBean> showAllRule() {
		return ruleService.ruleInfos(getUserId());
	}

	@ApiOperation("新建规则")
	@RequestMapping("create")
	public void addRule(RuleForm.AddForm ruleForm) {
		 ruleService.addRule(ruleForm);
	}

	@ApiOperation("删除规则")
	@RequestMapping("delete")
	public void delRule(RuleForm.DelForm ruleForm) {
		ruleService.delRule(ruleForm);
	}

	@ApiOperation("规则被哪些表的字段应用")
	@RequestMapping("check_rely")
	public List<String> checkRely(RuleForm.CheckRelyForm ruleForm) {
		return ruleService.checkRely(ruleForm);
	}

	@ApiOperation("修改规则")
	@RequestMapping("modify")
	public void modify(RuleForm.ModifyForm ruleForm) {
		ruleService.modifyRule(ruleForm);
	}

	@ApiOperation("规则检查")
	@RequestMapping("check")
	public boolean check(RuleForm.CheckForm ruleForm) {
		return ruleService.checkRule(ruleForm);
	}

	@ApiOperation("获取table的规则")
	@RequestMapping("tb/info")
	public List<ExportJobVo.RuleFilterVo> getRuleInfosByXtbId(@RequestParam String xtbId) {
		return ruleService.getRuleInfosByXtbId(xtbId);
	}
}
