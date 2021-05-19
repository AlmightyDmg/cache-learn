package com.haizhi.databridge.web.controller.advice;

import com.haizhi.databridge.web.result.StatusCode;
import com.haizhi.databridge.web.result.WebResult;
import com.haizhi.databridge.exception.ValidationErrorException;
import com.haizhi.databridge.util.ErrorsUtils;
//import com.haizhi.sdk.exception.SDKException;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice {

	@ResponseBody
	@ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class})
	public WebResult<?> handleException(Exception exception) throws Exception {
		return WebResult.fail(exception, null);
	}

	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public WebResult<?> handleException(MethodArgumentNotValidException exception) {
		return WebResult.fail(exception, ErrorsUtils.compositeValiditionError(exception.getBindingResult()));
	}

	@ResponseBody
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public WebResult<?> handleException(MissingServletRequestParameterException exception) throws Exception {
		return WebResult.fail(exception, exception.getParameterName());
	}

	/**
	 * web层校验
	 *
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@ExceptionHandler(value = {ValidationErrorException.class})
	public WebResult<?> validationException(ValidationErrorException exception) {
		return WebResult.fail(exception, exception.getBody()).setStatus(StatusCode.PARAMETERS_INVALID);
	}

	/**
	 * jpa层的校验
	 *
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = {ConstraintViolationException.class})
	public WebResult<?> validationException(ConstraintViolationException exception) throws Exception {
		return WebResult.fail(exception,
			exception.getConstraintViolations()
				.stream()
				.collect(Collectors.toMap(
					ConstraintViolation::getPropertyPath,
					ConstraintViolation::getMessage,
					(m1, m2) -> m1 + m2))
		);
	}

//	/**
//	 * 统一处理client异常
//	 *
//	 * @param sdkException
//	 * @return
//	 */
//	@ExceptionHandler({SDKException.class})
//	@ResponseBody
//	public ResponseEntity<?> sdkExceptionHandler(SDKException sdkException) {
//		Throwable r = ErrorsUtils.getRootCause(sdkException);
//		WebResult result = WebResult.fail(r);
//		// TODO 临时用反射获取枚举值，后续SDKException返回状态码使用枚举值，需将hora的状态码提至公共common
//		Optional<StatusCode> enum1 = EnumUtil.getEnumObject(StatusCode.class, e -> e.getValue() == sdkException.getStatus());
//		StatusCode statusCode = enum1.orElse(StatusCode.API_INTERNAL_ERROR);
//		result.setStatus(statusCode);
//		if (result.getErrstr().startsWith("Failed to connect to")) {
//			result.setStatus(StatusCode.TASSADAR_CONNECTION_ERROR);	// all client connection error
//		}
//		return ResponseEntity
//			.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//			.body(result);
//	}
//
//	/**
//	 * 统一处理自定义异样，根据状态码返回
//	 *
//	 * @param horaException
//	 * @return
//	 */
//	@ExceptionHandler({HoraException.class})
//	@ResponseBody
//	public ResponseEntity<?> horaExceptionHandler(HoraException horaException) {
//		Throwable r = ErrorsUtils.getRootCause(horaException);
//		WebResult result = WebResult.fail(r);
//		result.setStatus(horaException.getStatus());
//		return ResponseEntity
//			.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//			.body(result);
//	}

	/**
	 * 统一处理Controller中的异常 需要统一处理异常编码
	 *
	 * @param runtimeException
	 * @return
	 */
	@ExceptionHandler({RuntimeException.class})
	@ResponseBody
	public ResponseEntity<?> runtimeExceptionHandler(RuntimeException runtimeException) {
		Throwable r = ErrorsUtils.getRootCause(runtimeException);
		WebResult result = WebResult.fail(r);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.body(result);
	}

	/**
	 * @description: 集中处理所有未显示捕获的异常信息
	 * @param: ${tags}
	 * @return: ${return_type}
	 * @throws
	 * @author WangChengYu
	 * @date 2020-02-06 23:23
	 */
	@ResponseBody
	@ExceptionHandler(value = {Exception.class, Error.class})
	public WebResult<?> handleUncatchException(Error exception) {
		return WebResult.fail(exception, null);
	}
}
