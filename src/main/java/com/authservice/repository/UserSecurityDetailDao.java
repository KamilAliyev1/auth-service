package com.authservice.repository;


import com.authservice.model.UserSecurityDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserSecurityDetailDao extends JpaRepository<UserSecurityDetail,Long> {


    Optional<UserSecurityDetail> findByEmail(String email);


    boolean existsByEmail(String email);


    @Transactional
    @Modifying
    @Query(value = "UPDATE usersecuritydetails SET is_enabled=1 WHERE id=:id",nativeQuery = true)
    int setEnableWithId(@Param("id") Long id);




}
