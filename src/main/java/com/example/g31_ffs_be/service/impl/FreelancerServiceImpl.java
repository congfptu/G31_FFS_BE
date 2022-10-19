package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.FreelancerDetailDTO;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.FreelancerRepository;
import com.example.g31_ffs_be.service.FreelancerService;
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
public class FreelancerServiceImpl implements FreelancerService {
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public void addFreelancer(Freelancer f) {

    }
    private FreelancerAdminDto mapToFreeDTO(Freelancer freelancer){
      FreelancerAdminDto freelancerAdminDto=mapper.map(freelancer,FreelancerAdminDto.class);
      return freelancerAdminDto;
    }
    private FreelancerDetailDTO mapToFreelancerDetailDTO(Freelancer freelancer){
        FreelancerDetailDTO freelancerDetailDTO=mapper.map(freelancer,FreelancerDetailDTO.class);
        return freelancerDetailDTO;
    }
    @Override
    public List<FreelancerAdminDto> getFreelancerByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Freelancer> page = freelancerRepository.getFreelancerByName(name,pageable);
        List<Freelancer> freelancers=page.getContent();
        List<FreelancerAdminDto> fas=new ArrayList<>();
        for (Freelancer f: freelancers){
            FreelancerAdminDto fa=new FreelancerAdminDto();
            fa=mapToFreeDTO(f);
            fa.setFullName(f.getUser().getFullname());
            fa.setEmail(f.getUser().getAccount().getEmail());
            fa.setIsBanned(f.getUser().getIsBanned());
            fas.add(fa);
        }
        return fas;


    }

    @Override
    public FreelancerDetailDTO getDetailFreelancer(String id) {
        Optional<Freelancer> freelancer=freelancerRepository.findById(id);
       Freelancer f=freelancer.get();
        FreelancerDetailDTO fd=mapToFreelancerDetailDTO(f);
        double star=0;
        User u=f.getUser();
        for (Feedback feedback:u.getFeedbackTos())
            star+=feedback.getStar();
        star=star/u.getFeedbackTos().size();
        fd.setStar(star);
        fd.setSubCareer(f.getSubCareer().getName());
        fd.setFullName(u.getFullname());
        fd.setAddress(u.getAddress());
        fd.setPhone(u.getPhone());
        return fd;
    }
}
