package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.AccountRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRoleRepository extends CrudRepository<AccountRole,Integer> {

}
