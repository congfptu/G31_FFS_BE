package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;
    @Override
    public void addUser(User u) {
        try {
            repo.save(u);
        }
        catch (Exception e){

        }
    }
}
