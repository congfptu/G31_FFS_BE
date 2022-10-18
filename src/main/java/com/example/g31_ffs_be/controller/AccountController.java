package com.example.g31_ffs_fe.controller;

import com.example.g31_ffs_fe.dto.AccountDto;
import com.example.g31_ffs_fe.model.Account;
import com.example.g31_ffs_fe.service.FreelancerService;
import com.example.g31_ffs_fe.service.impl.AccountServiceImpl;
import com.example.g31_ffs_fe.service.impl.UserServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("")
@CrossOrigin("*")
public class AccountController {
    @Autowired private AccountServiceImpl service;
    @Autowired private UserServiceImpl service1;
    @Autowired private FreelancerService service2;

   /* @GetMapping("/account")
    public List<AccountDto> showUserList(){
        List<AccountDto> accounts=service.getAllAccounts();

        return accounts ;
    }*/
    @GetMapping("/accounts")
    public List<Account> showUserList1(){
        List<Account> accounts=service.getAllAccount();

        return accounts ;
    }
    @RequestMapping("/account/add/freelancer")
    public void addAccountFreelancer(){

        String id="LF"+RandomString.make(8);
        if (!service.checkIdExist(id)){
            Account acc=new Account();
            acc.setId(id);
            acc.setEmail("fdsdfs");
            service.addAccount(acc);
            service.addAccountRole(id,2);
        }
    }
    @RequestMapping("/account/add/recruiter")
    public void addAccountRecruiter(){
        String id="LR"+RandomString.make(8);
        if (!service.checkIdExist(id)){
            Account acc=new Account();
            acc.setId(id);
            acc.setEmail("fdsdfs");
            service.addAccount(acc);

        }
    }
    @RequestMapping("/account/add/staff")
    public void addAccountStaff(){
        String id="LS"+RandomString.make(8);
        if (!service.checkIdExist(id)){
            Account acc=new Account();
            acc.setId(id);
            acc.setEmail("fdsdfs");
            service.addAccount(acc);
        }
    }

}
