package com.devsuperior.bds04.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // allows error interception/capture
public class ResourceExceptionHandler {

	
	//MethodArgumentNotValidException
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException error, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; //422
		ValidationError err = new ValidationError();

		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); 
		err.setError("Validation exception");
		err.setMessage(error.getMessage()); // Message thrown by Service layer
		err.setPath(request.getRequestURI());

		for (FieldError fe : error.getBindingResult().getFieldErrors()) {
			err.addError(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}
	

}
