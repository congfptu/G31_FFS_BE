package com.example.g31_ffs_be.repository;



import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.Staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,String> {
    @Query(value = "select a.* from `staff` a " +
            " inner join `account` b on a.id=b.id " +
            " where a.fullname like CONCAT('%',:name,'%') and b.email like CONCAT('%',:name,'%') "+
            " Order by a.fullname asc,b.email asc ", nativeQuery = true)
    Page<Staff> getStaffByName(String name, Pageable pageable);
}
