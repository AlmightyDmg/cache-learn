package com.haizhi.databridge.repository.importdata;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TDataBaseSourceBean;

@Transactional
@Repository
public interface TdataBaseSourceRepository extends HaizhiBaseRepository<TDataBaseSourceBean, String> {
	@Query(value = "select *  from t_database  WHERE t_database.ds_name = ?1 "
			+ "and t_database.owner = ?2 and t_database.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByDsNameAndOwner(String dsName, String owner);


	@Query(value = "select *  from t_database  WHERE t_database.ds_id = ?1 "
			+ "and t_database.owner = ?2 and t_database.deleted=0", nativeQuery = true)
	Optional<TDataBaseSourceBean> findByDbIdAndOwner(String dsId, String owner);

	@Query(value = "select *  from t_database  WHERE t_database.owner = ?1 and t_database.source_type = ?2  "
			+ "and t_database.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByOwnerAndSourceType(String owner, Integer sourceType);

	@Query(value = "select *  from t_database  WHERE t_database.owner = ?1 "
			+ "and t_database.db_id in (?2) and t_database.deleted=0", nativeQuery = true)
	Optional<List<TDataBaseSourceBean>> findByOwnerAndDbIdIn(String owner, List<String> dbIds);

	@Query(value = "update t_database set deleted = 1 where db_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteByDbId(String dbId);

	Optional<TDataBaseSourceBean> findByDbId(String dbId);

	@Query(value = "select count(1) as count "
			+ "from t_database where t_database.deleted=0 and t_database.owner=?1", nativeQuery = true)
	Map<String, BigInteger> countTDataBaseSourceBeanByOwner(String owner);
}
