package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Career;
import lombok.Data;

@Data
public class RecruiterDetailDTO {
    String id;
    String avatar;
    String email;
    Career career;
    String phone;
    String companyName;
    String address;
    String taxNumber;
    String website;
    String companyIntro;
    double star;
    Boolean isBanned;
}
