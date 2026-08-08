package com.library.exception;

import com.library.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(404).body(
                new ErrorResponse(404, ex.getMessage(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity.status(500).body(
                new ErrorResponse(500, "Something went wrong: " + ex.getMessage(), LocalDateTime.now())
        );
    }
}