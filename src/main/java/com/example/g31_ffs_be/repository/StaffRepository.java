package com.example.g31_ffs_fe.repository;


import com.example.g31_ffs_fe.model.Staff;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends CrudRepository<Staff,String> {

}
