package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.FreelancerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FreelancerServiceImpl implements FreelancerService {
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    WorkExperienceRepository workExperienceRepository;
    @Autowired JobSavedRepository jobSavedRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired JobRequestRepository jobRequestRepository;

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
            fa.setIsBanned(u.getIsBanned());
            fa.setEmail(u.getAccount().getEmail());
            fa.setIsBanned(u.getIsBanned());
            fa.setAccountBalance(u.getAccountBalance() != null ? u.getAccountBalance() : 0);
            fas.add(fa);
        }
        return fas;
    }

    @Override
    public APIResponse<FreelancerAdminDto> getFreelancerByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        APIResponse apiResponse=new APIResponse();
        Page<Freelancer> page = freelancerRepository.getFreelancerByName(name, pageable);
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setResults(convertListFreelancerDto(page.getContent()));
        apiResponse.setTotalResults(page.getTotalElements());
        apiResponse.setPageIndex(pageNo+1);
          return apiResponse;
    }

    @Override
    public FreelancerDetailDto getDetailFreelancer(String id) {
        try {
            Freelancer f = freelancerRepository.getDetailFreelancer(id);
            FreelancerDetailDto fd= mapToFreelancerDetailDTO(f);
            double star = 0;
            User u = f.getUser();
            Set<Feedback> feedbacks=u.getFeedbackTos();
            for (Feedback feedback : feedbacks)
                star += feedback.getStar();
            star = star / feedbacks.size();
            fd.setFeedbackTos(feedbacks);
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
        Page<Freelancer>page=freelancerRepository.getTop5ByName(name,PageRequest.of(0,5));
        List<Freelancer> freelancers = page.getContent();
        return convertListFreelancerDto(freelancers);
    }

    @Override
    public FreelancerProfileDTO getFreelancerProfile(String id) {
       try {
        Freelancer freelancer = freelancerRepository.getProfileFreelancer(id);

           FreelancerProfileDTO freelancerProfileDTO = new FreelancerProfileDTO();
           freelancerProfileDTO.setId(id);
           freelancerProfileDTO.setGender(freelancer.getGender());
           freelancerProfileDTO.setAvatar(freelancer.getUser().getAvatar());
           freelancerProfileDTO.setPhone(freelancer.getUser().getPhone());
           freelancerProfileDTO.setFullName(freelancer.getUser().getFullName());
           freelancerProfileDTO.setAddress(freelancer.getUser().getAddress());
           freelancerProfileDTO.setCity(freelancer.getUser().getCity());
           freelancerProfileDTO.setCountry(freelancer.getUser().getCountry());
           List<SkillDTO> skillDTOS = new ArrayList<>();
           for (Skill s : freelancer.getSkills()) {
               SkillDTO skillDTO = new SkillDTO();
               skillDTO.setId(s.getId());
               skillDTO.setName(s.getName());
               skillDTOS.add(skillDTO);
           }
           freelancerProfileDTO.setSkills(skillDTOS);
           freelancerProfileDTO.setCostPerHour(freelancer.getCostPerHour());
           freelancerProfileDTO.setEmail(freelancer.getUser().getAccount().getEmail());
           freelancerProfileDTO.setDescription(freelancer.getDescription());
           freelancerProfileDTO.setCv(freelancer.getCv());
           List<EducationDTO> educationDTOS = new ArrayList<>();
           for (Education e : freelancer.getEducations()
           ) {
               EducationDTO educationDTO = new EducationDTO();
               educationDTO.setId(e.getId().toString());
               educationDTO.setUniversity(e.getUniversity());
               educationDTO.setMajor(e.getMajor());
               educationDTO.setTo(e.getTo().toString());
               educationDTO.setFrom(e.getFrom().toString());
               educationDTO.setLevel(e.getLevel());
               educationDTOS.add(educationDTO);
           }
           freelancerProfileDTO.setEducations(educationDTOS);
           List<WorkExperienceDTO> workExperienceDTOS = new ArrayList<>();
           for (WorkExperience w : freelancer.getWorkExperiences()
           ) {
               WorkExperienceDTO workExperienceDTO = new WorkExperienceDTO();
               workExperienceDTO.setId(w.getId().toString());
               workExperienceDTO.setDescription(w.getDescription());
               workExperienceDTO.setMonthFrom(w.getMonthFrom());
               workExperienceDTO.setYearFrom(w.getYearFrom());
               workExperienceDTO.setMonthTo(w.getMonthTo());
               workExperienceDTO.setYearTo(w.getYearTo());
               workExperienceDTO.setCompanyName(w.getCompanyName());
               workExperienceDTO.setPosition(w.getPosition());
               workExperienceDTOS.add(workExperienceDTO);
           }
           freelancerProfileDTO.setWorkExps(workExperienceDTOS);
           freelancerProfileDTO.setBirthDate(freelancer.getBirthdate());
           freelancerProfileDTO.setSubCareer(freelancer.getSubCareer().getName());
           double star = 0;
           User u = freelancer.getUser();
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
    @Override
    public void addEducation(Education education, String freelancerId) {
        Freelancer f=new Freelancer();
        f.setId(freelancerId);
        education.setFreelancer(f);
        educationRepository.save(education);
    }

    @Override
    public void updateEducation(Education education) {
        educationRepository.save(education);
    }

    @Override
    public void deleteEducation(int id) {
        educationRepository.deleteById(id);
    }

    @Override
    public void addWorkExperience(WorkExperience workExperience, String freelancerId) {
        Freelancer f=new Freelancer();
        f.setId(freelancerId);
        workExperience.setFreelancer(f);
        workExperienceRepository.save(workExperience);
    }

    @Override
    public void deleteWorkExperience(int id) {
        workExperienceRepository.deleteById(id);
    }

    @Override
    public void updateWorkExperience(WorkExperience workExperience) {
        workExperienceRepository.save(workExperience);
    }

    @Override
    public void addSkill(List<Skill> skill,String freelancerId) {
        Freelancer freelancer=freelancerRepository.findById(freelancerId).get();
        freelancer.getSkills().addAll(skill);
        freelancerRepository.save(freelancer);
    }
    @Override
    public void deleteSkill(Skill skill,String freelancerId) {
        Freelancer freelancer=freelancerRepository.findById(freelancerId).get();
        Set<Skill> skills= new LinkedHashSet<>();
        skills= freelancer.getSkills();
        for (Skill s : skills) {
            if (s.getId()==skill.getId()) {
                skills.remove(s);
                freelancerRepository.save(freelancer);
                break;
            }
        }

    }

    @Override
    public void updateProfile(RegisterDto registerDto) {
        try{
            String id=registerDto.getId();
            User user=userRepository.getReferenceById(id);
            user.setFullName(registerDto.getFullName());
            user.setAddress(registerDto.getAddress());
            user.setCity(registerDto.getCity());
            user.setCountry(registerDto.getCountry());
            user.setPhone(registerDto.getPhone());
            userRepository.save(user);
            Freelancer freelancer=freelancerRepository.getReferenceById(id);
            freelancer.setBirthdate(registerDto.getBirthdate());
            freelancer.setGender(registerDto.getGender());
            Subcareer s=new Subcareer();
            s.setId(registerDto.getSubCareer());
            freelancer.setSubCareer(s);
            freelancerRepository.save(freelancer);}
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public APIResponse<PostFindingDTO> getJobSaved(String freelancerId,int pageNo,int pageSize) {
     Pageable pageable=PageRequest.of(pageNo,pageSize);
        Page<Job> jobs=jobSavedRepository.getAllJobSaved(freelancerId,pageable);
        List<Job> paymentDTOResponseList=jobs.getContent();
        List<PostFindingDTO> listJobs=new ArrayList<>();
        for (Job j: paymentDTOResponseList){
            PostFindingDTO post=new PostFindingDTO();
            post.setPostID(j.getId());
            post.setJobTitle(j.getJobTitle());
            post.setSubCareer(j.getSubCareer().getName());
            String des=j.getDescription();
            if(des.length()>=100)
                des=des.substring(0,99);
            post.setDescription(des);
            post.setAttach(j.getAttach());
            post.setPaymentType(j.getPaymentType()==1?"Trả theo giờ":"Trả theo dự án");
            String message="Đã đăng cách đây ";
            long count= ChronoUnit.HOURS.between(j.getTime(), LocalDateTime.now());
            if(count<24)
                message+=count+" giờ";
            else if(count<24*7)
                message+=count/24+" ngày";
            else if(count<24*30)
                message+=count/(24*7)+" tuần";
            else{
                message+=count/(24*30)+" tháng";
            }

            post.setTimeCount(message);
            Locale vn = new Locale("en", "VN");
            NumberFormat vnFormat = NumberFormat.getCurrencyInstance(vn);
            /*  request.setAttribute("total", vnFormat.format(total).substring(3) + " VNĐ");*/
            post.setBudget(vnFormat.format(j.getBudget()).substring(3) + " VNĐ");
            post.setCreatedDate(j.getTime());
            post.setArea(j.getArea());
            post.setIsActive(j.getIsActive());
            post.setListSkills(j.getSkills());
            listJobs.add(post);
        }
        APIResponse<PostFindingDTO> postFindingDTOAPIResponse= new APIResponse();
        postFindingDTOAPIResponse.setResults(listJobs);
        postFindingDTOAPIResponse.setPageIndex(pageNo+1);
        postFindingDTOAPIResponse.setTotalPages(jobs.getTotalPages());
        postFindingDTOAPIResponse.setTotalResults(jobs.getTotalElements());
        return  postFindingDTOAPIResponse;
    }

    @Override
    public APIResponse<PostFindingDTO> getJobRequest(String freelancerId, int status, int pageNo, int pageSize) {
        Pageable pageable=PageRequest.of(pageNo,pageSize);
        Page<Job> jobs=null;
        switch ( status){
            case 0:jobs=jobRequestRepository.getJobRequestWithStatus(freelancerId,0,pageable); break;
            case 1:jobs=jobRequestRepository.getJobRequestWithStatus(freelancerId,1,pageable); break;
            case 2:jobs=jobRequestRepository.getJobRequestWithStatus(freelancerId,2,pageable); break;
            case -1:jobs=jobRequestRepository.getAllJobRequest(freelancerId,pageable); break;
        }

        List<Job> paymentDTOResponseList=jobs.getContent();
        List<PostFindingDTO> listJobs=new ArrayList<>();
        for (Job j: paymentDTOResponseList){
            PostFindingDTO post=new PostFindingDTO();
            post.setPostID(j.getId());
            post.setJobTitle(j.getJobTitle());
            post.setSubCareer(j.getSubCareer().getName());
            String des=j.getDescription();
            if(des.length()>=100)
                des=des.substring(0,99);
            post.setDescription(des);
            post.setAttach(j.getAttach());
            post.setPaymentType(j.getPaymentType()==1?"Trả theo giờ":"Trả theo dự án");
            String message="Đã đăng cách đây ";
            long count= ChronoUnit.HOURS.between(j.getTime(), LocalDateTime.now());
            if(count<24)
                message+=count+" giờ";
            else if(count<24*7)
                message+=count/24+" ngày";
            else if(count<24*30)
                message+=count/(24*7)+" tuần";
            else{
                message+=count/(24*30)+" tháng";
            }

            post.setTimeCount(message);
            Locale vn = new Locale("en", "VN");
            NumberFormat vnFormat = NumberFormat.getCurrencyInstance(vn);
            /*  request.setAttribute("total", vnFormat.format(total).substring(3) + " VNĐ");*/
            post.setBudget(vnFormat.format(j.getBudget()).substring(3) + " VNĐ");
            post.setCreatedDate(j.getTime());
            post.setArea(j.getArea());
            post.setIsActive(j.getIsActive());
            post.setListSkills(j.getSkills());
            listJobs.add(post);
        }
        APIResponse<PostFindingDTO> postFindingDTOAPIResponse= new APIResponse();
        postFindingDTOAPIResponse.setResults(listJobs);
        postFindingDTOAPIResponse.setPageIndex(pageNo+1);
        postFindingDTOAPIResponse.setTotalPages(jobs.getTotalPages());
        postFindingDTOAPIResponse.setTotalResults(jobs.getTotalElements());
        return  postFindingDTOAPIResponse;
    }


}
