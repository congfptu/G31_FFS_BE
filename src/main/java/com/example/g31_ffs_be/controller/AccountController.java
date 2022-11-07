package com.example.g31_ffs_be.controller;
import com.example.g31_ffs_be.repository.RoleRepository;
import com.example.g31_ffs_be.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("")
@CrossOrigin("*")
public class AccountController {
    @Autowired private AccountService accountService;

    @Autowired
    RoleRepository roleRepository;
}
