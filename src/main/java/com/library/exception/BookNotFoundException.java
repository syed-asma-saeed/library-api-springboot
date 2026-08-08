package com.library.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}

/*
RuntimeException not Exception. In Spring Boot, unchecked exceptions work better with @ControllerAdvice. You don't need throws declarations everywhere.
 */