package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class CareerTitleDTO {
    Integer id;
    String name;
    SubCareerTitleDTO subCareers;
}
