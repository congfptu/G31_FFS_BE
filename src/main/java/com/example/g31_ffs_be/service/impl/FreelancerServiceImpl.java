package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.FreelancerDetailDto;
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
    private FreelancerDetailDto mapToFreelancerDetailDTO(Freelancer freelancer){
        FreelancerDetailDto freelancerDetailDTO=mapper.map(freelancer, FreelancerDetailDto.class);
        return freelancerDetailDTO;
    }
    private List<FreelancerAdminDto> convertListFreelancerDto(List<Freelancer> freelancers) {
        List<FreelancerAdminDto> fas=new ArrayList<>();
        for (Freelancer f:freelancers){
            User u=f.getUser();
            FreelancerAdminDto fa=new FreelancerAdminDto();
            fa=mapToFreeDTO(f);
            fa.setFullName(u.getFullname());
            fa.setEmail(u.getAccount().getEmail());
            fa.setIsBanned(u.getIsBanned());
            fa.setAccountBalance(u.getAccountBalance()!=null?u.getAccountBalance():0);
            fas.add(fa);
        }
        return fas;
    }
    @Override
    public List<FreelancerAdminDto> getFreelancerByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Freelancer> page = freelancerRepository.getFreelancerByName(name,pageable);
        List<Freelancer> freelancers=page.getContent();
        return convertListFreelancerDto(freelancers);


    }

    @Override
    public FreelancerDetailDto getDetailFreelancer(String id) {
        Optional<Freelancer> freelancer=freelancerRepository.findById(id);
       Freelancer f=freelancer.get();
        FreelancerDetailDto fd=mapToFreelancerDetailDTO(f);
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

    @Override
    public List<FreelancerAdminDto> getTop5ByName(String name) {
        List<Freelancer> freelancers=freelancerRepository.getTop5ByName(name);
        return convertListFreelancerDto(freelancers);
    }


}
