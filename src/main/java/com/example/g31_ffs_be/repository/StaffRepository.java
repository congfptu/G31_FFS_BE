package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface StaffRepository extends JpaRepository<Staff,String> {
   @Query(value = "SELECT s from Staff s "+
           "LEFT JOIN FETCH s.account a " +
           "where s.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')",
           countQuery = "SELECT count(s.id) from Staff s "+
                   "LEFT JOIN  s.account a " +
                   "where s.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')")
    Page<Staff> getStaffByName(String name, Pageable pageable);
}
