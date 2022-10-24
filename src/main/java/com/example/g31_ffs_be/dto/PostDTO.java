package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class PostDTO {
    String id;
    Instant time;
    String createdBy;
    String jobTitle;
    Integer isApproved;
    String approvedBy;
}
