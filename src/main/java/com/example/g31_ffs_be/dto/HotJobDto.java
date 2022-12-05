package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class HotJobDto {

    Integer id;
    RecruiterDto recruiter;
    String title;
    String subcareer;
    String area;
    String typebudget;
    double bugget;


}
