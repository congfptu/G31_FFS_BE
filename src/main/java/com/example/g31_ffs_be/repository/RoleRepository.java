package com.example.g31_ffs_fe.repository;


import com.example.g31_ffs_fe.model.Role;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

}
