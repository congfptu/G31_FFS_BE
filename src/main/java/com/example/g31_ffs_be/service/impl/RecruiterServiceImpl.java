package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.FreelancerDetailDto;
import com.example.g31_ffs_be.dto.RecruiterAdminDto;
import com.example.g31_ffs_be.dto.RecruiterDetailDTO;
import com.example.g31_ffs_be.model.Feedback;
import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.RecruiterRepository;
import com.example.g31_ffs_be.service.RecruiterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    private ModelMapper mapper;

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
            ra.setFullName(u.getFullname());
            ra.setEmail(u.getAccount().getEmail());
            ra.setIsBanned(u.getIsBanned());
            ra.setAccountBalance(u.getAccountBalance());
            ras.add(ra);
        }
        return ras;
    }

    @Override
    public List<RecruiterAdminDto> getRecruiterByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Recruiter> page = recruiterRepository.getRecruiterByName(name, pageable);
        List<Recruiter> recruiters = page.getContent();
        return convertListRecruiterAdminDto(recruiters);
    }

    @Override
    public RecruiterDetailDTO getDetailRecruiter(String id) {
        try {
            Optional<Recruiter> recruiter = recruiterRepository.findById(id);
            Recruiter r = recruiter.get();
            RecruiterDetailDTO rd = mapToRecruiterDetailDto(r);
            double star = 0;
            User u = r.getUser();
            for (Feedback feedback : u.getFeedbackTos())
                star += feedback.getStar();
            star = star / u.getFeedbackTos().size();
            rd.setStar(star);
            rd.setAddress(u.getAddress());
            rd.setPhone(u.getPhone());
            rd.setEmail(u.getAccount().getEmail());
            rd.setIsBanned(u.getIsBanned());
            return rd;
        }
        catch (Exception e){
           return null;
        }
    }

    @Override
    public List<RecruiterAdminDto> getTop5RecruiterByName(String name) {
        List<Recruiter> recruiters = recruiterRepository.getTop5Recruiter(name);
        return convertListRecruiterAdminDto(recruiters);
    }
}
