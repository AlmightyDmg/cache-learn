package com.haizhi.databridge.repository.exportdata;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.haizhi.data.jpa.HaizhiBaseRepository;
import com.haizhi.databridge.bean.domain.exportdata.ExportLogBean;

@Repository
public interface ExportLogRepository extends HaizhiBaseRepository<ExportLogBean, String> {
	Optional<List<ExportLogBean>> findAllByJobId(String jobId);
}
