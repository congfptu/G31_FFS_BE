package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


public interface UserRepository extends JpaRepository<User,String> {
    @Query(value = " SELECT u.* FROM `user` u WHERE u.verification_code like :code",nativeQuery = true)
     User findByVerificationCode(String code);
    @Modifying
    @Transactional
    @Query(value = " insert into user_service (user_id,service_id,date_buy,service_price) values (:user_id,:service_id,:date_buy,:service_price)"
            , nativeQuery = true)
    Integer insertUserService(String user_id, int service_id, LocalDateTime date_buy, double service_price);

    @Query(value = " SELECT  u FROM User u " +
            "where u.id like :id")
    User findByUserId(String id);



}
