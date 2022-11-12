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

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredMemberShip() {
        userRepository.updateMemberShipExpired();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredBan() {
        userRepository.updateBanUserExpired();
    }
}

