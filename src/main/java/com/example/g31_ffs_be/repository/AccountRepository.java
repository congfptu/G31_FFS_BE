package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AccountRepository extends JpaRepository<Account,String> {
 Account findByEmail(String email);
 @Query(value = " SELECT a.*,c.role_id FROM `account` a " +
               "inner join `user` b  on a.id=b.user_id " +
                "inner join account_role c on a.id=c.account_id "+
               "where b.reset_password_token like :resetPasswordToken",nativeQuery = true)
 Account findByResetPasswordToken(String resetPasswordToken);
 void deleteAccountByEmail(String email);
}
