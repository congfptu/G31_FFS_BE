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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.DecimalFormat;
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
    @Autowired
    JobSavedRepository jobSavedRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
    @Autowired  PostRepository postRepository;

    @Override
    public void addFreelancer(Freelancer f) {

    }

    private FreelancerAdminDto mapToFreeDTO(Freelancer freelancer) {
        FreelancerAdminDto freelancerAdminDto = mapper.map(freelancer, FreelancerAdminDto.class);
        return freelancerAdminDto;
    }

    private FreelancerDetailDto mapToFreelancerDetailDTO(Freelancer freelancer) {
        return mapper.map(freelancer, FreelancerDetailDto.class);
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
        APIResponse<FreelancerAdminDto> apiResponse = new APIResponse<>();
        Page<Freelancer> page = freelancerRepository.getFreelancerByName(name, pageable);
        apiResponse.setTotalPages(page.getTotalPages());
        apiResponse.setResults(convertListFreelancerDto(page.getContent()));
        apiResponse.setTotalResults(page.getTotalElements());
        apiResponse.setPageIndex(pageNo + 1);
        return apiResponse;
    }

    @Override
    public FreelancerDetailDto getFreelancerInfo(String id) {
        try {
            Freelancer f = freelancerRepository.getDetailFreelancer(id);
            FreelancerDetailDto fd = mapToFreelancerDetailDTO(f);
            User u = f.getUser();
            Set<Feedback> feedbacks = u.getFeedbackTos();
            fd.setFeedbackTos(feedbacks);
            fd.setStar(u.getStar());
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
        Page<Freelancer> page = freelancerRepository.getTop5ByName(name, PageRequest.of(0, 5));
        List<Freelancer> freelancers = page.getContent();
        return convertListFreelancerDto(freelancers);
    }

    @Override
    public FreelancerProfileDTO getFreelancerProfile(String id) {
        try {
            Freelancer freelancer = freelancerRepository.getDetailFreelancer(id);

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
            freelancerProfileDTO.setStar(freelancer.getUser().getStar());
            return freelancerProfileDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    @Override
    public FreelancerProfileDTO getDetailFreelancerByRecruiter(String recruiterId,String id) {
        try {
            Freelancer freelancer = freelancerRepository.getFreelancerDetailByRecruiter(id);

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
            boolean isApplied=false;
            for(JobRequest jobRequest:freelancer.getJobRequests()){
                if(jobRequest.getStatus()==1 && jobRequest.getJob().getCreateBy().getId().equals(recruiterId)) {
                    isApplied = true;
                    break;
                }
            }
            freelancerProfileDTO.setIsApplied(isApplied);
            freelancerProfileDTO.setWorkExps(workExperienceDTOS);
            freelancerProfileDTO.setBirthDate(freelancer.getBirthdate());
            freelancerProfileDTO.setSubCareer(freelancer.getSubCareer().getName());
            freelancerProfileDTO.setStar(freelancer.getUser().getStar());

            return freelancerProfileDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public void addEducation(Education education, String freelancerId) {
        Freelancer f = new Freelancer();
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
        Freelancer f = new Freelancer();
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
    public void addSkill(List<Skill> skill, String freelancerId) {
        for (Skill s : skill) {
            if (freelancerRepository.checkSkillExist(freelancerId, s.getId()) == null)
                freelancerRepository.insertFreelancerSkill(freelancerId, s.getId());
        }
    }

    @Override
    public int deleteSkill(String freelancerId, int skillId) {
        return freelancerRepository.deleteFreelancerSkill(freelancerId, skillId);
    }


    @Override
    public void updateProfile(RegisterDto registerDto) {
        try {

            String id = registerDto.getId();

            User user = userRepository.getReferenceById(id);
            user.setFullName(registerDto.getFullName());
            user.setAddress(registerDto.getAddress());
            user.setCity(registerDto.getCity());
            user.setCountry(registerDto.getCountry());
            user.setPhone(registerDto.getPhone());
            userRepository.save(user);
            Freelancer freelancer = freelancerRepository.getReferenceById(id);
            freelancer.setBirthdate(registerDto.getBirthdate());
            freelancer.setGender(registerDto.getGender());
            Subcareer s = new Subcareer();
            s.setId(registerDto.getSubCareer());
            freelancer.setSubCareer(s);
            freelancerRepository.save(freelancer);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public APIResponse<PostFindingDTO> getJobSaved(String freelancerId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> jobs = jobSavedRepository.getAllJobSaved(freelancerId, pageable);
        List<Job> paymentDTOResponseList = jobs.getContent();
        List<PostFindingDTO> listJobs = new ArrayList<>();
        for (Job j : paymentDTOResponseList) {
            PostFindingDTO post = new PostFindingDTO();
            post.setPostID(j.getId());
            post.setJobTitle(j.getJobTitle());
            post.setSubCareer(j.getSubCareer().getName());
            String des = j.getDescription();
            if (des.length() >= 100)
                des = des.substring(0, 99);
            post.setDescription(des);
            post.setAttach(j.getAttach());
            post.setPaymentType(j.getPaymentType() == 1 ? "Trả theo giờ" : "Trả theo dự án");
            String message = "Đã đăng cách đây ";
            LocalDateTime time=j.getTime();
            long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
            long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
            long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
            long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
            long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
            long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
            if (minutes < 60)
                message += minutes + " phút";
            else if (hours<24)
                message += hours+ " giờ";
            else if (days<7)
                message += days +" ngày";
            else if (weeks < 5)
                message += weeks + " tuần";
            else if (months < 12) {
                message += months+ " tháng";
            }
            else{
                message += year+ "năm";
            }


            post.setTimeCount(message);
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
            post.setBudget(vnFormat.format(j.getBudget()) + " VNĐ");
            post.setCreatedDate(j.getTime());
            post.setArea(j.getArea());
            post.setIsActive(j.getIsActive());
            post.setListSkills(j.getSkills());
            listJobs.add(post);
        }
        APIResponse<PostFindingDTO> postFindingDTOAPIResponse = new APIResponse<>();
        postFindingDTOAPIResponse.setResults(listJobs);
        postFindingDTOAPIResponse.setPageIndex(pageNo + 1);
        postFindingDTOAPIResponse.setTotalPages(jobs.getTotalPages());
        postFindingDTOAPIResponse.setTotalResults(jobs.getTotalElements());
        return postFindingDTOAPIResponse;
    }

    @Override
    public APIResponse<PostFindingDTO> getJobRequest(String freelancerId, int status, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        APIResponse<PostFindingDTO> postFindingDTOAPIResponse = new APIResponse<>();
        Page<Job> jobs = null;
        jobs = jobRequestRepository.getJobRequestWithStatus(freelancerId, status, pageable);
        if (jobs != null) {
            List<Job> paymentDTOResponseList = jobs.getContent();
            List<PostFindingDTO> listJobs = new ArrayList<>();
            for (Job j : paymentDTOResponseList) {
                PostFindingDTO post = new PostFindingDTO();
                post.setPostID(j.getId());
                post.setJobTitle(j.getJobTitle());
                post.setSubCareer(j.getSubCareer().getName());
                String des = j.getDescription();
                if (des.length() >= 100)
                    des = des.substring(0, 99);
                post.setDescription(des);
                post.setAttach(j.getAttach());
                post.setPaymentType(j.getPaymentType() == 1 ? "Trả theo giờ" : "Trả theo dự án");
                String message = "Đã đăng cách đây ";
                LocalDateTime time=j.getTime();
                long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
                long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
                long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
                long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
                long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
                long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
                if (minutes < 60)
                    message += minutes + " phút";
                else if (hours<24)
                    message += hours+ " giờ";
                else if (days<7)
                    message += days +" ngày";
                else if (weeks < 5)
                    message += weeks + " tuần";
                else if (months < 12) {
                    message += months+ " tháng";
                }
                else{
                    message += year+ "năm";
                }
                post.setTimeCount(message);
                Locale vn = new Locale("vi", "VN");
                NumberFormat vnFormat = NumberFormat.getInstance(vn);
                /*  request.setAttribute("total", vnFormat.format(total).substring(3) + " VNĐ");*/
                post.setBudget(vnFormat.format(j.getBudget()) + " VNĐ");
                post.setCreatedDate(j.getTime());
                post.setArea(j.getArea());
                post.setIsActive(j.getIsActive());
                if (status == -1) {
                    for (JobRequest jobRequest : j.getJobRequests()) {
                        if (jobRequest.getFreelancer().getId().equals(freelancerId) && jobRequest.getJob().getId().equals(j.getId()))
                            post.setIsApproved(jobRequest.getStatus());
                    }
                } else {
                    post.setIsApproved(status);
                }

                post.setListSkills(j.getSkills());
                listJobs.add(post);
            }

            postFindingDTOAPIResponse.setResults(listJobs);
            postFindingDTOAPIResponse.setPageIndex(pageNo + 1);
            postFindingDTOAPIResponse.setTotalPages(jobs.getTotalPages());
            postFindingDTOAPIResponse.setTotalResults(jobs.getTotalElements());
        }
        return postFindingDTOAPIResponse;
    }

    @Override
    public APIResponse<FreelancerFilterDto> getAllFreelancerByFilter(Boolean isMemberShip,String city, int costOption, int subCareer, List<Integer> skill,String keyword,int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Freelancer> page = null;
        if(isMemberShip)
        {
            switch (costOption) {
                case 1:

                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetweenAndIsMemberShip(city, skill, 0, -1, subCareer, keyword, pageable);
                    break;
                case 2:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetweenAndIsMemberShip(city, skill, 0, 100000, subCareer, keyword, pageable);
                    break;
                case 3:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetweenAndIsMemberShip(city, skill, 100000, 200000, subCareer, keyword, pageable);
                    break;
                case 4:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetweenAndIsMemberShip(city, skill, 200000, 500000, subCareer, keyword, pageable);
                    break;
                case 5:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetweenAndIsMemberShip(city, skill, 500000, -1, subCareer, keyword, pageable);
                    break;
            }
        }
        else {
            switch (costOption) {
                case 1:

                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetween(city, skill, 0, -1, subCareer, keyword, pageable);
                    break;
                case 2:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetween(city, skill, 0, 100000, subCareer, keyword, pageable);
                    break;
                case 3:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetween(city, skill, 100000, 200000, subCareer, keyword, pageable);
                    break;
                case 4:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetween(city, skill, 200000, 500000, subCareer, keyword, pageable);
                    break;
                case 5:
                    page = freelancerRepository.getAllFreelancerWithCostPerHourBetween(city, skill, 500000, -1, subCareer, keyword, pageable);
                    break;
            }
        }
        APIResponse<FreelancerFilterDto> apiResponse = new APIResponse<>();
        List<FreelancerFilterDto> filterDTOs = new ArrayList<>();
        if (page != null) {
            {
                for (Freelancer f : page.getContent()) {
                    FreelancerFilterDto filterDto = new FreelancerFilterDto();
                    filterDto.setId(f.getId());
                    filterDto.setSkills(f.getSkills());
                    filterDto.setAvatar(f.getUser().getAvatar());
                    filterDto.setCity(f.getUser().getCity());
                    filterDto.setFullName(f.getUser().getFullName());
                    filterDto.setSubCareer(f.getSubCareer().getName());
                    filterDto.setTotalFeedbacks(f.getUser().getFeedbackTos().size());
                    double star = f.getUser().getStar();
                    filterDto.setStar(Double.toString(star));
                    Locale vn = new Locale("vi", "VN");
                    NumberFormat vnFormat = NumberFormat.getInstance(vn);
                    filterDto.setCostPerHour(f.getCostPerHour() == null ? "0" : vnFormat.format(f.getCostPerHour()) + "VNĐ");
                    filterDto.setDescription(f.getDescription());
                    filterDTOs.add(filterDto);
                }
                apiResponse.setResults(filterDTOs);
                apiResponse.setPageIndex(pageNo + 1);
                apiResponse.setTotalPages(page.getTotalPages());
                apiResponse.setTotalResults(page.getTotalElements());
            }
        }
        return apiResponse;
    }

    @Override
    public APIResponse<FreelancerFilterDto> getFreelancerApplied(int jobId,String recruiterId,String city,List<Integer> skill,int subCareer,String keyword,int status,int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Job job=postRepository.getReferenceById(jobId);
        APIResponse<FreelancerFilterDto> apiResponse = new APIResponse<>();
        if(job.getCreateBy().getId().equals(recruiterId))
        {
        Page<Freelancer> freelancers = freelancerRepository.getFreelancerAppliedJob(jobId,city,skill, subCareer,keyword,status,pageable);

        List<FreelancerFilterDto> filterDTOs = new ArrayList<>();
        if (freelancers != null) {
            {
                for (Freelancer f : freelancers.getContent()) {
                    FreelancerFilterDto filterDto = new FreelancerFilterDto();
                    filterDto.setId(f.getId());
                    filterDto.setSkills(f.getSkills());
                    filterDto.setAvatar(f.getUser().getAvatar());
                    filterDto.setCity(f.getUser().getCity());
                    filterDto.setFullName(f.getUser().getFullName());
                    filterDto.setSubCareer(f.getSubCareer().getName());
                    NumberFormat formatter = new DecimalFormat("#0.0");
                    filterDto.setTotalFeedbacks(f.getUser().getFeedbackTos().size());
                    Double star = f.getUser().getStar();
                    if (star == 0)
                        filterDto.setStar("0");
                    else
                        filterDto.setStar(formatter.format(star));
                    Locale vn = new Locale("vi", "VN");
                    NumberFormat vnFormat = NumberFormat.getInstance(vn);
                    filterDto.setCostPerHour(f.getCostPerHour() == null ? "0" : vnFormat.format(f.getCostPerHour()) + "VNĐ");
                    filterDto.setDescription(f.getDescription());
                    filterDTOs.add(filterDto);
                }
                apiResponse.setResults(filterDTOs);
                apiResponse.setPageIndex(pageNo + 1);
                apiResponse.setTotalPages(freelancers.getTotalPages());
                apiResponse.setTotalResults(freelancers.getTotalElements());
            }
        }
        }
        return apiResponse;
    }
}
