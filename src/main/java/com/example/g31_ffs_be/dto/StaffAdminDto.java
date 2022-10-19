package com.example.g31_ffs_be.dto;

import lombok.*;

@Data

public class StaffAdminDto {
    private String id;
    private String email;
    private String fullname;
    private String phone;
    private String address;
    private Boolean isActive;
}
