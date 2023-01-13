package com.authservice.model;

public enum TokenType {

    ACCESS,REFRESH;

    private long validTime;

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }
}
