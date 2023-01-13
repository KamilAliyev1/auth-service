package com.authservice.service;


import com.authservice.dto.MailDTO;
import com.authservice.dto.UserSecurityDetailGetDTO;
import com.authservice.dto.UserSecurityDetailSaveDTO;
import com.authservice.dto.UserSecurityDetailUpdateDTO;
import com.authservice.model.TokenPair;
import com.authservice.model.TokenType;
import com.authservice.model.UserSecurityDetail;
import com.authservice.repository.UserSecurityDetailDao;
import com.authservice.util.CustomService;
import com.authservice.util.customexception.IllegalRequest;
import com.authservice.util.customexception.ResourcesNotFounded;
import com.authservice.util.customexception.UniqueException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class UserSecurityDetailService implements UserDetailsService, CustomService<UserSecurityDetailSaveDTO, UserSecurityDetailUpdateDTO> {

    @Value("${verification-url}")
    String verificationUrl;

    public String getVerificationUrl() {
        return verificationUrl;
    }

    public void setVerificationUrl(String verificationUrl) {
        this.verificationUrl = verificationUrl;
    }

    private final TokenService tokenService;

    private final UserSecurityDetailDao userSecurityDetailDao;

    private final PasswordEncoder passwordEncoder;

    private final BrokerService brokerService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user =  userSecurityDetailDao.findByEmail(username)
                .orElseThrow(
                        ()->
                        new IllegalStateException(
                            String.format("%s not founded",username)
                        )
                );

        return user;
    }

    private UserSecurityDetailGetDTO map(UserSecurityDetail detail){

        return UserSecurityDetailGetDTO.builder()
                .ID(detail.getID())
                .email(detail.getEmail())
                .auths(detail.getGrantedAuths())
                .isCredentialsNonExpired(detail.isCredentialsNonExpired())
                .isEnabled(detail.isEnabled())
                .isAccountNonLocked(detail.isAccountNonLocked())
                .isAccountNonExpired(detail.isAccountNonExpired())
                .build();
    }


    @Override
    public UserSecurityDetailGetDTO findById(Long id) {

        var temp = userSecurityDetailDao.findById(id);

        if(temp.isEmpty())throw new ResourcesNotFounded();

        UserSecurityDetail detail = temp.get();

        return  map(detail);


    }

    @Override
    public List<UserSecurityDetailGetDTO> findAll() {

        List<UserSecurityDetail> userSecurityDetails = userSecurityDetailDao.findAll();

        return userSecurityDetails.stream().map(
                t->map(t)
        ).collect(Collectors.toList());

    }

    @Override
    public UserSecurityDetailGetDTO update(UserSecurityDetailUpdateDTO obj, Long id) {

        if(id==null || !existsById(id)) {
            throw new ResourcesNotFounded();
        }


        UserSecurityDetail userSecurityDetail = userSecurityDetailDao.findById(id).get();


        userSecurityDetail.setEmail(obj.email()!=null?obj.email(): userSecurityDetail.getEmail());

        userSecurityDetail.setPassword(obj.password()!=null?passwordEncoder.encode(obj.password()): userSecurityDetail.getPassword());

        var entity = userSecurityDetailDao.saveAndFlush(userSecurityDetail);

        if(obj.email()!=null)
            emailVerification(userSecurityDetail);


        return map(entity);
    }

    @Override
    public boolean existsById(Long id) {

        return userSecurityDetailDao.existsById(id);

    }

    @Override
    public void deleteById(Long id) {

        userSecurityDetailDao.deleteById(id);

    }


    @Transactional
    @Override
    public UserSecurityDetailGetDTO save(UserSecurityDetailSaveDTO obj) {

        if(userSecurityDetailDao.existsByEmail(obj.email()))throw new UniqueException();


        UserSecurityDetail userSecurityDetail = UserSecurityDetail.builder()
                .email(obj.email())
                .password(passwordEncoder.encode(obj.password()))
                .isAccountNonExpired(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isEnabled(false)
                .build();


        var entity = userSecurityDetailDao.saveAndFlush(userSecurityDetail);

        emailVerification(entity);

        return map(entity);

    }

    public void emailVerification(UserSecurityDetail userSecurityDetail){

        JWT jwt;

        jwt  = tokenService
                .getJwt(
                        userSecurityDetail.getID().toString()
                        , "",
                        userSecurityDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
                        , TokenType.ACCESS
                );

        String token = jwt.serialize();

        TokenPair tokenPair = new TokenPair(userSecurityDetail.getID(), token,null);

        tokenService.saveToken(tokenPair);

        MailDTO mailDTO = new MailDTO(
                getVerificationUrl()+"?token="+token
                ,userSecurityDetail.getEmail()
                ,"verification");

        brokerService.sendToQueue(mailDTO);

    }

    public void setEnabled(String token){

        Long id = Long.parseLong(
                String.valueOf(tokenService.verifyJws(token).get("sub"))
        );

        tokenService.validate(id,token);

        userSecurityDetailDao.setEnableWithId(id);
    }

}
