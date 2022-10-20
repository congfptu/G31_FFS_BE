package com.example.g31_ffs_be.dto;

import lombok.Data;

@Data
public class RecruiterAdminDto {
    String id;
    String email;
    String fullName;
    String accountBalance;
    Boolean isBanned;
}
