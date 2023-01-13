package com.authservice.util;


import com.authservice.util.customexception.ResourcesNotFounded;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import java.util.List;



@RequiredArgsConstructor
public abstract class CustomRestController<SAVETYPE,UPDATETYPE> {

    protected final CustomService<SAVETYPE,UPDATETYPE> service;


    @PreAuthorize("hasAuthority('USER_GET_All') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAll(){

        List<?> objectList;

        objectList = service.findAll();

        return new ResponseEntity<>(objectList,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody SAVETYPE obj, Errors errors){

        Object dto = service.save(obj);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PreAuthorize("#id.toString() == authentication.principal or hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){

        Object obj = service.findById(id);


        return ResponseEntity.ok(obj);
    }

    @PreAuthorize("#id.toString() == authentication.principal or hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> change(@PathVariable("id") Long id,@Valid @RequestBody UPDATETYPE obj,Errors errors){


        if(!service.existsById(id)) throw new ResourcesNotFounded();

        Object dto = service.update(obj,id);


        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

    @PreAuthorize("#id.toString() == authentication.principal or hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUserSecurityDetail(@PathVariable("id") Long id){

        service.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

}
