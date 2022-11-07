package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;

import java.util.List;

public interface FreelancerService {
    void addFreelancer(Freelancer f);
    APIResponse<FreelancerAdminDto> getFreelancerByName(String name,int pageNo,int pageSize);
    FreelancerDetailDto getFreelancerInfo(String id);
    List<FreelancerAdminDto>getTop5ByName(String name);

    FreelancerProfileDTO getFreelancerProfile(String id);

    void addEducation(Education education, String freelancerId);
    void deleteEducation(int id);
    void updateEducation(Education education);
    void addWorkExperience(WorkExperience workExperience, String freelancerId);
    void deleteWorkExperience(int id);
    void updateWorkExperience(WorkExperience workExperience);
    void addSkill(List<Skill> skill,String freelancerId);
    int deleteSkill(String freelancerId,int skillId);
    void updateProfile(RegisterDto registerDto);
    APIResponse<PostFindingDTO> getJobSaved(String freelancerId,int pageNo,int pageSize);
    APIResponse<PostFindingDTO> getJobRequest(String freelancerId,int status,int pageNo,int pageSize);
    APIResponse<FreelancerFilterDto> getAllFreelancerByFilter(String address,int costOption,int subCareer,List<Integer> skill,String keyword,int pageNo,int pageSize);
    APIResponse<FreelancerFilterDto> getFreelancerApplied(int jobId,String recruiterId,String address,List<Integer> skill,int subCareer,String keyword,int pageNo,int pageSize);

}
