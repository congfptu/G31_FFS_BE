package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.FreelancerDetailDTO;
import com.example.g31_ffs_be.model.Freelancer;

import java.util.List;

public interface FreelancerService {
    void addFreelancer(Freelancer f);
    List<FreelancerAdminDto> getFreelancerByName(String name,int pageNo,int pageSize);
    FreelancerDetailDTO getDetailFreelancer(String id);
}
