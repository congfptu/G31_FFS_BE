package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class WorkExperienceDTO {
    String id;
    String companyName;
    String position;
    String from;
    String to;
    String description;
}
