package com.haizhi.databridge.repository.importdata;

import java.util.List;
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

    @Query(value = "select xxljob_id from scheduler_xxljob_rel where is_del=0 and scheduler_id = (?1)", nativeQuery = true)
    Optional<List<String>> findXxljobId(String schedulerId);

    @Modifying
    @Transactional
    @Query(value = "update scheduler_xxljob_rel set is_del = 1 where job_id = ?1", nativeQuery = true)
    void logicDeleteByJobId(String jobId);
}
