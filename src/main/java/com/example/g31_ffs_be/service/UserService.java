package com.example.g31_ffs_fe.service;

import com.example.g31_ffs_fe.model.User;
import com.example.g31_ffs_fe.repository.FreelancerRepository;
import com.example.g31_ffs_fe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {
   
    void addUser(User u);
}
