package com.example.g31_ffs_fe.service.impl;

import com.example.g31_ffs_fe.model.User;
import com.example.g31_ffs_fe.repository.UserRepository;
import com.example.g31_ffs_fe.service.AccountService;
import com.example.g31_ffs_fe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;
    @Override
    public void addUser(User u) {
     repo.save(u);
    }
}
