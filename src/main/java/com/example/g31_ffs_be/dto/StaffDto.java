package com.example.g31_ffs_be.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data

public class StaffDto {
    String id;
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    private String email;
    @NotEmpty
    @Size(min = 2, message = "fullname tối thiểu 2 chữ")
    private String fullName;
    private String password;
    private String phone;
    private String address;
    private int role;
    private String isActive;
    private String isActive1;


}
