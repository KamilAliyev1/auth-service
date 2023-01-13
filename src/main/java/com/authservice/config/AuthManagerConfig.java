package com.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@Slf4j
@Configuration
public class AuthManagerConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;



    @Bean
    AuthenticationManager authenticationManager(){

        var aut = new DaoAuthenticationProvider();

        aut.setPasswordEncoder(passwordEncoder);

        aut.setUserDetailsService(userDetailsService);

        log.info("AuthenticationManager CREATED");

        return new ProviderManager(aut);

    }
}
