package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill,Integer> {
    @Query(value ="select a.* from `skill` a " +
                    "where a.id  not in " +
                    "(select skill_id from `freelancer_skill` where freelancer_id like :freelancerId)", nativeQuery = true)
    List<Skill> getAllRemainSkills(String freelancerId);
}
