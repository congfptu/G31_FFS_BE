package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Job;
import com.example.g31_ffs_be.model.RequestPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Job, String> {
    @Query(value = " SELECT * FROM jobs " +
            " WHERE (description LIKE CONCAT('%',:keyword,'%') " +
            "or job_title LIKE CONCAT('%',:keyword,'%'))"+
            " and is_approved LIKE CONCAT('%',:status,'%')"
            , nativeQuery = true)
    Page<Job> getRequestPostByStatusAndDescription(String keyword, String status, Pageable pageable);
    @Query(value = " SELECT * FROM jobs "+
            "WHERE description LIKE CONCAT('%',:keyword,'%')"
            , nativeQuery = true)
    Page<Job> getRequestPostSearchByNamePaging(String keyword,Pageable pageable);
    @Query(value = " SELECT * FROM jobs " +
            " WHERE id=:id "
            , nativeQuery = true)
    Job getJobDetail(String id);
    @Query(value = " SELECT * FROM jobs " +
            "where area LIKE CONCAT('%',:keyword1,'%') and budget LIKE CONCAT('%',:keyword2,'%') "
            , nativeQuery = true)
    Page<Job> getJobSearch(String keyword1,String keyword2,Pageable pageable);
    @Query(value = " SELECT * FROM jobs " +
            "where area LIKE CONCAT('%',:keyword1,'%') and budget LIKE CONCAT('%',:keyword2,'%') and sub_career_id=:sub_career_id"
            , nativeQuery = true)
    Page<Job> getJobSearch(String keyword1,String keyword2,String sub_career_id,Pageable pageable);
}
