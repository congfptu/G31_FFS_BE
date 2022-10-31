package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Education;
import com.example.g31_ffs_be.model.Feedback;
import com.example.g31_ffs_be.model.Skill;
import com.example.g31_ffs_be.model.WorkExperience;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class FreelancerDetailDto {
    String id;
    Boolean gender;
    String phone;
    String fullName;
    String address;
    Set<Skill> skills;
    double costPerHour;
    String description;
    Set<Education> educations;
    Set<WorkExperience> workExperiences;
    Set<Feedback> feedbackTos;
    double star;
    LocalDate birthdate;
    String subCareer;
    Boolean isBanned;

}
