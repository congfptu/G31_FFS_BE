package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.Skill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostDetailDTO {
    int postID;
    RecruiterDto createBy;
    String jobTitle;
    String subCareer;
    String description;
    String attach;
    String paymentType;
    String budget;
    String timeCount;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    LocalDateTime time;
    String area;
    Boolean isActive;
    Integer isApproved;
    String approvedBy;
    Set<Skill> listSkills;
}
