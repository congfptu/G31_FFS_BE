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
    @Query(value = "select a.* from `recruiter` a" +
            " inner join `user` b on b.user_id=a.recruiter_id" +
            " inner join `account` c on a.recruiter_id=c.id" +
            " where b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') "+
            "Order by b.fullname asc,c.email asc ", nativeQuery = true)
    Page<Recruiter> getRecruiterByName(String name, Pageable pageable);
    @Query(value = "select * from `recruiter` a " +
            "inner join user b on b.user_id=a.recruiter_id " +
            "inner join account c on a.recruiter_id=c.id " +
            "where b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') " +
            "order by length(a.recruiter_id),a.recruiter_id asc LIMIT 5", nativeQuery = true)
    List<Recruiter> getTop5Recruiter(String name);


}
