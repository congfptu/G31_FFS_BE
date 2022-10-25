package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDTO {
    String id;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    LocalDateTime createdDate;
    String createdBy;
    String jobTitle;
    Integer isApproved;
    String approvedBy;

}
