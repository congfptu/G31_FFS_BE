package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
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

    private FreelancerAdminDto mapToFreeDTO(Freelancer freelancer) {
        FreelancerAdminDto freelancerAdminDto = mapper.map(freelancer, FreelancerAdminDto.class);
        return freelancerAdminDto;
    }

    private FreelancerDetailDto mapToFreelancerDetailDTO(Freelancer freelancer) {
        FreelancerDetailDto freelancerDetailDTO = mapper.map(freelancer, FreelancerDetailDto.class);
        return freelancerDetailDTO;
    }

    private List<FreelancerAdminDto> convertListFreelancerDto(List<Freelancer> freelancers) {
        List<FreelancerAdminDto> fas = new ArrayList<>();
        for (Freelancer f : freelancers) {
            User u = f.getUser();
            FreelancerAdminDto fa = new FreelancerAdminDto();
            fa = mapToFreeDTO(f);
            fa.setFullName(u.getFullName());
            fa.setEmail(u.getAccount().getEmail());
            fa.setIsBanned(u.getIsBanned());
            fa.setAccountBalance(u.getAccountBalance() != null ? u.getAccountBalance() : 0);
            fas.add(fa);
        }
        return fas;
    }

    @Override
    public List<FreelancerAdminDto> getFreelancerByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Freelancer> page = freelancerRepository.getFreelancerByName(name, pageable);
        List<Freelancer> freelancers = page.getContent();
        return convertListFreelancerDto(freelancers);


    }

    @Override
    public FreelancerDetailDto getDetailFreelancer(String id) {
        try {
            Optional<Freelancer> freelancer = freelancerRepository.findById(id);
            Freelancer f = freelancer.get();
            FreelancerDetailDto fd = mapToFreelancerDetailDTO(f);
            double star = 0;
            User u = f.getUser();
            for (Feedback feedback : u.getFeedbackTos())
                star += feedback.getStar();
            star = star / u.getFeedbackTos().size();
            fd.setStar(star);
            fd.setSubCareer(f.getSubCareer().getName());
            fd.setFullName(u.getFullName());
            fd.setAddress(u.getAddress());
            fd.setPhone(u.getPhone());
            fd.setIsBanned(u.getIsBanned());
            return fd;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public List<FreelancerAdminDto> getTop5ByName(String name) {
        List<Freelancer> freelancers = freelancerRepository.getTop5ByName(name);
        return convertListFreelancerDto(freelancers);
    }

    @Override
    public FreelancerProfileDTO getFreelancerProfile(String id) {
       try {
           Optional<Freelancer> freelancer = freelancerRepository.findById(id);

           FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();
           freelancerProfileDTO.setId(id);
           freelancerProfileDTO.setGender(freelancer.get().getGender());
           freelancerProfileDTO.setPhone(freelancer.get().getUser().getPhone());
           freelancerProfileDTO.setFullName(freelancer.get().getUser().getFullName());
           freelancerProfileDTO.setAddress(freelancer.get().getUser().getAddress());
           freelancerProfileDTO.setCity(freelancer.get().getUser().getCity());
           freelancerProfileDTO.setCountry(freelancer.get().getUser().getCountry());
           List<SkillDTO> skillDTOS = new ArrayList<>();
           for (Skill s : freelancer.get().getSkills()) {
               SkillDTO skillDTO = new SkillDTO();
               skillDTO.setId(s.getId());
               skillDTO.setName(s.getName());
               skillDTOS.add(skillDTO);
           }
           freelancerProfileDTO.setSkills(skillDTOS);
           freelancerProfileDTO.setCostPerHour(freelancer.get().getCostPerHour());
           freelancerProfileDTO.setEmail(freelancer.get().getUser().getAccount().getEmail());
           freelancerProfileDTO.setDescription(freelancer.get().getDescription());
           freelancerProfileDTO.setCv(freelancer.get().getCv());
           List<EducationDTO> educationDTOS = new ArrayList<>();
           for (Education e : freelancer.get().getEducations()
           ) {
               EducationDTO educationDTO = new EducationDTO();
               educationDTO.setId(e.getId().toString());
               educationDTO.setUniversity(e.getUniversity());
               educationDTO.setMajor(e.getDescription());
               educationDTO.setTo(e.getTo().toString());
               educationDTO.setFrom(e.getFrom().toString());
               educationDTO.setLevel(e.getLevel());
               educationDTOS.add(educationDTO);
           }
           freelancerProfileDTO.setEducations(educationDTOS);
           List<WorkExperienceDTO> workExperienceDTOS = new ArrayList<>();
           for (WorkExperience w : freelancer.get().getWorkExperiences()
           ) {
               WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO();
               workExperienceDTO.setId(w.getId().toString());
               workExperienceDTO.setDescription(w.getDescription());
               workExperienceDTO.setTo(w.getMonthTo().toString()+"-"+w.getYearTo().toString());
               workExperienceDTO.setFrom(w.getMonthFrom().toString()+"-"+w.getYearFrom().toString());
               workExperienceDTO.setCompanyName(w.getCompanyName());
               workExperienceDTO.setPosition(w.getPosition());
               workExperienceDTOS.add(workExperienceDTO);
           }
           freelancerProfileDTO.setWorkExps(workExperienceDTOS);
           freelancerProfileDTO.setBirthDate(freelancer.get().getBirthdate());
           freelancerProfileDTO.setSubCareer(freelancer.get().getSubCareer().getName());
           double star = 0;
           User u = freelancer.get().getUser();
           for (Feedback feedback : u.getFeedbackTos())
               star += feedback.getStar();
           star = star / u.getFeedbackTos().size();
           freelancerProfileDTO.setStar(star);
           return freelancerProfileDTO;
       }catch (Exception e){
           System.out.println(e);
       }
        return null;
    }


}
