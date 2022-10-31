package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class WorkExperienceDTO {
    String id;
    String companyName;
    String position;
    Integer monthFrom;
    Integer yearFrom;
    Integer monthTo;
    Integer yearTo;
    String description;
}
