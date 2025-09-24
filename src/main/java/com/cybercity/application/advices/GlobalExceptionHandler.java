package com.cybercity.application.advices;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cybercity.application.exceptions.RegistrationFailException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	 @ExceptionHandler(RegistrationFailException.class)
	    public ResponseEntity<ApiResponse<?>> handleRegistrationFail(RegistrationFailException ex) {
	        ApiError apiError = new ApiError(
	                HttpStatus.BAD_REQUEST,
	                ex.getMessage(),
	                Collections.emptyList()  // no sub-errors for now
	        );
	        return ResponseEntity.badRequest().body(new ApiResponse<>(apiError));
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
	        ApiError apiError = new ApiError(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                ex.getMessage(),
	                Collections.emptyList()
	        );
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(new ApiResponse<>(apiError));
	    }
}
