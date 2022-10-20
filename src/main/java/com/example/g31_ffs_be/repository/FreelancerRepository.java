package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Freelancer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer,String> {
  @Query(value = "select a.* from `freelancer` a" +
            " inner join `user` b on b.user_id=a.freelancer_id" +
            " inner join `account` c on a.freelancer_id=c.id" +
            " where b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') "+
          "Order by b.fullname asc,c.email asc ", nativeQuery = true)
  Page<Freelancer> getFreelancerByName(String name,Pageable pageable);

}

