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
    String fullName;
    String companyName;
    String city;
    String country;
    String address;
    String taxNumber;
    String website;
    String companyIntro;
    double star;
    Boolean isBanned;
}
