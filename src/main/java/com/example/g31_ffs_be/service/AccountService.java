package com.example.g31_ffs_fe.service;

import com.example.g31_ffs_fe.dto.AccountDto;
import com.example.g31_ffs_fe.model.Account;
import com.example.g31_ffs_fe.model.Freelancer;

import java.util.List;

public interface AccountService {
    List<AccountDto> getAllAccounts();
    void addAccount(Account f);
    public Boolean checkIdExist(String id);
    public void addAccountRole(String account_id,int role_id);
}
