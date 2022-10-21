package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.FreelancerDetailDto;
import com.example.g31_ffs_be.model.Freelancer;

import java.util.List;

public interface FreelancerService {
    void addFreelancer(Freelancer f);
    List<FreelancerAdminDto> getFreelancerByName(String name,int pageNo,int pageSize);
    FreelancerDetailDto getDetailFreelancer(String id);
    List<FreelancerAdminDto>getTop5ByName(String name);
}
