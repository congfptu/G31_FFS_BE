package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTask {
    @Autowired
    UserRepository userRepository;
    @Scheduled(fixedRate = 1055500)
    public void updateExpiredMemberShip() {
       /* List<User> users=userRepository.getAllUserExpiredMembership();
        for(User user:users) {
            System.out.println(user.getId());
            userRepository.save(user);
        }*/
        }
    }

