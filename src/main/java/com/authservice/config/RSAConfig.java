package com.authservice.config;


import com.nimbusds.jose.jwk.RSAKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Slf4j
@Configuration
public class RSAConfig {


    @Bean
    protected RSAKey generateRsaKey() throws NoSuchAlgorithmException {

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

        keyPairGenerator.initialize(2048);


        KeyPair keyPair = keyPairGenerator.generateKeyPair();


        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();


        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey =  new RSAKey.Builder(rsaPublicKey)
                .keyID(UUID.randomUUID().toString())
                .privateKey(rsaPrivateKey)
                .build();

        log.info("RSAKey CREATED");

        return rsaKey;
    }

}
