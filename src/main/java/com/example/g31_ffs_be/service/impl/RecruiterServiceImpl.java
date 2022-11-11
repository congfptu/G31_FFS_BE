package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.FreelancerRepository;
import com.example.g31_ffs_be.repository.RecruiterRepository;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.RecruiterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class RecruiterServiceImpl implements RecruiterService {
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    FreelancerRepository freelancerRepository;

    private RecruiterAdminDto mapToFreeDTO(Recruiter recruiter) {
        RecruiterAdminDto recruiterAdminDto = mapper.map(recruiter, RecruiterAdminDto.class);
        return recruiterAdminDto;
    }

    private RecruiterDetailDTO mapToRecruiterDetailDto(Recruiter recruiter) {
        RecruiterDetailDTO recruiterDetailDTO = mapper.map(recruiter, RecruiterDetailDTO.class);
        return recruiterDetailDTO;
    }

    private List<RecruiterAdminDto> convertListRecruiterAdminDto(List<Recruiter> recruiters) {
        List<RecruiterAdminDto> ras = new ArrayList<>();
        for (Recruiter r: recruiters) {
            RecruiterAdminDto ra;
            User u=r.getUser();
            ra = mapToFreeDTO(r);
            ra.setFullName(u.getFullName());
            ra.setEmail(u.getAccount().getEmail());
            ra.setIsBanned(u.getIsBanned());
            ra.setAccountBalance(u.getAccountBalance());
            ra.setPhone(u.getPhone());
            ras.add(ra);
        }
        return ras;
    }

   /* @Override
    public APIResponse<RecruiterAdminDto> getRecruiterActivated(String name,Boolean status,Boolean isBanned,int defaultBan, int pageNo, int pageSize) {
        APIResponse<RecruiterAdminDto> apiResponse=new APIResponse();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Recruiter> page = recruiterRepository.getRecruiterActive(name,status,pageable);
        apiResponse.setPageIndex(pageNo+1);
        apiResponse.setResults(convertListRecruiterAdminDto(page.getContent()));
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setTotalResults(page.getTotalElements());

        return apiResponse;
    }*/

    @Override
    public APIResponse<RecruiterAdminDto> getRecruiterActivated(String keyword, Boolean status,int defaultStatus, int pageNo, int pageSize) {
        APIResponse<RecruiterAdminDto> apiResponse=new APIResponse();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Recruiter> page = recruiterRepository.getRecruiterActive(keyword,status,defaultStatus,pageable);
        apiResponse.setPageIndex(pageNo+1);
        apiResponse.setResults(convertListRecruiterAdminDto(page.getContent()));
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setTotalResults(page.getTotalElements());
        return apiResponse;
    }

    @Override
    public APIResponse<RecruiterAdminDto> getRecruiterNotActivated(String keyword, int pageNo, int pageSize) {
        APIResponse<RecruiterAdminDto> apiResponse=new APIResponse();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Recruiter> page = recruiterRepository.getRecruiterIsNotActive(keyword,pageable);
        apiResponse.setPageIndex(pageNo+1);
        apiResponse.setResults(convertListRecruiterAdminDto(page.getContent()));
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setTotalResults(page.getTotalElements());
        return apiResponse;
    }

    @Override
    public RecruiterDetailDTO getDetailRecruiter(String id) {
        try {
            Recruiter r  = recruiterRepository.getDetailRecruiter(id);
            RecruiterDetailDTO rd = mapToRecruiterDetailDto(r);
            double star = 0;
            User u = r.getUser();
            rd.setAvatar(u.getAvatar());
            rd.setStar(u.getStar());
            rd.setAddress(u.getAddress());
            rd.setPhone(u.getPhone());
            rd.setFullName(u.getFullName());
            rd.setEmail(u.getAccount().getEmail());
            rd.setIsBanned(u.getIsBanned());
            rd.setCity(u.getCity());
            rd.setCountry(u.getCountry());
            return rd;
        }
        catch (Exception e){
            System.out.println(e);
           return null;
        }
    }

    @Override
    public List<RecruiterAdminDto> getTop5RecruiterByName(String name,Boolean status) {
        List<Recruiter> recruiters = recruiterRepository.getTop5Recruiter(name,status,PageRequest.of(0,5)).getContent();
        return convertListRecruiterAdminDto(recruiters);
    }

    @Override
    public Boolean updateProfile(RegisterDto registerDto) {
        try {
            String id = registerDto.getId();
            User user = userRepository.getReferenceById(id);
            user.setFullName(registerDto.getFullName());
            user.setAddress(registerDto.getAddress());
            user.setCity(registerDto.getCity());
            user.setCountry(registerDto.getCountry());
            user.setPhone(registerDto.getPhone());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public Boolean updateProfileRecruiter(RegisterDto registerDto) {
        try{
        Recruiter recruiter =recruiterRepository.getReferenceById(registerDto.getId());
        recruiter.setTaxNumber(registerDto.getTaxNumber());
        recruiter.setWebsite(registerDto.getWebsite());
        Career career=new Career();
        career.setId(registerDto.getCareer());
        recruiter.setCareer(career);
        recruiter.setCompanyIntro(registerDto.getDescription());
        recruiter.setCompanyName(registerDto.getCompanyName());
        recruiterRepository.save(recruiter);}
        catch(Exception e) {
            System.out.println(e);
            return true;
        }
        return false;
    }

    @Override
    public RecruiterDetailDTO getProfileRecruiterByFreelancer(String id, String freelancerId) {
        try {
            Freelancer freelancer=freelancerRepository.getReferenceById(freelancerId);
            Recruiter r  = recruiterRepository.getDetailRecruiterByFreelancer(id);
            RecruiterDetailDTO rd = mapToRecruiterDetailDto(r);
            double star = 0;
            User u = r.getUser();
            rd.setAvatar(u.getAvatar());
            rd.setStar(u.getStar());
            rd.setAddress(u.getAddress());
            rd.setPhone(u.getPhone());
            rd.setFullName(u.getFullName());
            rd.setEmail(u.getAccount().getEmail());
            rd.setIsBanned(u.getIsBanned());
            rd.setCity(u.getCity());
            rd.setCountry(u.getCountry());
            Boolean isApplied=false;
            for(JobRequest jobRequest:freelancer.getJobRequests()){
                if(jobRequest.getStatus()==1 && jobRequest.getJob().getCreateBy().getId().equals(id)) {
                    isApplied = true;
                    break;
                }
            }
            rd.setIsApplied(isApplied);
            return rd;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

}
