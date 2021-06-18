package com.haizhi.databridge.repository.importdata;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;

@Transactional
@Repository
public interface TTableRepository extends HaizhiBaseRepository<TTableBean, String> {

	@Query(value = "select *  from t_table  WHERE t_table.db_id = ?1 AND t_table.owner = ?2 "
			+ "and t_table.deleted=0", nativeQuery = true)
	Optional<List<TTableBean>> findAllByDbIdAndOwner(String dbId, String owner);

	Optional<TTableBean> findByTbNameAndDbIdAndOwner(String tbName, String dbId, String owner);


	Optional<TTableBean> findByTableIdAndOwner(String tableId, String owner);

	Optional<List<TTableBean>> findBySchedulerIdAndOwner(String schedulerId, String owner);

	@Query(value = "update t_table set t_table.deleted = 1 where t_table.table_id = ?1", nativeQuery = true)
	Object logicDeleteByTableId(String tableId);

	@Query(value = "update t_table set t_table.deleted = 1 where t_table.scheduler_id = ?1", nativeQuery = true)
	void logicDeleteBySchedulerId(String schedulerId);

	@Query(value = "select *  from t_table  WHERE t_table.owner = ?1 "
			+ "and t_table.deleted=0", nativeQuery = true)
	Optional<List<TTableBean>> findByOwner(String owner);

	@Query(value = "select *  from t_table  WHERE t_table.owner = ?1 and t_table.scheduler_id in (?2) "
			+ "and t_table.deleted=0", nativeQuery = true)
	Optional<List<TTableBean>> findTableBeanByOwnerAndSchedulerIds(String owner, List<String> schdulerIds);

	@Query(value = "select * from t_table where  t_table.owner = ?1 "
			+ "and t_table.`tb_name` like concat('%', (?2) ,'%'))", nativeQuery = true)
	Optional<List<TTableBean>> findTableByOwnerAndTbNameLike(String owner, String searchKey);

	@Query(value = "select *  from t_table  WHERE t_table.owner = ?1 and t_table.db_id in (?2) "
			+ "and t_table.deleted=0", nativeQuery = true)
	Optional<List<TTableBean>> findTableBeanByOwnerAndDbIds(String owner, List<String> dbId);

	@Query(value = "select t_table.db_id, count(1) as count  from t_table  WHERE t_table.owner = ?1 and t_table.db_id in (?2) "
			+ "and t_table.deleted=0", nativeQuery = true)
	List<Map<String, Object>> countTableNumByDbIdAndOwner(String owner, List<String> dbIds);
}
