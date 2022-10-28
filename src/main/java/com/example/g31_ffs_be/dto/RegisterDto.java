package com.example.g31_ffs_be.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class RegisterDto {
    String id;
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    String email;
    String password;
    String fullName;
    String address;
    String city;
    String country;
    String phone;
    String role;
    int subCareer;
    String taxNumber;
    String website;
    Boolean gender;
    LocalDate birthdate;
    double costPerHour;
    int career;
    String companyName;


}
