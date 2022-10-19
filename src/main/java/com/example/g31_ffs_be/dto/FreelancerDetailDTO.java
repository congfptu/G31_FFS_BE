package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Education;
import com.example.g31_ffs_be.model.Skill;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.model.Workexperience;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class FreelancerDetailDTO {
    String id;
    Boolean gender;
    String phone;
    String fullName;
    String address;
    List<Skill> skills;
    double costPerHour;
    String description;
    List<Education> educations;
    List<Workexperience> workexperiences;
    double star;
    LocalDate birthdate;
    String subCareer;

}
