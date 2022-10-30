package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class RecruiterDto {
    String id;
    String companyName;
    String website;
    double star;
    Integer numberOfFeedback;
    Integer totalPosted;
}
