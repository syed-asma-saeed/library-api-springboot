package com.library.dto.response;

import java.time.LocalDateTime;

// ErrorResponse DTO:
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
    // getters
}