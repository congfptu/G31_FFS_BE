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
    @Query(value = " SELECT DISTINCT(j.id),j.approved_by,create_by,job_title,sub_career_id,description,attach,payment_type,budget,time,area,is_approved,is_active,is_top,fee \n" +
            " FROM `jobs` j inner join job_skill js on j.id=js.job_id\n" +
            " inner join `subcareer` sc on j.sub_career_id=sc.id\n" +
            " inner join `skill` s on s.id=js.skill_id\n" +
            "            where area LIKE CONCAT('%',:area,'%') and budget LIKE CONCAT('%',:budget,'%') \n" +
            "            and (job_title LIKE CONCAT('%',:keyword,'%') or description LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%')) " +
            "and is_top=:is_top"
            , nativeQuery = true)
    Page<Job> getJobSearch(String area,String budget,String keyword,String is_top,Pageable pageable);
    @Query(value = " SELECT DISTINCT(j.id),j.approved_by,create_by,job_title,sub_career_id,description,attach,payment_type,budget,time,area,is_approved,is_active,is_top,fee \n" +
            " FROM `jobs` j inner join job_skill js on j.id=js.job_id\n" +
            " inner join `subcareer` sc on j.sub_career_id=sc.id\n" +
            " inner join `skill` s on s.id=js.skill_id\n" +
            "            where area LIKE CONCAT('%',:area,'%') and budget LIKE CONCAT('%',:budget,'%') \n" +
            "            and (job_title LIKE CONCAT('%',:keyword,'%') or description LIKE CONCAT('%',:keyword,'%') or s.name LIKE CONCAT('%',:keyword,'%')) " +
            "and is_top=:is_top " +
            "and sc.id=:sub_career_id"
            , nativeQuery = true)
    Page<Job> getJobSearch(String area,String budget,String sub_career_id,String keyword,String is_top,Pageable pageable);
}
