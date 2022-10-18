package com.example.g31_ffs_fe.repository;


import com.example.g31_ffs_fe.model.Education;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends CrudRepository<Education,Integer> {

}
