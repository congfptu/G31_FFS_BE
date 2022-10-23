package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.AccountDto;
import com.example.g31_ffs_be.model.Account;

import java.util.List;

public interface AccountService {
    List<AccountDto> getAllAccounts();
    void addAccount(Account f);
    public Boolean checkIdExist(String id);
    public void addAccountRole(String account_id,int role_id);
    public Boolean checkEmailExist(String email);
    void sendVerificationEmail(Account account,String siteURL);
}
