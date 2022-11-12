package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostFindingDTO {
    int postID;
    String jobTitle;
    String subCareer;
    String description;
    String attach;
    String paymentType;
    String budget;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    LocalDateTime createdDate;
    String timeCount;
    String area;
    Boolean isActive;
    Boolean isTop;
    Set<Skill> listSkills;
    int isApproved;
}
