package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.JobBean;

@Repository
public interface JobRepository extends HaizhiBaseRepository<JobBean, String> {
	Optional<JobBean> findByJobId(String jobId);
	Optional<List<JobBean>> findAllByXtbIdInAndJobSource(List<String> xtbIdList, Integer jobSource);
	Optional<List<JobBean>> findAllByUserIdAndJobSource(String userId, Integer jobSource);
	Optional<List<JobBean>> findAllByDsId(String dsId);
	Optional<List<JobBean>> findAllByRelaIdAndEntIdAndJobSource(String relaId, String entId, Integer jobSource);

	@Modifying
	@Transactional
	@Query(value = "update JOB set is_del = 1 where job_id = ?1", nativeQuery = true)
	void logicDeleteByJobId(String jobId);

	@Transactional
	@Modifying
	@Query(value = "update JOB set status = ?2 where job_id = ?1", nativeQuery = true)
	int updateJob(String jobId, int status);
}
