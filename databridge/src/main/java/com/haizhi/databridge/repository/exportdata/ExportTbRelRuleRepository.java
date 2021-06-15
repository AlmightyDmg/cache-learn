package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbRelRuleBean;

@Transactional
@Repository
@DynamicInsert
@DynamicUpdate
public interface ExportTbRelRuleRepository extends HaizhiBaseRepository<ExportTbRelRuleBean, String> {
	Optional<List<ExportTbRelRuleBean>> findAllByXtbId(String xtbId);
	Optional<ExportTbRelRuleBean> findByRelaId(String relaId);
	Optional<List<ExportTbRelRuleBean>> findAllByRuleId(String ruleId);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "update EXPORT_TB_REL_RULE set is_del = 1 where rel_id = ?1", nativeQuery = true)
	void logicDeleteByRelId(String relId);

	Optional<List<ExportTbRelRuleBean>> findAllByXtbIdIn(List<String> xtbIdList);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "update EXPORT_TB_REL_RULE set is_del = 1 where xtb_id in ?1", nativeQuery = true)
	void logicDeleteByXtbIdIn(List<String> xtbIdList);
}
