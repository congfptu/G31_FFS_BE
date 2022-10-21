package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class PostDTO {
    String id;
    Instant time;
    String createdBy;
    String jobTitle;
    Boolean isApproved;
}
