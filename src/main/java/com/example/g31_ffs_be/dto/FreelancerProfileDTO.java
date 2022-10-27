package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Education;
import com.example.g31_ffs_be.model.Skill;
import com.example.g31_ffs_be.model.WorkExperience;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class FreelancerProfileDTO {
    String id;
    Boolean gender;
    String phone;
    String fullName;
    String address;
    String city;
    String country;
    List<SkillDTO> skills;
    Double costPerHour;
    String email;
    String description;
    String cv;
    List<EducationDTO> educations;
    List<WorkExperienceDTO> workExps;
    Double star;
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;
    String subCareer;
}
