package com.authservice.repository;

import com.authservice.model.TokenPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepo extends CrudRepository<TokenPair,Long> {
}
