package com.haizhi.databridge.service;

import java.io.IOException;
import java.util.List;

import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

public interface DataSchedulerService {

	List<DataSchedulerVo.RetrieveVo> retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException;
	void update(DataSchedulerForm.UpdateForm updateForm);
	void delete(DataSchedulerForm.DeleteForm updateForm);
	DataTransJobVo getJobExecInfo(String jobId);
	String updateJobStatus(String jobId, Integer jobStatus, Long startTime, Long endTime);
	String updateJobExecUnit(JobUnitStateForm form);
}
