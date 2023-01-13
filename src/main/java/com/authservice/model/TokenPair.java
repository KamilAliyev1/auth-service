package com.authservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash(value = "token-pair",timeToLive = 5000)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenPair implements Serializable {

    @Id
    private Long ID;

    private String accessToken;

    private String refreshToken;

}
