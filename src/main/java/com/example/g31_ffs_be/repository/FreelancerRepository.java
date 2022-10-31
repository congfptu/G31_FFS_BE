package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Freelancer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer,String> {
  @Query(value = "SELECT f FROM Freelancer f "+
          "LEFT JOIN FETCH f.user b " +
          "LEFT JOIN FETCH b.account a " +
          "where b.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')",
         countQuery = "SELECT count(f.id) FROM Freelancer f "+
                 "LEFT JOIN  f.user b " +
                 "LEFT JOIN b.account a " +
                 "where b.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')")
  Page<Freelancer> getFreelancerByName(String name,Pageable pageable);
  @Query(value = "SELECT f FROM Freelancer f "+
          "LEFT JOIN FETCH f.user b " +
          "LEFT JOIN FETCH b.account a " +
          "where b.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')",
          countQuery = "SELECT count(f.id) FROM Freelancer f "+
                  "LEFT JOIN  f.user b " +
                  "LEFT JOIN  b.account a " +
                  "where b.fullName like CONCAT('%',:name,'%') or a.email like CONCAT('%',:name,'%')")
  Page<Freelancer> getTop5ByName(String name,Pageable pageable);

  @Query(value = "SELECT f FROM Freelancer f "+
          "LEFT JOIN FETCH f.educations e " +
          "LEFT JOIN FETCH f.workExperiences w " +
          "LEFT JOIN FETCH f.user b " +
          "LEFT JOIN FETCH b.account a " +
          "LEFT JOIN FETCH f.skills s " +
          "LEFT JOIN FETCH f.subCareer sub " +
          "LEFT JOIN FETCH a.role "+
          "LEFT JOIN FETCH b.feedbackTos fb "+
          "where f.id like :id ")
  Freelancer getDetailFreelancer(String id);

  @Query(value = "select a from Freelancer a"+
          " LEFT join fetch a.subCareer b" +
          " LEFT join fetch a.skills c" +
          " LEFT join fetch a.educations d"+
          " LEFT join fetch a.workExperiences e"+
          " LEFT join fetch a.user f"+
          " LEFT join fetch f.account g"+
          " where a.id= :id"
          , countQuery = "select count(a) from Freelancer a"+
          " LEFT join  a.subCareer b" +
          " LEFT join  a.skills c" +
          " LEFT join  a.educations d"+
          " LEFT join a.workExperiences e"+
          " LEFT join a.user f"+
          " LEFT join f.account g"+
          " where a.id= :id")
  Freelancer getProfileFreelancer(String id);

}

