package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.AccountDto;
import com.example.g31_ffs_be.dto.RegisterDto;
import com.example.g31_ffs_be.model.Account;

import java.util.List;

public interface AccountService {
    List<AccountDto> getAllAccounts();
    public Boolean checkIdExist(String id);
    public Boolean checkEmailExist(String email);
    void sendVerificationEmail(Account account,String siteURL);
    void createAccount(RegisterDto registerDto);
    public void sendResetPasswordEmail(Account account,String siteURL);
    public Boolean changePassword(AccountDto account);
    public Boolean forgotPassword(Account account);
     Account getAccountFromToken(String token);
    public Boolean changePasswordUser(AccountDto account);

}
