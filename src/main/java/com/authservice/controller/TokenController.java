package com.authservice.controller;


import com.authservice.dto.LoginRequest;
import com.authservice.service.AuthenticationService;
import com.authservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController("TokeControllerV1")
@RequestMapping("/tokens")
public class TokenController {

    private final AuthenticationService authenticationService;

    @PostMapping("/refresh")
    public ResponseEntity<?> fromRefreshToken(@RequestParam String token){
        return ResponseEntity.ok(authenticationService.fromRefreshToken(token));
    }

    @PostMapping("/credential")
    public ResponseEntity<?> fromUserCredentials(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.fromCredential(loginRequest));
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/validate")
    public void validateTokenFromDB(@RequestParam Long id,@RequestParam String token){
        authenticationService.validateTokenFromDB(id,token);
    }

}