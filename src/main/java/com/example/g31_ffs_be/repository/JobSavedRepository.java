package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
public interface JobSavedRepository extends JpaRepository<Job,String> {
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

    @Query(value = " select count(*) from job_saved where job_id=:job_id and freelancer_id=:freelancer_id"
            , nativeQuery = true)
    Integer getJob(Integer job_id, String freelancer_id);

    @Query(value = " SELECT DISTINCT job FROM Freelancer f" +
                   " LEFT JOIN  f.jobSaves job" +
                   " LEFT JOIN fetch job.subCareer" +
                    " WHERE f.id=:freelancer_id",
            countQuery = " SELECT count(job.id)  FROM Freelancer f" +
                    " LEFT JOIN  f.jobSaves job" +
                    " LEFT JOIN  job.subCareer" +
                    " WHERE f.id=:freelancer_id")
    Page<Job> getAllJobSaved(String freelancer_id, Pageable pageable);
}
