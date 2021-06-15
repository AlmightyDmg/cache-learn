package com.haizhi.databridge.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.TDataBaseSourceBean;

@Transactional
@Repository
public interface TdataBaseSourceRepository extends HaizhiBaseRepository<TDataBaseSourceBean, String> {
	@Query(value = "select *  from t_database  WHERE t_database.ds_name = ?1 "
			+ "and t_database.owner = ?2 and t_database.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByDsNameAndOwner(String dsName, String owner);

	@Query(value = "select *  from t_database  WHERE t_database.ds_id = ?1 "
			+ "and t_database.owner = ?2 and t_database.deleted=0", nativeQuery = true)
	Optional<TDataBaseSourceBean> findByDbIdAndOwner(String dsId, String owner);

	@Query(value = "select *  from t_database  WHERE t_database.owner = ?1 "
			+ "and t_database.db_id in (?2) and t_database.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByOwnerAndDbIdIn(String owner, List<String> dbIds);

	@Query(value = "update t_database set deleted = 1 where db_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteByDbId(String dbId);

}
