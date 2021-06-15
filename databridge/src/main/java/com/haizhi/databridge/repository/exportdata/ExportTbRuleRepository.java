package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbRuleBean;

@Transactional
@Repository
public interface ExportTbRuleRepository extends HaizhiBaseRepository<ExportTbRuleBean, String> {

//	/**
//	 * @Description 删除规则
//	 * @Date 2020/12/22 3:03 下午
//	 * @param ruleId
//	 * @return
//	 **/
//	@Query(value = "update EXPORT_TB_RULE set is_del = 1 where rule_id = (?1)", nativeQuery = true)
//	void deleteRule(String ruleId);

//	/**
//	 * @Description 修改规则
//	 * @Date 2020/12/22 3:04 下午
//	 * @param ruleName, cond, userId
//	 * @return
//	 **/
//	@Query(value = "update EXPORT_TB_RULE set rule_name = (?1) , cond = (?2) where rule_id = (?3) and is_del = 0", nativeQuery = true)
//	void modifyRule(String ruleName,String cond, String ruleId);

//	/**
//	 * @Description 查看用户自建的规则
//	 * @Date 2020/12/22 3:05 下午
//	 * @param userId
//	 **/
//	@Query(value = "select * from EXPORT_TB_RULE where owner = (?1) and is_del = 0", nativeQuery = true)
//	List<ExportTbRuleBean> findOwnRuleByOwner(String userId);

	Optional<List<ExportTbRuleBean>> findAllByTypeAndOwner(Integer type, String userId);
//	/**
//	 * @Description 查看系统内置的规则
//	 * @Date 2020/12/22 3:06 下午
//	 * @param
//	 * @return
//	 **/
//	@Query(value = "select * from EXPORT_TB_RULE where type=0 and is_del = 0", nativeQuery = true)
//	List<ExportTbRuleBean> findSysRuleById();

	@Modifying
	@Query(value = "update EXPORT_TB_RULE set is_del = 1 where rule_id = ?1 and is_del=0", nativeQuery = true)
	void logicDeleteByRuleId(String ruleId);

	Optional<List<ExportTbRuleBean>> findAllByTypeAndEntId(Integer type, String entId);
	Optional<ExportTbRuleBean> findByRuleId(String ruleId);
	Optional<List<ExportTbRuleBean>> findAllByRuleIdIn(List<String> ruleIds);
}
