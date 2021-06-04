package com.haizhi.databridge.web.controller.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.databridge.web.result.WebResult;

@RequestMapping
public class GlobalErrorsController extends BaseController implements ErrorController {
	
	@Autowired(required = false)
	private ErrorAttributes errorAttributes;
	private static final String PATH = "path";

	@RequestMapping({"/error"})
	@ResponseBody
	public ResponseEntity<?> error() {
		Map<String, Object> body = this.errorAttributes.getErrorAttributes(new ServletWebRequest(this.request), false);
		return ResponseEntity.badRequest()
				.header("Location", body.get(getErrorPath()).toString())
				.header("requestIP", this.getRemoteIpFromHttpRequest())
				.body(WebResult.of(body).setStatus(StatusCode.API_INTERNAL_ERROR));
	}

	public String getErrorPath() {
		return PATH;
	}
}
