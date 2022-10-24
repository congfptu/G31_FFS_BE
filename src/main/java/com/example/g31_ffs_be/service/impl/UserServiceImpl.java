package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addUser(User u) {
        try {
            userRepository.save(u);
        }
        catch (Exception e){

        }
    }

    @Override
    public void banUser(String id) {
        User u= userRepository.findById(id).get();
        u.setIsBanned(true);
        userRepository.save(u);
    }
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user==null||!user.getIsBanned()) {
            return false;
        } else {
            user.setVerificationCode("");
            user.setIsBanned(false);
            userRepository.save(user);
            return true;
        }

    }
}
