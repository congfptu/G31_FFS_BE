package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.model.User;

public interface UserService {
   
    void addUser(User u);
    void banUser(String id);

}
