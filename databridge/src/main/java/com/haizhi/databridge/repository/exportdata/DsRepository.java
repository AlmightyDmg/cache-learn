package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.DsBean;


@Repository
public interface DsRepository extends HaizhiBaseRepository<DsBean, String> {
	Optional<DsBean> findByDsId(String dsId);
	Optional<List<DsBean>> findByOwnerAndSourceType(String owner, Integer sourceType);
	Optional<List<DsBean>> findByOwnerAndTypeAndSourceType(String owner, Integer type, Integer sourceType);
	List<DsBean> findByDsIdInAndSourceType(List<String> dsIds, Integer sourceType);

	@Query(value = "select * from DS where is_del=0 and ds_id in (?1)", nativeQuery = true)
	Optional<List<DsBean>> findAllByDsIdIn(List<String> dsIds);

	@Modifying
	@Transactional
	@Query(value = "update DS set is_del = 1 where ds_id = ?1", nativeQuery = true)
	void deleteByDsId(String dsId);

	Optional<List<DsBean>> findAllByName(String name);


	Optional<DsBean> findTopByDbIdAndEntId(String dbId, String entId);

}
