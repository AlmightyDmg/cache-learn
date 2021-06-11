package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.ExportDsTbBean;

@Transactional
@Repository
public interface ExportDsTbRepository extends HaizhiBaseRepository<ExportDsTbBean, String> {
	Optional<ExportDsTbBean> findByXtbId(String xtbId);
	Optional<List<ExportDsTbBean>> findAllByDsId(String dsId);

	@Query(value = "select * from EXPORT_DS_TB where is_del=0 and xtb_id in (?1)", nativeQuery = true)
	Optional<List<ExportDsTbBean>> findXtbInfos(List<String> xtbIds);

	@Query(value = "select * from EXPORT_DS_TB where is_del=0 and ds_id in (?1)", nativeQuery = true)
	Optional<List<ExportDsTbBean>> findXtbInfoIndsIds(List<String> dsIds);

	@Modifying
	@Query(value = "update EXPORT_DS_TB set is_del = 1 where xtb_id = ?1 and is_del=0", nativeQuery = true)
	void logicDeleteByXtbId(String xtbId);

	@Query(value = "select ds_id, count(ds_id) from EXPORT_DS_TB where ds_id in (?1) and is_del=0 group by ds_id", nativeQuery = true)
	List<Map<String, Object>> countExportDsTbBeanByDsIdIn(List<String> dsIds);

	@Modifying
	@Query(value = "update EXPORT_DS_TB set is_del = 1 where xtb_id in ?1 and is_del=0", nativeQuery = true)
	void logicDeleteByXtbIdIn(List<String> xtbIdList);
}
