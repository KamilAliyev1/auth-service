package com.authservice.config.properties;


import com.authservice.model.TokenType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Map;

@Configuration
@ConfigurationProperties(
        prefix = "jwt"
)
public class TokenConfigProperties {

    private String issuer;


    private Map<String ,String > validSeconds;


    @PostConstruct
    private void init(){

        long validSecondNum = Long.parseLong(validSeconds.get("refresh"));

        TokenType.REFRESH.setValidTime(validSecondNum);

        validSecondNum = Long.parseLong(validSeconds.get("access"));

        TokenType.ACCESS.setValidTime(validSecondNum);

    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Map<String, String> getValidSeconds() {
        return validSeconds;
    }

    public void setValidSeconds(Map<String, String> validSeconds) {
        this.validSeconds = validSeconds;
    }



}
