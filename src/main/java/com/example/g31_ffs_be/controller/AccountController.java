package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.model.*;
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
    @Autowired private UserServiceImpl userService;
    @Autowired private FreelancerService freelancerService;

/*  @Autowired  PasswordEncoder passwordEncoder;*/


   /* @GetMapping("/account")
    public List<AccountDto> showUserList(){
        List<AccountDto> accounts=service.getAllAccounts();

        return accounts ;
    }*/
    @GetMapping("/accounts")
    public List<Account> showUserList(){
        List<Account> accounts=service.getAllAccount();

        return accounts ;
    }
    @PostMapping("/account/add")
    public void addAccount(@RequestBody  String strJson){
        String id=RandomString.make(8);
        JSONObject json=new JSONObject(strJson);
        if(json.getString("role_id").equals("2"))
            id="LF"+id;
        else id="LR"+id;
        if (!service.checkIdExist(id)){
            Account acc=new Account();
            User u=new User();
            Freelancer f=new Freelancer();
            acc.setId(id);
            acc.setEmail(json.getString("email"));
         /*   acc.setPassword(passwordEncoder.encode(json.getString("password")));*/
            acc.setCreatedDate(Instant.now());
            service.addAccount(acc);
            u.setId(id);
            f.setId(id);
            userService.addUser(u);
            freelancerService.addFreelancer(f);
            service.addAccountRole(id,json.getInt("role_id"));
        }
    }


}