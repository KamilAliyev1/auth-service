package com.authservice.util.customexception;

public class IllegalRequest extends RuntimeException{
    public IllegalRequest() {

    }

    public IllegalRequest(String message) {
        super(message);
    }
}
