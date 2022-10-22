package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> {
 Account findByEmail(String email);
}
