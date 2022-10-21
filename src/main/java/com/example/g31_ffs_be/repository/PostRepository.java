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
            " WHERE description LIKE CONCAT('%',:keyword,'%') and is_approved=:status"
            , nativeQuery = true)
    Page<Job> getRequestPostByStatusAndDescription(String keyword, Boolean status, Pageable pageable);

    @Query(value = " SELECT * FROM jobs " +
            " WHERE id=:id "
            , nativeQuery = true)
    Job getJobDetail(String id);
}
