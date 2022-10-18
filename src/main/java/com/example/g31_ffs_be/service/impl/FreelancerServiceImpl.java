package com.example.g31_ffs_fe.service.impl;

import com.example.g31_ffs_fe.model.Freelancer;
import com.example.g31_ffs_fe.repository.FreelancerRepository;
import com.example.g31_ffs_fe.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreelancerServiceImpl implements FreelancerService {
    @Autowired
    FreelancerRepository repo;
    @Override
    public void addFreelancer(Freelancer f) {
     repo.save(f);
    }
}
