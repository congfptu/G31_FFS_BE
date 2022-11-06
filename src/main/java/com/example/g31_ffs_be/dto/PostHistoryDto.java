package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class PostHistoryDto {
    String jobTitle;
    String description;
    String timeCount;
    String paymentType;
    String status;
    String subCareer;
    int totalApplied;
}
