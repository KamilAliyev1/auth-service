package com.authservice.controller;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/jwk")
public class JwkController {
    private final RSAKey rsaKey;


    @GetMapping
    public ResponseEntity getPublicKey() throws JOSEException {
        log.info("PUBLIC KEY REQUESTED");
        JWKSet jwkSet = new JWKSet(rsaKey.toPublicJWK());
        return ResponseEntity.ok(jwkSet.toPublicJWKSet().toJSONObject(true));
    }

}
