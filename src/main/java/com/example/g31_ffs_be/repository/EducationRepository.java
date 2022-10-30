package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Education;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface EducationRepository extends CrudRepository<Education,Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into education (freelancer_id,university, level, from, to, description)" +
            " values (:freelancer_id,:university,:level,:from,:to,:description)"
            , nativeQuery = true)
    Integer insert(String freelancer_id, String university,String level, String from, String to, String description);
}
