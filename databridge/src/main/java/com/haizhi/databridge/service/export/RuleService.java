package com.haizhi.databridge.service.export;

import static com.haizhi.databridge.util.IdUtils.genKey;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.haizhi.databridge.bean.domain.exportdata.ExportTbRelRuleBean;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbRuleBean;
import com.haizhi.databridge.bean.vo.ExportJobVo;
import com.haizhi.databridge.exception.DatabridgeException;
import com.haizhi.databridge.repository.exportdata.ExportTbRelRuleRepository;
import com.haizhi.databridge.repository.exportdata.ExportTbRuleRepository;
import com.haizhi.databridge.util.RequestCommonData;
import com.haizhi.databridge.util.SpringUtils;
import com.haizhi.databridge.util.SqlCheckUtils;
import com.haizhi.databridge.web.controller.form.RuleForm;
import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.dataclient.connection.dmc.client.mobius.request.QueryExplainReq;
import com.haizhi.dataclient.datapi.dmc.DmcTableApi;

@Component
@Service
@Log4j2
public class RuleService extends RequestCommonData {

	@Autowired
	private ExportTbRuleRepository ruleRepo;

	@Autowired
	private ExportTbRelRuleRepository exportTbRelRuleRepository;

	@Autowired
	private ExportDsService exportDsService;

	@Autowired
	private ExportJobService exportJobService;


	/**
	 * @Description 获取所有规则
	 * @Date 2020/12/22 5:39 下午
	 * @param userId
	 * @return
	 **/
	public List<ExportTbRuleBean> ruleInfos(String userId) {
		List<ExportTbRuleBean> ruleBeanList = new ArrayList<>();
		// 自建的规则
		Optional<List<ExportTbRuleBean>> ownList = ruleRepo.findAllByTypeAndOwner(1, userId);
		// 内置的规则
		Optional<List<ExportTbRuleBean>> syslist = ruleRepo.findAllByTypeAndEntId(0, getEnterpriseId());
		// 合并
		if (ownList.isPresent()) {
			ruleBeanList.addAll(ownList.get());
		}
		if (syslist.isPresent()) {
			ruleBeanList.addAll(syslist.get());
		}
		return ruleBeanList;
	}

	/**
	 * @Description 新建规则
	 * @Date 2020/12/22 5:40 下午
	 * @param ruleForm
	 * @return
	 **/

	public void addRule(RuleForm.AddForm ruleForm) {
		String ruleName = ruleForm.getRuleName();
		String userId = getOwner();
		// 判断是否存在重名
		boolean bool = checkName(ruleName, userId);
		if (bool) {
			throw new DatabridgeException(StatusCode.RULE_NAME_EXIT, "已存在同名规则");
		} else {
			ExportTbRuleBean ruleBean = new ExportTbRuleBean();
			ruleBean.setRuleName(ruleForm.getRuleName());
			ruleBean.setRuleId(genKey("rule"));
			ruleBean.setOwner(userId);
			ruleBean.setCond(ruleForm.getCond());
			ruleBean.setEntId(getEnterpriseId());
			ruleBean.setType(1);
			ruleRepo.save(ruleBean);
		}
	}

	/**
	 * @Description 删除规则
	 * @Date 2020/12/22 5:46 下午
	 * @param ruleForm
	 * @return
	 **/
	public void delRule(RuleForm.DelForm ruleForm) {
		String ruleId = ruleForm.getRuleId();
		boolean hasMapped = false;
		Optional<List<ExportTbRelRuleBean>> optionalExportTbRelRuleBeanList = exportTbRelRuleRepository.findAllByRuleId(ruleId);
		if (optionalExportTbRelRuleBeanList.isPresent()) {
			List<String> xtbIdList = optionalExportTbRelRuleBeanList.get().stream()
				.map(ExportTbRelRuleBean::getRelaId).collect(Collectors.toList());
			List<String> xTbmappedList = exportDsService.getMappedXtbIdList(xtbIdList);
			for (ExportTbRelRuleBean exportTbRelRuleBean: optionalExportTbRelRuleBeanList.get()) {
				if (xTbmappedList.contains(exportTbRelRuleBean.getXtbId())) {
					hasMapped = true;
				}
			}
		}
		if (hasMapped) {
			throw new DatabridgeException(StatusCode.RULE_MAPPED, "存在已映射规则");
		}
		ruleRepo.logicDeleteByRuleId(ruleId);
	}

	public List<String> checkRely(RuleForm.CheckRelyForm ruleForm) {
//		List<String> relyFieldNameList = new ArrayList<>();
		List<String> relyXtbIdList = new ArrayList<>();
		String ruleId = ruleForm.getRuleId();
		Optional<List<ExportTbRelRuleBean>> optExpTbRelRuleBeanList = exportTbRelRuleRepository.findAllByRuleId(ruleId);
		if (optExpTbRelRuleBeanList.isPresent()) {
//			relyFieldNameList = optExpTbRelRuleBeanList.get().stream()
//				.map(ExportTbRelRuleBean::getFieldName).collect(Collectors.toList());
			List<String> xtbIdList = optExpTbRelRuleBeanList.get().stream().map(ExportTbRelRuleBean::getXtbId)
				.collect(Collectors.toList());
			if (!ObjectUtils.isEmpty(xtbIdList)) {
				relyXtbIdList = exportDsService.getMappedXtbIdList(xtbIdList);
			}
		}
		return relyXtbIdList;
	}

	/**
	 * @Description 修改规则
	 * @Date 2020/12/22 6:02 下午
	 * @param ruleForm
	 * @return
	 **/
	public void modifyRule(RuleForm.ModifyForm ruleForm) {
		String ruleName = ruleForm.getRuleName();
		String codn = ruleForm.getCond();
		String ruleId = ruleForm.getRuleId();

		Boolean nameDuplicateBool = false;

		Optional<List<ExportTbRuleBean>> ownList = ruleRepo.findAllByTypeAndOwner(1, getOwner());
		if (ownList.isPresent()) {
			for (ExportTbRuleBean exportTbRuleBean: ownList.get()) {
				if (exportTbRuleBean.getRuleName().equals(ruleName) && !exportTbRuleBean.getRuleId().equals(ruleId)) {
					throw new DatabridgeException(StatusCode.RULE_NAME_EXIT, "已存在同名规则");
				}
			}
		}
		// 内置的规则
		Optional<List<ExportTbRuleBean>> syslist = ruleRepo.findAllByTypeAndEntId(0, getEnterpriseId());
		if (syslist.isPresent()) {
			for (ExportTbRuleBean exportTbRuleBean: syslist.get()) {
				if (exportTbRuleBean.getRuleName().equals(ruleName)) {
					throw new DatabridgeException(StatusCode.RULE_NAME_EXIT, "已存在同名规则");
				}
			}
		}

		Optional<ExportTbRuleBean> exportTbRuleBeanOptional = ruleRepo.findByRuleId(ruleId);
		if (!exportTbRuleBeanOptional.isPresent()) {
			throw new DatabridgeException("规则不存在");
		}
		ExportTbRuleBean exportTbRuleBean = exportTbRuleBeanOptional.get();
		exportTbRuleBean.setRuleName(ruleName);
		exportTbRuleBean.setCond(codn);
		ruleRepo.update(exportTbRuleBean);


	}

	/**
	 * @Description 检验是否有相同名称的用户
	 * @Date 2020/12/22 7:28 下午
	 * @param ruleName
	 * @return
	 **/
	public boolean checkName(String ruleName, String userId) {

		ArrayList list = new ArrayList();
		// 获取到这个用户所有的规则
		List<ExportTbRuleBean> ruleList = ruleInfos(userId);
		// 获取到所有规则名称
		for (ExportTbRuleBean bean: ruleList) {
			list.add(bean.getRuleName());
		}
		return list.contains(ruleName);
	}

	public boolean checkRule(RuleForm.CheckForm ruleForm) {

		String filterCond = ruleForm.getCond();
		// 最后调mobius传的sql
		String finalSql = " WITH  `test` AS ( {0} ),`output` AS ( select `tmpSchema` from `test` )  select `tmpSchema` from `test` where {1}";

		// finalSql中需要替换临时表的部分，因为数据库没有，所以用with as （select 字段 values(值)） 构造一下出来
		// 不知道filterCond中的字段类型，所以三种类型分别构建一个出来
		String stringValueSql = "SELECT  `tmpSchema` FROM VALUES('abc') AS test(`tmpSchema`) WHERE false";
//		String integerValueSql = "SELECT  `tmpSchema` FROM VALUES(1) AS test(`tmpSchema`) WHERE false";
//		String datetimeValueSql = "SELECT  `tmpSchema` FROM VALUES('2021-01-10 10:27:37') AS test(`tmpSchema`) WHERE false";
		DmcTableApi client = SpringUtils.getBean(DmcTableApi.class);
		String sql = MessageFormat.format(finalSql, stringValueSql, filterCond.replace("${field}", "tmpSchema"));
		String plan = client.explain(QueryExplainReq.builder().sql(sql).build()).getPlan();
		return checkFormula(plan);
	}

	/**
	 * 计算字段校验
	 * @param
	 * @return
	 */
	public boolean checkFormula(String plan) {
		//先做基础校验
		for (SqlCheckUtils.SqlError err : SqlCheckUtils.SqlError.values()) {
			//sql未能通过检查
			if (!SqlCheckUtils.SqlError.check(plan, err)) {
				Pair<Boolean, SqlCheckUtils.CheckMsg> pair = new Pair<>(false, err);
				SqlCheckUtils.handlerError(pair.getValue());
				return false;
			}
		}

		return true;
	}


	public List<ExportJobVo.RuleFilterVo> getRuleInfosByXtbId(String xtbId) {
		List<ExportJobVo.RuleFilterVo> ruleFilterVos = new ArrayList<>();
		Optional<List<ExportTbRelRuleBean>> optionalExportTbRelRuleBeanList = exportTbRelRuleRepository.findAllByXtbId(xtbId);
		if (optionalExportTbRelRuleBeanList.isPresent()) {
			List<String> ruleIdList = optionalExportTbRelRuleBeanList.get()
				.stream().map(ExportTbRelRuleBean::getRuleId).collect(Collectors.toList());
			Map<String, ExportTbRuleBean> exportTbRuleBeanMap = new HashMap<>();
			if (!ObjectUtils.isEmpty(ruleIdList)) {
				Optional<List<ExportTbRuleBean>> optionalExportTbRuleBeanList = ruleRepo.findAllByRuleIdIn(ruleIdList);
				if (optionalExportTbRuleBeanList.isPresent()) {

					for (ExportTbRuleBean exportTbRuleBean: optionalExportTbRuleBeanList.get()) {
						exportTbRuleBeanMap.put(exportTbRuleBean.getRuleId(), exportTbRuleBean);
					}
				}
			}
			for (ExportTbRelRuleBean exportTbRelRuleBean: optionalExportTbRelRuleBeanList.get()) {
				String cond = "";
				if (!ObjectUtils.isEmpty(exportTbRuleBeanMap.get(exportTbRelRuleBean.getRuleId()))) {
					cond = exportTbRuleBeanMap.get(exportTbRelRuleBean.getRuleId()).getCond();
				}
				ruleFilterVos.add(ExportJobVo.RuleFilterVo
					.builder()
					.mappingName(exportTbRelRuleBean.getFieldName())
					.cond(cond)
					.build()
				);
			}

		}
		return ruleFilterVos;
	}
}
