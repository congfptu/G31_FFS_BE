package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Career;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends JpaRepository<Career, Integer> {
    @Query(value = " SELECT * FROM career " +
            " WHERE name LIKE CONCAT('%',:name,'%') "
            , nativeQuery = true)
    Page<Career> getCareerByName(String name, Pageable pageable);
    @Query(value = " SELECT * FROM career " +
            " WHERE name =:name"
            , nativeQuery = true)
   Career getCareerByName(String name);
}
