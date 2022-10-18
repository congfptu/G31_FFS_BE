package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,String> {

}
