package com.example.g31_ffs_be.exception;

import org.springframework.http.HttpStatus;

public class JwtException extends RuntimeException{
    private HttpStatus status;
    private String message;
    public JwtException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
