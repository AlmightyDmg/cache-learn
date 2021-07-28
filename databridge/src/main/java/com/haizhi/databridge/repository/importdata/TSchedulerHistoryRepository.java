package com.haizhi.databridge.repository.importdata;

import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.importdata.TSchedulerHistory;

@Repository
public interface TSchedulerHistoryRepository extends HaizhiBaseRepository<TSchedulerHistory, String> {
}
