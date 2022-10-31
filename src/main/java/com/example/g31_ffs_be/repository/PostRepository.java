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
@Query(value = " SELECT DISTINCT j FROM Job j " +
        " LEFT JOIN FETCH j.listSkills s "+
        " LEFT JOIN FETCH j.subCareer sub "+
        " where j.area  LIKE CONCAT('%',:area,'%') "+
        " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or j.description LIKE CONCAT('%',:keyword,'%'))"+
        " and j.isTop=:is_top"+
        " and j.budget<=:budget"
        , countQuery = " SELECT count( distinct j.id) FROM Job j " +
        " LEFT JOIN  j.listSkills s "+
        " LEFT JOIN  j.subCareer sub "+
        " where j.area  LIKE CONCAT('%',:area,'%') "+
        " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or j.description LIKE CONCAT('%',:keyword,'%'))"+
        " and j.isTop=:is_top"+
        " and j.budget<=:budget"

   )

    Page<Job> getJobSearch(String area,Double budget,String keyword,Boolean is_top,Pageable pageable);
    @Query(value = " SELECT DISTINCT j FROM Job j " +
            " LEFT JOIN FETCH j.listSkills s "+
            " LEFT JOIN FETCH j.subCareer sub "+
            " where j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or j.description LIKE CONCAT('%',:keyword,'%'))"+
            " and j.isTop=:is_top"+
            " and j.budget<=:budget"+
            " and j.subCareer.id=:sub_career_id"
            , countQuery = " SELECT count( distinct j.id) FROM Job j " +
            " LEFT JOIN  j.listSkills s "+
            " LEFT JOIN  j.subCareer sub "+
            " where j.area  LIKE CONCAT('%',:area,'%') "+
            " and (j.jobTitle LIKE CONCAT('%',:keyword,'%') or j.description LIKE CONCAT('%',:keyword,'%'))"+
            " and j.isTop=:is_top"+
            " and j.budget<=:budget"+
            " and j.subCareer.id=:sub_career_id"

    )
    Page<Job> getJobSearch(String area,Double budget,String keyword,Boolean is_top,Integer sub_career_id,Pageable pageable);
}
