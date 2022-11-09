package com.example.g31_ffs_be.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
@Data
public class LoginDTO {
    @NotEmpty(message = "Vui lòng điền email.")
  //  @Email(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Email không hợp lệ.")
    private String email;
    @NotEmpty(message = "Vui lòng điền mật khẩu.")
    private String password;
}
