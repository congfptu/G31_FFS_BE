package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.Skill;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
public class PostDetailDTO {
    String postID;
    RecruiterDto createBy;
    String job_title;
    String sub_career;
    String description;
    String attach;
    String payment_type;
    double budget;
    Instant time;
    String area;
    Boolean is_Active;
    Integer is_Approved;
    String approved_by;
    Set<Skill> listSkills;
}
