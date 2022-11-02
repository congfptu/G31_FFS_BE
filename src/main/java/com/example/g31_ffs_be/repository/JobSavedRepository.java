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
    @Modifying
    @Transactional
    @Query(value = " delete from job_saved where job_id=:job_id and freelancer_id=:freelancer_id"
            , nativeQuery = true)
    Integer delete(Integer job_id, String freelancer_id);

    @Query(value = " select *from job_saved where job_id=:job_id and freelancer_id=:freelancer_id"
            , nativeQuery = true)
    JobSaved getJob(Integer job_id, String freelancer_id);
}
