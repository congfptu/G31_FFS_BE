package com.example.g31_ffs_fe.repository;


import com.example.g31_ffs_fe.model.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerRepository extends JpaRepository<Freelancer,String> {

}
