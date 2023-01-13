package com.authservice.dto;



public record LoginRequest (
        String username,
        String password
){
}
