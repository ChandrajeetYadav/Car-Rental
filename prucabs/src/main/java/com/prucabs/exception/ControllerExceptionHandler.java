package com.prucabs.exception;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final String STATUS = "status";
	private static final String ERROR = "error";
	private static final String ERRORS = "errors";
	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> responseMap = new DefaultErrorAttributes().getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,ErrorAttributeOptions.Include.MESSAGE,ErrorAttributeOptions.Include.BINDING_ERRORS));
		responseMap.remove(ERRORS);
		responseMap.remove(EXCEPTION);
				responseMap.put(STATUS, HttpStatus.BAD_REQUEST.value());
		responseMap.put(ERROR, HttpStatus.BAD_REQUEST.name());
		responseMap.put(MESSAGE, ex.getBindingResult().getFieldErrors().stream()
				.map(e -> e.getField() + " - " + e.getDefaultMessage()).collect(Collectors.toList()));
		return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({OrdersException.class})
	public ResponseEntity<Object> handleGenericException(OrdersException ex, WebRequest request) {
		Map<String, Object> responseMap = new DefaultErrorAttributes().getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,ErrorAttributeOptions.Include.MESSAGE,ErrorAttributeOptions.Include.BINDING_ERRORS));
		responseMap.remove(EXCEPTION);
		responseMap.put(STATUS, HttpStatus.PRECONDITION_FAILED.value());
		responseMap.put(ERROR, ex.getMessage());
		responseMap.put("DATA", ((OrdersException) ex).getMessage());
		return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
	}
	
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
		Map<String, Object> responseMap = new DefaultErrorAttributes().getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION,ErrorAttributeOptions.Include.MESSAGE,ErrorAttributeOptions.Include.BINDING_ERRORS));
		responseMap.remove(EXCEPTION);
		if (ex instanceof BadRequestException) {
			responseMap.put(STATUS, HttpStatus.BAD_REQUEST.value());
			responseMap.put(ERROR, ex.getMessage());
			responseMap.put("DATA", ((BadRequestException) ex).getMessage());
			return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.BAD_REQUEST);
		} else if (ex instanceof CarNotAvailableException) {
			responseMap.put(STATUS, HttpStatus.PRECONDITION_FAILED.value());
			responseMap.put(ERROR, ex.getMessage());
			responseMap.put("DATA", ((CarNotAvailableException) ex).getMessage());
			return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED);
		}
		
		responseMap.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
		responseMap.put(ERROR, ex.getMessage());
		return new ResponseEntity<>(responseMap, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
