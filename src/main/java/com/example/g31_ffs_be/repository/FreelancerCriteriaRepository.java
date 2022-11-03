//package com.example.g31_ffs_be.repository;
//
//import com.example.g31_ffs_be.model.Freelancer;
//import org.springframework.data.domain.Page;
//
//import javax.persistence.EntityManager;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//
//public class FreelancerCriteriaRepository {
//    private final EntityManager entityManager;
//    private final CriteriaBuilder criteriaBuilder;
//
//    public FreelancerCriteriaRepository(EntityManager entityManager, CriteriaBuilder criteriaBuilder) {
//        this.entityManager = entityManager;
//        this.criteriaBuilder = criteriaBuilder;
//    }
//    public Page<Freelancer> findAllFreelancerFilters(String address, int costOption, int subCareer, int skill, int pageNo, int pageSize){
//        CriteriaQuery<Freelancer> criteriaQuery=criteriaBuilder.createQuery(Freelancer.class);
//        return
//    }
//}
//
