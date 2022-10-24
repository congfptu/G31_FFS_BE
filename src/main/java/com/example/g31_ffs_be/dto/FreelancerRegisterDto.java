package com.example.g31_ffs_be.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class FreelancerRegisterDto {
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    String email;
    String password;
    String fullName;
    String address;
    String city;
    String country;
    String phone;
    int roleId;
}
