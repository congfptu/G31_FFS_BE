package com.example.g31_ffs_be.service.impl;
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
    private RecruiterAdminDto mapToFreeDTO(Recruiter recruiter){
        RecruiterAdminDto recruiterAdminDto=mapper.map(recruiter,RecruiterAdminDto.class);
        return recruiterAdminDto;
    }
    private RecruiterDetailDTO mapToRecruiterDetailDto(Recruiter recruiter){
        RecruiterDetailDTO recruiterDetailDTO=mapper.map(recruiter,RecruiterDetailDTO.class);
        return recruiterDetailDTO;
    }
    @Override
    public List<RecruiterAdminDto> getRecruiterByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Recruiter> page = recruiterRepository.getRecruiterByName(name,pageable);
        List<Recruiter> freelancers=page.getContent();
        List<RecruiterAdminDto> ras=new ArrayList<>();
        for (Recruiter f: freelancers){
            RecruiterAdminDto ra=new RecruiterAdminDto();
            ra=mapToFreeDTO(f);
            ra.setFullName(f.getUser().getFullname());
            ra.setEmail(f.getUser().getAccount().getEmail());
            ra.setIsBanned(f.getUser().getIsBanned());
            ras.add(ra);
        }
        return ras;
    }

    @Override
    public RecruiterDetailDTO getDetailRecruiter(String id) {
        Optional<Recruiter> recruiter=recruiterRepository.findById(id);
        Recruiter r=recruiter.get();
        RecruiterDetailDTO rd=mapToRecruiterDetailDto(r);
        double star=0;
        User u=r.getUser();
        for (Feedback feedback:u.getFeedbackTos())
            star+=feedback.getStar();
        star=star/u.getFeedbackTos().size();
        rd.setStar(star);
        rd.setAddress(u.getAddress());
        rd.setPhone(u.getPhone());
        rd.setEmail(u.getAccount().getEmail());
        return rd;
    }
}
