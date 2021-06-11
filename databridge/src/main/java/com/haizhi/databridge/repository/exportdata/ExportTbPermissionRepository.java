package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.ExportTbPermissionBean;

@Transactional
@Repository
public interface ExportTbPermissionRepository extends HaizhiBaseRepository<ExportTbPermissionBean, String> {
	Optional<List<ExportTbPermissionBean>> findAllByXtbId(String xtbId);
	Optional<List<ExportTbPermissionBean>> findAllByDsId(String xtbId);
	Optional<List<ExportTbPermissionBean>> findAllByDsIdAndRoleId(String dsId, String roleId);

	@Modifying
	@Query(value = "update EXPORT_TB_PERMISSION set is_del = 1 where xtb_id = ?1 and role_id = ?2", nativeQuery = true)
	void logicDeleteByXtbIdAndRoleId(String xtbId, String roleId);
	@Modifying
	@Query(value = "update EXPORT_TB_PERMISSION set is_del = 1 where ds_id = ?1 and role_id = ?2", nativeQuery = true)
	void logicDeleteByDsIdAndRoleId(String dsId, String roleId);

	@Query(value = "select * from EXPORT_TB_PERMISSION where is_del=0 and role_id in (?1)", nativeQuery = true)
	Optional<List<ExportTbPermissionBean>> findAllByRoleIdIn(List<String> roleIds);

	@Modifying
	@Query(value = "update EXPORT_TB_PERMISSION set is_del = 1 where xtb_id = ?1 and is_del=0", nativeQuery = true)
	void logicDeleteByXtbId(String xtbId);

	@Modifying
	@Query(value = "update EXPORT_TB_PERMISSION set is_del = 1 where xtb_id in ?1 and is_del=0", nativeQuery = true)
	void logicDeleteByXtbIdIn(List<String> xtbIdList);
}
