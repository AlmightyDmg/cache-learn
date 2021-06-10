package com.haizhi.databridge.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.TSchedulerBean;

@Transactional
@Repository
public interface TSchedulerRepository extends HaizhiBaseRepository<TSchedulerBean, String> {

	@Query(value = "SELECT * FROM TSchedulerBean WHERE TSchedulerBean.scheduler_id = ?1 "
			+ "AND TSchedulerBean.owner = ?2 AND and deleted=0", nativeQuery = true)
	Optional<TSchedulerBean> findBySchedulerIdAndOwner(String schedulerId, String owner);

	@Query(value = "SELECT * FROM TSchedulerBean WHERE TSchedulerBean.scheduler_name = ?1 "
			+ "AND TSchedulerBean.owner = ?2 AND and deleted=0", nativeQuery = true)
	Optional<TSchedulerBean> findBySchedulerNameAndOwner(String schedulerName, String owner);

	@Query(value = "update TSchedulerBean set deleted = 1 where scheduler_id = ?1 and deleted=0", nativeQuery = true)
	void logicDeleteBySchedulerId(String schedulerId);

	@Query(value = "select count(1) as count "
			+ "from TSchedulerBean where TSchedulerBean.deleted=0 and TSchedulerBean.owner=?1", nativeQuery = true)
	Map<String, BigInteger> countTSchedulerBeanByOwner(String owner);

	@Query(value = "SELECT * FROM TSchedulerBean WHERE TSchedulerBean.owner = ?1 "
			+ "ORDER BY TSchedulerBean.create_at LIMIT ?2, ?3", nativeQuery = true)
	Optional<List<TSchedulerBean>> findTSchedulerByOwner(String owner, Integer start, Integer end);

	@Query(value = "select *  from TSchedulerBean  WHERE TSchedulerBean.scheduler_id in (?1) and TSchedulerBean.deleted=0", nativeQuery = true)
	Optional<List<TSchedulerBean>> findTSchedulerBeanBySchedulerIds(List<String> schdulerIds);

	@Query(value = "select * from TSchedulerBean where  TSchedulerBean.owner = ?1 "
			+ "and TSchedulerBean.`scheduler_name` like concat('%', (?2) ,'%'))", nativeQuery = true)
	Optional<List<TSchedulerBean>> findTSchedulerByOwnerAndSchedulerNameLike(String owner, String searchKey);
}
