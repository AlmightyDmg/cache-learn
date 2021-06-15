package com.haizhi.databridge.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

public interface DataSchedulerService {

	DataSchedulerVo.RetrieveVo retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException;
	void update(DataSchedulerForm.UpdateForm updateForm) throws UnsupportedEncodingException;
	void delete(DataSchedulerForm.DeleteForm updateForm);
	DataSchedulerVo.ListVo list(DataSchedulerForm.ListForm listForm) throws UnsupportedEncodingException;
	DataTransJobVo getJobExecInfo(String jobId);
	String updateJobStatus(String jobId, Integer jobStatus, Long startTime, Long endTime);
	String updateJobExecUnit(JobUnitStateForm form);
}
