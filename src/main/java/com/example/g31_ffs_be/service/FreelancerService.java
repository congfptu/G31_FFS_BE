package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface FreelancerService {
    void addFreelancer(Freelancer f);
    APIResponse<FreelancerAdminDto> getFreelancerByName(String name,int pageNo,int pageSize);
    FreelancerDetailDto getDetailFreelancer(String id);
    List<FreelancerAdminDto>getTop5ByName(String name);

    FreelancerProfileDTO getFreelancerProfile(String id);

    void addEducation(Education education, String freelancerId);
    void deleteEducation(int id);
    void updateEducation(Education education);
    void addWorkExperience(WorkExperience workExperience, String freelancerId);
    void deleteWorkExperience(int id);
    void updateWorkExperience(WorkExperience workExperience);
    void addSkill(List<Skill> skill,String freelancerId);
    void deleteSkill(Skill skill,String freelancerId);
    void updateProfile(RegisterDto registerDto);
    APIResponse<PostFindingDTO> getJobSaved(String freelancerId,int pageNo,int pageSize);
    APIResponse<PostFindingDTO> getJobRequest(String freelancerId,int status,int pageNo,int pageSize);
    APIResponse<FreelancerFilterDto> getAllFreelancerByFilter(String address,int costOption,String subCareer,String skill,int pageNo,int pageSize);

}
