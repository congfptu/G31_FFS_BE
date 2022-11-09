package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Freelancer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
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
          "LEFT JOIN FETCH b.feedbackTos fb "+
          "where f.id like :id ")
  Freelancer getDetailFreelancer(String id);
  @Query(value = "SELECT f FROM Freelancer f "+
          "LEFT JOIN FETCH f.educations e " +
          "LEFT JOIN FETCH f.workExperiences w " +
          "LEFT JOIN FETCH f.user b " +
          "LEFT JOIN FETCH b.account a " +
          "LEFT JOIN FETCH f.skills s " +
          "LEFT JOIN FETCH f.subCareer sub " +
          "LEFT JOIN FETCH b.feedbackTos fb "+
          "LEFT JOIN FETCH f.jobRequests rq "+
          "LEFT JOIN FETCH rq.job job "+
          "LEFT JOIN FETCH job.createBy cre "+
          "where f.id like :id")
  Freelancer getFreelancerDetailByRecruiter(String id);

/*
  @Query(value = "select distinct a from Freelancer a"+
          " LEFT join fetch a.subCareer b" +
          " LEFT join fetch a.skills c" +
          " LEFT join fetch a.user f"+
          " LEFT join fetch f.account g"+
          " where a.id= :id"
          , countQuery = "select count(distinct a.id) from Freelancer a"+
          " LEFT join  a.subCareer b" +
          " LEFT join  a.skills c" +
          " LEFT join a.user f"+
          " LEFT join f.account g"+
          " where a.id= :id")
  Freelancer getProfileFreelancer(String id);
*/


  @Query(value = "select  distinct a from Freelancer a"+
          " LEFT JOIN fetch a.subCareer b" +
          " LEFT JOIN  a.skills c" +
          " LEFT JOIN fetch a.user f"+
          " WHERE " +
          "(f.city like :city or :city='') " +
          "and (-1 in :skill or c.id in :skill) " +
          "and (b.id =:subCareer or :subCareer=-1)"+
          "and (a.costPerHour>=:costFrom and (a.costPerHour<=:costTo or :costTo=-1)) "+
          "and ( f.fullName like CONCAT('%',:keyword,'%') or c.name  like CONCAT('%',:keyword,'%') ) "
          ,
           countQuery = "select count(distinct a.id) from Freelancer a"+
                   " LEFT JOIN  a.subCareer b" +
                   " LEFT JOIN  a.skills c" +
                   " LEFT JOIN  a.user f"+
                   " WHERE (f.city like :city or :city='') " +
                   "and (-1 in :skill or c.id in :skill)" +
                   "and (b.id =:subCareer or :subCareer=-1)"+
                   " and (a.costPerHour>=:costFrom and (a.costPerHour<=:costTo or :costTo=-1)) "+
                   "and ( f.fullName like CONCAT('%',:keyword,'%') or c.name  like CONCAT('%',:keyword,'%') ) "
  )
  Page<Freelancer> getAllFreelancerWithCostPerHourBetween(String city,List<Integer> skill,double costFrom,double costTo,int subCareer,String keyword,Pageable pageable);

  @Query(value = "select distinct fre from Job j"+
          " LEFT JOIN   j.jobRequests rq" +
          " LEFT JOIN  rq.freelancer fre" +
          " LEFT JOIN fetch fre.subCareer sub "+
          " LEFT JOIN  fre.skills s "+
          " LEFT JOIN fetch fre.user u "+
          " WHERE j.id=:jobId "+
          " and rq.status=:status" +
          " and (u.city like :city or :city='')" +
          "and (-1 in :skill or s.id in :skill) " +
          "and (sub.id =:subCareer or :subCareer=-1)"+
          "and ( u.fullName like CONCAT('%',:keyword,'%') or s.name  like CONCAT('%',:keyword,'%') ) "+
          " order by rq.applyDate desc"

          ,
          countQuery = "select  count(distinct fre.id) from Job j"+
                  " LEFT JOIN  j.jobRequests rq" +
                  " LEFT JOIN  rq.freelancer fre" +
                  " LEFT JOIN  fre.subCareer sub"+
                  " LEFT JOIN  fre.skills s"+
                  " LEFT JOIN  fre.user u "+
                  " WHERE j.id=:jobId "+
                  " and rq.status=:status" +
                  " and (u.city like :city or :city='')" +
                  "and (-1 in :skill or s.id in :skill) " +
                  "and (sub.id =:subCareer or :subCareer=-1)"+
                  "and ( u.fullName like CONCAT('%',:keyword,'%') or s.name  like CONCAT('%',:keyword,'%') ) "+
                  " order by rq.applyDate desc"
  )
  Page<Freelancer> getFreelancerAppliedJob(int jobId,String city,List<Integer> skill,int subCareer,String keyword,int status, Pageable pageable);




  @Modifying
  @Transactional
  @Query(value = " delete from freelancer_skill  where freelancer_id=:freelancerId and skill_id=:skillId"
          , nativeQuery = true)
  Integer deleteFreelancerSkill(String freelancerId, int skillId);

  @Modifying
  @Transactional
  @Query(value = " insert into freelancer_skill (freelancer_id,skill_id) values (:freelancerId,:skillId)"
          , nativeQuery = true)
  Integer insertFreelancerSkill(String freelancerId, int skillId);


  @Query(value = " select freelancer_id from freelancer_skill where freelancer_id=:freelancerId and skill_id=:skillId"
          , nativeQuery = true)
  String checkSkillExist(String freelancerId, int skillId);




}

