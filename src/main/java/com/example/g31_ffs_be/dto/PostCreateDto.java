package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Skill;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostCreateDto {
    private int subCareerId;
    private String jobTitle;
    private String recruiterId;
    private String description;
    private String attach;
    private List<Integer> skillIds;
    private String area;
    private int paymentType;
    private double budget;
}
