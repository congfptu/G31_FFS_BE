package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.RoleRepository;
import com.example.g31_ffs_be.service.FreelancerService;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import com.example.g31_ffs_be.service.impl.StaffServiceImpl;
import com.example.g31_ffs_be.service.impl.UserServiceImpl;

import net.bytebuddy.utility.RandomString;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.time.Instant;
import java.util.List;
@RestController
@RequestMapping("")
@CrossOrigin("*")
public class AccountController {
    @Autowired private AccountServiceImpl service;

    @Autowired
    RoleRepository roleRepository;




}
