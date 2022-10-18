package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Workexperience;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends CrudRepository<Workexperience,Integer> {

}
