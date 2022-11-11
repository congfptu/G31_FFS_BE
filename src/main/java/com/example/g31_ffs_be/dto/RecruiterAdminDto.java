package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class RecruiterAdminDto {
    String id;
    String email;
    String fullName;
    double accountBalance;
    Boolean isBanned;
    String phone;
}
