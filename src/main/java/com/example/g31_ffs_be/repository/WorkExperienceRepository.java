package com.example.g31_ffs_be.repository;


import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.WorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkExperienceRepository extends JpaRepository<WorkExperience,Integer> {
    void deleteByFreelancer(Freelancer f);

}
