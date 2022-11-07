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
public interface PostRepository extends JpaRepository<Job, Integer> {
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
    @Query(value = " SELECT j FROM Job j" +
            " LEFT JOIN FETCH j.createBy cre" +
            " LEFT JOIN FETCH cre.user u " +
            " LEFT JOIN FETCH cre.jobs jobCreate" +
            " LEFT JOIN FETCH u.account acc" +
            " LEFT JOIN FETCH jobCreate.jobRequests" +
            " LEFT JOIN FETCH u.feedbackTos" +
            " LEFT JOIN FETCH j.freelancers free" +

            " LEFT JOIN FETCH j.skills " +
            " LEFT JOIN FETCH j.subCareer " +
            " LEFT JOIN FETCH j.jobRequests rq" +
            " LEFT JOIN FETCH j.approvedBy staff" +
            " WHERE (j.id=:id)  "
            )
    Job getJobDetail(int id);
/*@Query(value = " SELECT DISTINCT j FROM Job j " +
        " LEFT JOIN  j.skills s "+
        " LEFT JOIN FETCH j.subCareer sub "+
        " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
        " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
        " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"
        + "Order by j.topTime desc,j.budget desc"
        , countQuery = " SELECT count( distinct j.id) FROM Job j " +
        " LEFT JOIN  j.skills s "+
        " LEFT JOIN  j.subCareer sub "+
        " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
        " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
        " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"
       + "Order by j.topTime desc,j.budget desc"
   )
    Page<Job> getAllJobNormal(String area,String keyword,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"
            + "Order by j.topTime desc,j.budget desc"
            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"
            + "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobMemberShip(String area,String keyword,Pageable pageable);

    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"+
            " and (sub.id=:subCareerId)"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"+
            " and (sub.id=:subCareerId)"
            + "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobNormalWithSub(String area,String keyword,int subCareerId,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (sub.id=:subCareerId)"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (sub.id=:subCareerId) "
            + "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobMemberShipWithSub(String area,String keyword,int subCareerId,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"+
            " and j.isActive=true and j.isApproved=1"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"
            + "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobNormalWithPaymentType(String area,String keyword,int paymentType,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"
            + "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobMemberShipWithPaymentType(String area,String keyword,int paymentType,Pageable pageable);*/


    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType or :paymentType=-1)"+
            " and (sub.id=:subCareerId or :subCareerId=-1)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType or :paymentType=-1)"+
            " and (sub.id=:subCareerId or :subCareerId=-1)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"+
             "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobNormalSearchAll(String area,String keyword,int paymentType,int subCareerId,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType or :paymentType=-1)"+
            " and (sub.id=:subCareerId or :subCareerId=-1)"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType or :paymentType=-1)"+
            " and (sub.id=:subCareerId or :subCareerId=-1)"+
            "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobMemberShipSearchAll(String area,String keyword,int paymentType,int subCareerId,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Recruiter r " +
            " LEFT JOIN  r.jobs j "+
            " LEFT JOIN  j.jobRequests "+
            " LEFT JOIN fetch j.subCareer sub "+
            " WHERE r.id=:recruiterId and (j.isApproved=:status or :status=-1) "+
             "Order by j.time desc"
            , countQuery = "SELECT count(DISTINCT j.id) FROM Recruiter r " +
            " LEFT JOIN  r.jobs j "+
            " LEFT JOIN  j.jobRequests "+
            " LEFT JOIN  j.subCareer sub "+
            " WHERE r.id=:recruiterId and (j.isApproved=:status or :status=-1) "+
            "Order by j.time desc"
    )
    Page<Job> getAllJobPosted(String recruiterId, int status, Pageable pageable);


   /* @Query(value = " select count(*) from jobs a " +
            "inner join job_request b on a.id=b.job_id " +
            "inner join recruiter c on c.recruiter_id=a.create_by " +
            "where c.recruiter_id=:recruiterId",nativeQuery = true)
    Integer getTotalAppliedById(String recruiterId);*/

    @Modifying
    @Transactional
    @Query(value = " insert into job_skill (job_id,skill_id) values (:jobId,:skillId)"
            , nativeQuery = true)
    Integer insertJobSkill(int jobId, int skillId);
    @Modifying
    @Transactional
    @Query(value = " delete from job_skill  where job_id=:jobId and skill_id=:skillId"
            , nativeQuery = true)
    Integer deleteJobSkill(int jobId, int skillId);






}
