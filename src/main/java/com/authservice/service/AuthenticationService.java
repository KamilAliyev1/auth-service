package com.authservice.service;

import com.authservice.dto.LoginRequest;
import com.authservice.dto.TokenDTO;
import com.authservice.model.TokenPair;
import com.authservice.model.UserAuthority;
import com.authservice.model.UserSecurityDetail;
import com.authservice.util.customexception.IllegalRequest;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AuthenticationService {


    private final TokenService  tokenService;
    private final AuthenticationManager authenticationManager;

    private final UserSecurityDetailService userSecurityDetailService;

    public TokenDTO fromCredential(LoginRequest loginRequest){

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        );


        authentication = authenticationManager.authenticate(authentication);


        if(!authentication.isAuthenticated())throw new IllegalRequest("Has no auth");

        UserSecurityDetail userSecurityDetail = (UserSecurityDetail) authentication.getPrincipal();

        var token = tokenService.getTokenPairs(
                userSecurityDetail.getID()
                ,""
                , authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
        );

        tokenService.saveToken(token);


        return map(token);


    }

    TokenDTO map(TokenPair tokenPair){
        return new TokenDTO(tokenPair.getAccessToken(),tokenPair.getRefreshToken());
    }

    public void authorize(String token){

        Map<String ,Object > userdetails = tokenService.verifyJws(token);


        Long id = Long.parseLong(
                String.valueOf(
                        userdetails.get("sub")
                )
        );

        tokenService.validate(
                id
                ,token
        );

        List<String > auths = (List<String>) userdetails.get("auths");

        List<? extends GrantedAuthority> grantedAuthorities = auths.stream().map(e->new SimpleGrantedAuthority(e)).collect(Collectors.toList());

        var user = userSecurityDetailService.findById(id);

        if(!user.isAccountNonExpired())throw new AccountExpiredException("account expired");
        if(!user.isAccountNonLocked())throw new LockedException("account locked");
        if(!user.isCredentialsNonExpired())throw new CredentialsExpiredException("credentials expired");
        if(!user.isEnabled()) throw new DisabledException("account disabled");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                id.toString()
                , null
                , grantedAuthorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    public TokenDTO fromRefreshToken(String token){

        Map<String ,Object> map = tokenService.verifyJws(token);

        Long id = Long.parseLong(
                String.valueOf(
                        map.get("sub")
                )
        );

        tokenService.validate(
                id
                ,token
        );


        var getDTO = userSecurityDetailService.findById(id);

        if(!getDTO.isAccountNonExpired())throw new AccountExpiredException("account expired");
        if(!getDTO.isAccountNonLocked())throw new LockedException("account locked");
        if(!getDTO.isCredentialsNonExpired())throw new CredentialsExpiredException("credentials expired");
        if(!getDTO.isEnabled()) throw new DisabledException("account disabled");



        var tokenPair =  tokenService.getTokenPairs(
                id
                ,  null
                ,getDTO.auths().stream().map(t->t.getPermission().name()).collect(Collectors.toList())
        );

        tokenService.saveToken(tokenPair);

        return map(tokenPair);

    }

    public void validateTokenFromDB(Long id,String token){

        tokenService.validate(id,token);
        // TODO: 1/13/2023 DIGER SERVISLERDEN TOKEN GELENDE SADECE TOKENI DB DEN YOXLAMALIYAM YOXSA
        // TODO: 1/13/2023 USERIDE YOXLAMALIYAM?

    }
}
