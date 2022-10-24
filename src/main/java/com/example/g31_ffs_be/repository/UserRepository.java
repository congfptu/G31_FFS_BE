package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;




public interface UserRepository extends JpaRepository<User,String> {
    @Query(value = " SELECT u.* FROM `user` u WHERE u.verification_code like :code",nativeQuery = true)
     User findByVerificationCode(String code);

}
