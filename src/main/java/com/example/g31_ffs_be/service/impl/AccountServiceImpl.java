package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.AccountDto;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import java.util.List;

@Service
@Component
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository repo;

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

    public List<Account> getAllAccount() {
        return (List<Account>) repo.findAll();
    }

    @Override
    public void addAccount(Account f) {
        try {
            repo.save(f);
        } catch (Exception e) {

        }

    }

    @Override
    public Boolean checkIdExist(String id) {
        if (repo.existsById(id)) return true;

        return false;
    }

    @Override
    public Boolean checkEmailExist(String email) {

      return repo.findByEmail(email)!=null?true:false;
    }

    @Override
    public void addAccountRole(String account_id, int role_id) {

    }


}
