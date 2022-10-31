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
            " where a.freelancer_id like CONCAT('%',:name,'%') or" +
          "   b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') "+
          "Order by b.fullname asc,c.email asc ", nativeQuery = true)
  Page<Freelancer> getFreelancerByName(String name,Pageable pageable);
  @Query(value = "select * from `freelancer` a " +
          "inner join user b on b.user_id=a.freelancer_id " +
          "inner join account c on a.freelancer_id=c.id " +
          "where b.fullname like CONCAT('%',:name,'%') or c.email like CONCAT('%',:name,'%') " +
          "order by length(a.freelancer_id),a.freelancer_id asc LIMIT 5", nativeQuery = true)
  List<Freelancer> getTop5ByName(String name);
  @Query(value = "select a from Freelancer a"+
          " inner join fetch a.subCareer b" +
          " inner join fetch a.skills c" +
          " inner join fetch a.educations d"+
          " inner join fetch a.workExperiences e"+
          " inner join fetch a.user f"+
          " inner join fetch f.account g"+
          " where a.id= :id"
          , countQuery = "select count(a) from Freelancer a"+
          " inner join  a.subCareer b" +
          " inner join  a.skills c" +
          " inner join  a.educations d"+
          " inner join a.workExperiences e"+
          " inner join a.user f"+
          " inner join f.account g"+
          " where a.id= :id")
  Freelancer getProfileFreelancer(String id);

}

