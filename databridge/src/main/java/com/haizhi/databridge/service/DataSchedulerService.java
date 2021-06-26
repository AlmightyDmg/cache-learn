package com.haizhi.databridge.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.bean.vo.DataTransJobVo;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;
import com.haizhi.databridge.web.controller.form.JobStateForm;
import com.haizhi.databridge.web.controller.form.JobUnitStateForm;

public interface DataSchedulerService {

	DataSchedulerVo.RetrieveVo retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException;
	void create(DataSchedulerForm.CreateForm createForm) throws UnsupportedEncodingException;
	void update(DataSchedulerForm.UpdateForm updateForm) throws UnsupportedEncodingException;
	void delete(DataSchedulerForm.DeleteForm updateForm);
	DataSchedulerVo.ListVo list(DataSchedulerForm.ListForm listForm) throws IOException;
	DataTransJobVo getJobExecInfo(String jobId);
	String updateJobStatus(JobStateForm jobStateForm);
	String updateJobExecUnit(JobUnitStateForm form);
	void trigger(DataSchedulerForm.TriggerForm triggerForm);
	void start(DataSchedulerForm.StartForm startForm);
	void stop(DataSchedulerForm.StopForm stopForm);
}
