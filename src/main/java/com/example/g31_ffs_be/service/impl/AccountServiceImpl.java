package com.example.g31_ffs_fe.service.impl;

import com.example.g31_ffs_fe.dto.AccountDto;
import com.example.g31_ffs_fe.model.Account;
import com.example.g31_ffs_fe.model.AccountRole;
import com.example.g31_ffs_fe.model.AccountRoleId;
import com.example.g31_ffs_fe.model.Role;
import com.example.g31_ffs_fe.repository.AccountRepository;
import com.example.g31_ffs_fe.repository.AccountRoleRepository;
import com.example.g31_ffs_fe.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;
@Service
@Component
public class AccountServiceImpl implements AccountService {
    @Autowired AccountRepository repo;
    @Autowired AccountRoleRepository acc_role_repo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    @Bean
    @ElementCollection(fetch = FetchType.LAZY)
    public List<AccountDto> getAllAccounts() {
      /*  List<AccountDto> lists=new ArrayList<>();
        for (Account a:repo.findAll()) {
            AccountDto acc = modelMapper.map(a, AccountDto.class);

            lists.add(acc);

        }*/
      return null;
    }
    public List<Account> getAllAccount(){
        return (List<Account>) repo.findAll();
    }

    @Override
    public void addAccount(Account f) {
        repo.save(f);

    }

    @Override
    public Boolean checkIdExist(String id) {
        if (repo.existsById(id)) return true;

        return false;
    }

    @Override
    public void addAccountRole(String account_id, int role_id) {
        AccountRole acc=new AccountRole();
        Account a=new Account();
        a.setId(account_id);
        Role b=new Role();
        b.setId(role_id);

        acc_role_repo.save(acc);
    }



}
