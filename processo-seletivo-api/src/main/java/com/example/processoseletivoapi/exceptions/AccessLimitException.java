package com.example.processoseletivoapi.exceptions;

public class AccessLimitException extends RuntimeException {
    public AccessLimitException(String message) {
        super(message);
    }
}
