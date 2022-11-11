package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AccountRepository extends JpaRepository<Account,String> {
 @Query(value = " SELECT distinct a FROM Account a " +
         "LEFT JOIN FETCH a.role r "+
         "LEFT JOIN FETCH a.staff s "+
         "LEFT JOIN FETCH a.user u "+
         "LEFT JOIN FETCH u.recruiter re "+
         "where a.email like :email")
 Account findByEmail(String email);
 @Query(value = " SELECT distinct a FROM Account a " +
         "LEFT JOIN FETCH a.user u " +
         "LEFT JOIN FETCH a.role r "+
         "where a.id like :id")
 Account findByUserId(String id);

/* @Query(value = " SELECT a.*,c.role_id FROM `account` a " +
               "LEFT JOIN FETCH `user` b  on a.id=b.user_id " +
                "LEFT JOIN FETCH  account_role c on a.id=c.account_id "+
               "where b.reset_password_token like :resetPasswordToken",nativeQuery = true)*/
 @Query(value = " SELECT distinct a FROM Account a " +
         "LEFT JOIN FETCH a.user u " +
         "LEFT JOIN FETCH a.role r "+
         "where u.resetPasswordToken like :resetPasswordToken")
 Account findByResetPasswordToken(String resetPasswordToken);

}
