package com.haizhi.databridge.repository.importdata;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TblTransTaskRelBean;

/**
 * @author duanxiaoyi
 * @Description TODO
 * @createTime 2021年05月21日 20:39:12
 */
@Repository
public interface TblTransTaskRelRepository extends HaizhiBaseRepository<TblTransTaskRelBean, String> {

    @Query(value = "select * from t_trans_task_rel "
            + "where is_del=0 and job_id = (?1) and from_table_id = (?2) and to_table_id = (?3)", nativeQuery = true)
    Optional<TblTransTaskRelBean> findTransTask(String jobId, String fromTableId, String toTableId);

    @Modifying
    @Transactional
    @Query(value = "update t_trans_task_rel set is_del = 1 where job_id = ?1", nativeQuery = true)
    void logicDeleteByJobId(String jobId);

    @Modifying
    @Transactional
    @Query(value = "update t_trans_task_rel set trans_task_id = '' where job_id = ?1 and is_del = 0 ", nativeQuery = true)
    void clearTaskId(String jobId);
}
