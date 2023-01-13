package com.authservice.util.customexception;

public class ResourcesNotFounded extends RuntimeException{

    public ResourcesNotFounded() {
    }

    public ResourcesNotFounded(String message) {
        super(message);
    }
}
