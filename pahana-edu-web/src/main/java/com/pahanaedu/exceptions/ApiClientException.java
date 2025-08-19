package com.pahanaedu.exceptions;

public class ApiClientException extends Exception {
    private final int statusCode;

    public ApiClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}