package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
  @Query(value = "select r from Role r "+
          " left join fetch r.services se"+
          " left join fetch r.benefits "+
          "where r.roleName like :roleName " )
  Role findByRoleName(String roleName);
}
