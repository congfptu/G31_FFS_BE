package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.JobSaved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface JobSavedRepository extends JpaRepository<JobSaved,Integer> {
    @Modifying
    @Transactional
    @Query(value = " insert into job_saved (job_id,freelancer_id) values (:job_id,:freelancer_id)"
            , nativeQuery = true)
    Integer insert(Integer job_id, String freelancer_id);
}
