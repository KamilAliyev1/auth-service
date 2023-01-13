package com.authservice.dto;



public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}
