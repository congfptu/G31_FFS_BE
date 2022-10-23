package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class UserController {
    @Autowired
    AccountServiceImpl accountService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> singUpUser(@RequestHeader(name = "Authorization") String token){
        Account a=new Account();
        a.setEmail("congbvhe141326@fpt.edu.vn");
       accountService.sendVerificationEmail(a,"facebook.com");
        return new ResponseEntity<>("Chúng tôi đã gửi email xác thực tới email của bạn.Vui lòng xác thực tài khoản của bạn!", HttpStatus.OK);

    }
}
