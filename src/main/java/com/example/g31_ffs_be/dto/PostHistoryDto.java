package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class PostHistoryDto {
    int jobId;
    String jobTitle;
    String description;
    String timeCount;
    String paymentType;
    int status;
    String subCareer;
    int totalApplied;
}
