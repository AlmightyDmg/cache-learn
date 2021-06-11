package com.haizhi.databridge.repository.importdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;

@Transactional
@Repository
public interface TdataBaseSourceRepository extends HaizhiBaseRepository<TDataBaseSourceBean, String> {
	Optional<List<TDataBaseSourceBean>> findByDsNameAndOwner(String dsName, String owner);
	Optional<TDataBaseSourceBean> findByDbIdAndOwner(String dsName, String owner);

	@Query(value = "select *  from TDataBaseSourceBean  WHERE TDataBaseSourceBean.owner = ?1 "
			+ "and TDataBaseSourceBean.db_id in (?2) and TDataBaseSourceBean.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByOwnerAndDbIdIn(String owner, List<String> dbIds);

	@Query(value = "update TdataBaseSourceBean set deleted = 1 where db_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteByDbId(String dbId);

	Optional<TDataBaseSourceBean> findByDbId(String dbId);
}
