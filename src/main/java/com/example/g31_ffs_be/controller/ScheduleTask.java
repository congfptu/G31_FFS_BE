package com.example.g31_ffs_be.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {
    @Scheduled(fixedRate = 1000)
    public void updateExpiredMemberShip() {

        }
    }

