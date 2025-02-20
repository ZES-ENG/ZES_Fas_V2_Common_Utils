package com.zes.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final HttpStatus status; // HTTP status code
    private final String message;    // Error message
    private final String data;       // Additional data (if any)

    // Constructor to set status and message
    public AppException(HttpStatus status, String message) {
        super(message); // Call to parent class constructor
        this.status = status;
        this.message = message;
        this.data = null; // No additional data by default
    }

    // Constructor to set status, message, and additional data
    public AppException(HttpStatus status, String message, String data) {
        super(message); // Call to parent class constructor
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
