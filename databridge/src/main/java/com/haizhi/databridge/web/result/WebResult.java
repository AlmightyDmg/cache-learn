package com.haizhi.databridge.web.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.haizhi.databridge.exception.DatabridgeException;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

import org.slf4j.MDC;

import static com.haizhi.databridge.constants.DatabridgeContant.TRACE_ID;

@Slf4j
public class WebResult<T> {
	private StatusCode status = StatusCode.NORMAL;
	private String errstr;
	private T result;
	@JsonProperty("err_parameter")
	private T errorParam;


	public WebResult() {
	}

	public WebResult(T result, StatusCode status, String errstr) {
		this.result = result;
		this.status = status;
		this.errstr = errstr;
	}

	public WebResult(T result, StatusCode status, String errstr, T errorParam) {
		this.result = result;
		this.status = status;
		this.errstr = errstr;
		this.errorParam = errorParam;
	}

	public T getResult() {
		return this.result;
	}

	public WebResult<T> setResult(T result) {
		this.result = result;
		return this;
	}

	/**
	 * 兼容之前python服务返回的状态码（都是string类型的，而非int)
	 * @return
	 */
	public String getStatus() {
		return String.valueOf(this.status.getValue());
	}

	public WebResult<T> setStatus(StatusCode status) {
		this.status = status;
		return this;
	}

	public String getErrstr() {
		return this.errstr;
	}

	public WebResult<T> setErrstr(String errstr) {
		this.errstr = errstr;
		return this;
	}

	public T getErrorParam() {
		return this.errorParam;
	}

	public WebResult<T> setErrorParam(T errorParam) {
		this.errorParam = errorParam;
		return this;
	}

	public String getTrcid() {
		return MDC.get(TRACE_ID);
	}

	public static <T> WebResult<T> newInstance() {
		return new WebResult<T>();
	}

	public static <T> WebResult<T> of(T result) {
		if (result instanceof Optional) {
			return of((Optional<T>) result);
		}
		WebResult<T> r = newInstance();
		r.setResult(result);
		return r;
	}

	public static <T> WebResult<T> of(Optional<T> result) {
		return result.isPresent()
			? of(result.get())
			: new WebResult<T>().setErrstr("object does not exists").setStatus(StatusCode.OBJECT_NOT_EXISTS);
	}

	public static <T> WebResult<T> fail(Throwable throwable) {
		log.error(throwable.getMessage(), throwable);
		WebResult<T> r = newInstance();
		r.setStatus(StatusCode.API_INTERNAL_ERROR);
		r.setErrstr(throwable.getMessage());
		if (throwable instanceof DatabridgeException) {
			r.setErrorParam((T) ((DatabridgeException) throwable).getErrorParam());
		}

		return r;
	}

	public static <T> WebResult<T> fail(Throwable throwable, T o) {
		log.error(throwable.getMessage(), throwable);
		String msg = Optional.ofNullable(throwable.getCause())
			.map(s -> s.getMessage() + ",")
			.orElse("") + throwable.getMessage();
		WebResult<T> r = newInstance();
		r.setErrstr(msg);
		r.setStatus(StatusCode.API_INTERNAL_ERROR);
		r.setResult(o);
		return r;
	}


	/**
	 * @description: 自定义异常返回对象
	 * @param ${tags}
	 * @return ${return_type}
	 * @throws
	 * @author WangChengYu
	 * @date 2020-02-06 23:09
	 */
	public static <T> WebResult<T> fail(String errstr, StatusCode errCode, T t) {
		return new WebResult<>(t, errCode, errstr);
	}
}
