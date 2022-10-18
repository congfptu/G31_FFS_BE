package com.example.g31_ffs_be.repository;



import com.example.g31_ffs_be.model.Staff;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff,String> {


}
