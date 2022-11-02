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
    @Query(value = " SELECT j FROM Job j" +
            " LEFT JOIN FETCH j.skills " +
            " LEFT JOIN FETCH j.subCareer " +
            " LEFT JOIN FETCH j.createBy " +
            "WHERE j.id=:id "
            )
    Job getJobDetail(int id);
@Query(value = " SELECT DISTINCT j FROM Job j " +
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
    Page<Job> getAllJobMemberShipWithPaymentType(String area,String keyword,int paymentType,Pageable pageable);


    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"+
            " and (sub.id=:subCareerId)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+

            " and (j.paymentType=:paymentType)"+
            " and (sub.id=:subCareerId)"+
            " and ((j.budget <400000.0 and j.paymentType=1) or (j.budget <20000000.0 and j.paymentType=2))"+
             "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobNormalSearchAll(String area,String keyword,int paymentType,int subCareerId,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"+
            " and (sub.id=:subCareerId)"
            + "Order by j.topTime desc,j.budget desc"

            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.skills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.isActive=true and j.isApproved=1 and j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%'))"+
            " and (j.paymentType=:paymentType)"+
            " and (sub.id=:subCareerId)"+
            "Order by j.topTime desc,j.budget desc"
    )
    Page<Job> getAllJobMemberShipSearchAll(String area,String keyword,int paymentType,int subCareerId,Pageable pageable);







}
