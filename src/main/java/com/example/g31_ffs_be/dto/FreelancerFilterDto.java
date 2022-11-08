package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import lombok.Data;

import java.util.Set;

@Data
public class FreelancerFilterDto {
    private  String avatar;
    private  String fullName;
    private  String subCareer;
    private  String city;
    private Set<Skill> skills;
    private  String star;
    private  int totalFeedbacks;
    private  String costPerHour;
    private  String description;



}
