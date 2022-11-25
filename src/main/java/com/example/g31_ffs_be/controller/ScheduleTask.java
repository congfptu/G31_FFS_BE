package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleTask {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredMemberShip() {
        userRepository.updateNotSafe();
        userRepository.updateMemberShipExpired();
        userRepository.updateSafe();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredBan() {
        userRepository.updateNotSafe();
        userRepository.updateBanUserExpired();
        userRepository.updateSafe();
    }
    @Scheduled(cron = "0 0/5 * * * *")

    public void updateIsTopExpired() {
        userRepository.updateNotSafe();
        postRepository.updateIsTopExpired();
        userRepository.updateSafe();
    }

}

