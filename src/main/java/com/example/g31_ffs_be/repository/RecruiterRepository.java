package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,String> {
 /*   @Query(value = "select a.* from `recruiter` a" +
            " inner join `user` b on b.user_id=a.recruiter_id" +
            " inner join `account` c on a.recruiter_id=c.id" +
            " where a.recruiter_id like CONCAT('%',:name,'%') or" +
            " b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') "+
            "Order by b.fullname asc,c.email asc ", nativeQuery = true)*/
 @Query(value = "SELECT r FROM Recruiter r "+
         "LEFT JOIN FETCH r.user u " +
         "LEFT JOIN FETCH u.account a " +
         "LEFT JOIN FETCH a.role ro " +
         "where u.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')",
         countQuery = "SELECT count(r) FROM Recruiter r "+
                 "LEFT JOIN  r.user u " +
                 "LEFT JOIN u.account a " +
                 "LEFT JOIN a.role ro " +
                 "where u.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')")
    Page<Recruiter> getRecruiterByName(String name, Pageable pageable);

        @Query(value = "select r from Recruiter r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH u.account a " +
            "where u.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%') " +
            "order by length(r.id),r.id asc",
        countQuery ="select count(r) from Recruiter r " +
                "LEFT JOIN  r.user u " +
                "LEFT JOIN  u.account a " +
                "where u.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')")
    Page<Recruiter> getTop5Recruiter(String name,Pageable pageable);

    @Query(value = "select r from Recruiter r " +
            "LEFT JOIN FETCH r.user u " +
            "LEFT JOIN FETCH u.account a " +
            "LEFT JOIN FETCH u.feedbackTos " +
            "LEFT JOIN FETCH r.career " +
            "where r.id =:id " +
            "order by r.id asc")

    Recruiter getDetailRecruiter(String id);


}
