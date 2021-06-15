package com.haizhi.databridge.repository.importdata;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TTableBean;

@Transactional
@Repository
public interface TTableRepository extends HaizhiBaseRepository<TTableBean, String> {

	Optional<List<TTableBean>> findAllByDbIdAndOwner(String dbId, String owner);
	Optional<TTableBean> findByTbNameAndDbIdAndOwner(String tbName, String dbId, String owner);
	Optional<TTableBean> findByTableIdAndOwner(String tableId, String owner);

	Optional<List<TTableBean>> findBySchedulerIdAndOwner(String schedulerId, String owner);

	@Query(value = "update TTableBean set deleted = 1 where table_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteByTableId(String tableId);

	@Query(value = "update TTableBean set deleted = 1 where scheduler_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteBySchedulerId(String schedulerId);
	Optional<List<TTableBean>> findByOwner(String owner);

	@Query(value = "select *  from TTableBean  WHERE TTableBean.owner = ?1 TTableBean.scheduler_id in (?2) "
			+ "and TTableBean.deleted=0", nativeQuery = true)
	Optional<List<TTableBean>> findTableBeanByOwnerAndSchedulerIds(String owner, List<String> schdulerIds);

	@Query(value = "select * from TTableBean where  TTableBean.owner = ?1 "
			+ "and TTableBean.`tb_name` like concat('%', (?2) ,'%'))", nativeQuery = true)
	Optional<List<TTableBean>> findTableByOwnerAndTbNameLike(String owner, String searchKey);
}
