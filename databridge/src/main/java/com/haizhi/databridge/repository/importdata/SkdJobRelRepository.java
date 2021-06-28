package com.haizhi.databridge.repository.importdata;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.JobRelBean;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月21日 20:39:12
 */
@Repository
public interface SkdJobRelRepository extends HaizhiBaseRepository<JobRelBean, String> {
    Optional<JobRelBean> findByJobId(String jobId);

    @Modifying
    @Transactional
    @Query(value = "update t_job_distjob_rel set is_del = 1 where job_id = ?1", nativeQuery = true)
    void logicDeleteByJobId(String jobId);
}
