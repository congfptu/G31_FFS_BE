package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface JobRequestRepository extends JpaRepository<Job,String> {
    @Modifying
    @Transactional
    @Query(value = " insert into job_request (job_id,freelancer_id,status,apply_date) values (:job_id,:freelancer_id,:status,:date)"
            , nativeQuery = true)
    Integer insert(Integer job_id, String freelancer_id, int status, LocalDateTime date);
    @Query(value = " SELECT DISTINCT job FROM Freelancer f" +
            " LEFT JOIN  f.jobRequests jrq" +
            " LEFT JOIN  jrq.job job" +
            " LEFT JOIN fetch job.subCareer sub" +
            " WHERE f.id=:freelancer_id and (jrq.status=:status or :status=-1 )",
            countQuery = " SELECT count(job.id)  FROM Freelancer f" +
                    " LEFT JOIN  f.jobRequests jrq" +
                    " LEFT JOIN  jrq.job job " +
                    " LEFT JOIN  job.subCareer sub" +
                    " WHERE f.id=:freelancer_id and (jrq.status=:status or :status=-1 )")
    Page<Job> getJobRequestWithStatus(String freelancer_id,int status,Pageable pageable);



}
