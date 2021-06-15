package com.haizhi.databridge.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.haizhi.databridge.bean.vo.DataSchedulerVo;
import com.haizhi.databridge.web.controller.form.DataSchedulerForm;

public interface DataSchedulerService {

	DataSchedulerVo.RetrieveVo retrieve(DataSchedulerForm.RetrieveForm retrieveForm) throws IOException;
	void update(DataSchedulerForm.UpdateForm updateForm) throws UnsupportedEncodingException;
	void delete(DataSchedulerForm.DeleteForm deleteForm);
	DataSchedulerVo.ListVo list(DataSchedulerForm.ListForm listForm) throws UnsupportedEncodingException;

}
