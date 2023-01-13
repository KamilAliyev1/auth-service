package com.authservice.controller;


import com.authservice.service.TokenService;
import com.authservice.service.UserSecurityDetailService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;



@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class EmailVerificationController {

    private final TokenService tokenService;

    private final UserSecurityDetailService userSecurityDetailService;



    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/emailverification")
    public void verification(@RequestParam String token){
        userSecurityDetailService.setEnabled(token);
    }
}
