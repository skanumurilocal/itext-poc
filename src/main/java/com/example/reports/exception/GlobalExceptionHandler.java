package com.example.reports.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        // Return the ErrorResponse object with an appropriate HTTP status code
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Add more exception handling methods if needed for specific exceptions

    // Example: Handling a specific custom exception
    @ExceptionHandler(ReportsException.class)
    public ResponseEntity<Object> handleCustomException(ReportsException ex) {
        // Return the ErrorResponse object with a specific HTTP status code
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

