package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.repository.FreelancerRepository;
import com.example.g31_ffs_be.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FreelancerServiceImpl implements FreelancerService {
    @Autowired
    FreelancerRepository repo;
    @Override
    public void addFreelancer(Freelancer f) {
        try {
            repo.save(f);
        }
        catch(Exception e)
        {

        }

    }
}
