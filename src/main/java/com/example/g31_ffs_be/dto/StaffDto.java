package com.example.g31_ffs_be.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffDto {
    @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    private String email;
    @NotEmpty
    @Size(min = 2, message = "fullname tối thiểu 2 chữ")
    private String fullname;
    private String password;
    private String phone;
    private String address;
    private int role;
    private String isActive;
    private String isActive1;
}
