package com.authservice.config;


import com.authservice.filter.JwtTokenVerifyAuthorize;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenVerifyAuthorize jwtTokenVerifyAuthorize;


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,"/api/v1/usersecuritydetails/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/emailverification/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/jwk/**").permitAll()
                .requestMatchers("/tokens/**").permitAll()
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()
                .addFilterBefore(jwtTokenVerifyAuthorize, UsernamePasswordAuthenticationFilter.class)


        ;

        log.info("SecurityFilterChain CREATED");

        return http.build();

    }


}
