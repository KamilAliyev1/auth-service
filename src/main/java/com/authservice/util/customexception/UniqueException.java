package com.authservice.util.customexception;

public class UniqueException extends RuntimeException{
    public UniqueException() {
    }

    public UniqueException(String message) {
        super(message);
    }
}
