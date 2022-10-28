package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostFindingDTO {
    String postID;
    String jobTitle;
    String subCareer;
    String description;
    String attach;
    String paymentType;
    double budget;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    LocalDateTime createdDate;
    String area;
    Boolean isActive;
    Set<Skill> listSkills;
}
