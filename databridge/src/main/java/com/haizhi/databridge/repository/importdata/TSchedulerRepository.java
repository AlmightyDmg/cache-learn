package com.haizhi.databridge.repository.importdata;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TSchedulerBean;



@Transactional
@Repository
public interface TSchedulerRepository extends HaizhiBaseRepository<TSchedulerBean, String> {
	@Query(value = "SELECT * FROM t_scheduler WHERE t_scheduler.scheduler_id = ?1 "
			+ "AND deleted=0", nativeQuery = true)
	Optional<TSchedulerBean> findBySchedulerId(String schedulerId);

	@Query(value = "SELECT * FROM t_scheduler WHERE t_scheduler.scheduler_id = ?1 "
			+ "AND t_scheduler.owner = ?2 AND deleted=0", nativeQuery = true)
	Optional<TSchedulerBean> findBySchedulerIdAndOwner(String schedulerId, String owner);

	@Query(value = "SELECT * FROM t_scheduler WHERE t_scheduler.scheduler_name = ?1 "
			+ "AND t_scheduler.owner = ?2 AND t_scheduler.deleted=0", nativeQuery = true)
	Optional<TSchedulerBean> findBySchedulerNameAndOwner(String schedulerName, String owner);

	@Modifying
	@org.springframework.transaction.annotation.Transactional
	@Query(value = "update t_scheduler set deleted = 1 where scheduler_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteBySchedulerId(String schedulerId);

	@Query(value = "select count(1) as count "
			+ "from t_scheduler where t_scheduler.deleted=0 and t_scheduler.owner=?1", nativeQuery = true)
	Map<String, BigInteger> countTSchedulerBeanByOwner(String owner);

	@Query(value = "select count(1) as count "
			+ "from t_scheduler where t_scheduler.deleted=0 and t_scheduler.owner=?1 and t_scheduler.status=?2", nativeQuery = true)
	Map<String, BigInteger> countTSchedulerBeanByOwnerAndStatus(String owner, String status);

	@Query(value = "SELECT *  FROM t_scheduler WHERE t_scheduler.deleted=0  "
			+ "and t_scheduler.owner = ?1 "
			+ " ORDER BY t_scheduler.create_at LIMIT ?2, ?3", nativeQuery = true)
	Optional<List<TSchedulerBean>> findSchedulerByOwner(String owner, Integer start, Integer end);

	@Query(value = "select *  from t_scheduler  WHERE t_scheduler.scheduler_id in (?1) and t_scheduler.deleted=0", nativeQuery = true)
	Optional<List<TSchedulerBean>> findTSchedulerBeanBySchedulerIds(List<String> schdulerIds);

	@Query(value = "select * from t_scheduler where  t_scheduler.owner = ?1 "
			+ "and t_scheduler.`scheduler_name` like concat('%', (?2) ,'%') and t_scheduler.deleted=0", nativeQuery = true)
	Optional<List<TSchedulerBean>> findTSchedulerByOwnerAndSchedulerNameLike(String owner, String searchKey);

}
