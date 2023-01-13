package com.authservice.controlleradvice;

import com.authservice.util.customexception.IllegalRequest;
import com.authservice.util.customexception.ResourcesNotFounded;
import com.authservice.util.customexception.TokenVerifyException;
import com.authservice.util.customexception.UniqueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class UserSecurityDetailControllerAdvice {



    @ExceptionHandler(value = {ResourcesNotFounded.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    void notFound(Exception e){
        log.error(e.getMessage(),e);
    }


    @ExceptionHandler(value = {
                    UniqueException.class,
                    SQLIntegrityConstraintViolationException.class,
                    jakarta.validation.ConstraintViolationException.class
            }
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    void constraintViolation(Exception e){
        log.error(e.getMessage(),e);
    }

    @ExceptionHandler(value = {IllegalRequest.class, TokenVerifyException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    void illegal(Exception e){
        log.error(e.getMessage(),e);
    }






}
