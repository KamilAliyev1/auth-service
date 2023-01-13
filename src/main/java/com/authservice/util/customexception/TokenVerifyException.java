package com.authservice.util.customexception;

public class TokenVerifyException extends RuntimeException{
    public TokenVerifyException() {
    }

    public TokenVerifyException(String message) {
        super(message);
    }
}
