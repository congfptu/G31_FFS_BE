package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    SubCareerRepository subCareerRepository;
    @Autowired
    FeeRepository feeRepository;

    @Override
    public APIResponse<PostDTO> getAllPostByAdmin(int pageNumber, int pageSize, String keyword, int status) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging = postRepository.getAllJobByStatus(keyword, status, pageable);
        APIResponse<PostDTO> apiResponse = new APIResponse<>();
        List<Job> paymentDTOResponseList = paymentPaging.getContent();
        List<PostDTO> postDTOS = new ArrayList<>();
        for (Job f : paymentDTOResponseList) {
            PostDTO postDTO = new PostDTO();
            postDTO = mapToPostDTO(f);
            postDTO.setId(f.getId());
            postDTO.setCreatedDate(f.getTime());
            postDTO.setCreatedBy(f.getCreateBy().getCompanyName());
            postDTO.setJobTitle(f.getJobTitle());
            postDTO.setIsApproved(f.getIsApproved());
            Optional<Staff> staff = Optional.ofNullable(f.getApprovedBy());
            try {
                postDTO.setApprovedBy(f.getApprovedBy().getFullName());
            } catch (Exception e) {
                postDTO.setApprovedBy(null);
            }
            postDTOS.add(postDTO);
        }
        apiResponse.setResults(postDTOS);
        apiResponse.setPageIndex(pageNumber + 1);
        apiResponse.setTotalResults(paymentPaging.getTotalElements());
        apiResponse.setTotalPages(paymentPaging.getTotalPages());
        return apiResponse;
    }

    @Override
    public PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging = postRepository.getRequestPostSearchByNamePaging(keyword, pageable);
        List<Job> paymentDTOResponseList = paymentPaging.getContent();
        List<PostDTO> fas = new ArrayList<>();
        for (Job f : paymentDTOResponseList) {
            PostDTO fa = new PostDTO();
            fa = mapToPostDTO(f);
            fa.setId(f.getId());
            fa.setCreatedDate(f.getTime());
            fa.setCreatedBy(f.getCreateBy().getCompanyName());
            fa.setJobTitle(f.getJobTitle());
            fa.setIsApproved(f.getIsApproved());
            if (f.getApprovedBy() != null)
                fa.setApprovedBy(f.getApprovedBy().getFullName());
            fas.add(fa);
        }
        PostDTOResponse paymentDTOResponse = new PostDTOResponse();
        paymentDTOResponse.setPosts(fas);
        paymentDTOResponse.setPageIndex(pageNumber + 1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return paymentDTOResponse;
    }

    @Override
    public PostDetailDTO getPostDetail(String freelancerId, int id) {
        Job job = postRepository.getJobDetail(id);
        if (job == null || !job.getIsActive()) return null;
        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setPostID(job.getId());
        Recruiter createdBy = job.getCreateBy();
        User user = createdBy.getUser();
        RecruiterDto recruiterDto = new RecruiterDto();
        recruiterDto.setPhone(user.getPhone());
        recruiterDto.setId(createdBy.getId());
        recruiterDto.setEmail(user.getAccount().getEmail());
        recruiterDto.setCompanyName(createdBy.getCompanyName());
        recruiterDto.setWebsite(createdBy.getWebsite());
        recruiterDto.setNumberOfFeedback(user.getFeedbackTos().size());
        recruiterDto.setTotalPosted(createdBy.getJobs().size());

        double star = 0;

        /*for (Feedback feedback : user.getFeedbackTos())
            star += feedback.getStar();
        star = star / user.getFeedbackTos().size();*/
        recruiterDto.setStar(user.getStar());
        postDetailDTO.setCreateBy(recruiterDto);
        int totalApplied = 0;
        for (Job j : createdBy.getJobs())
            totalApplied += j.getJobRequests().size();
        postDetailDTO.setTotalApplied(totalApplied);
        postDetailDTO.setJobTitle(job.getJobTitle());
        postDetailDTO.setSubCareer(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        int isApply = -1;
        for (JobRequest j : job.getJobRequests()) {
            if (j.getFreelancer().getId().equals(freelancerId))
                isApply = j.getStatus();
        }
        boolean isSaved = false;
        for (Freelancer freelancer : job.getFreelancers()) {
            if (freelancer.getId().equals(freelancerId)) {
                isSaved = true;
                break;
            }
        }
        postDetailDTO.setIsApply(isApply);
        postDetailDTO.setIsSave(isSaved);
        postDetailDTO.setPaymentType(job.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
        String message = "???? ????ng c??ch ????y ";
        LocalDateTime time = job.getTime();
        long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
        long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
        long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
        long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
        long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
        if (minutes < 60)
            message += minutes + " ph??t";
        else if (hours < 24)
            message += hours + " gi???";
        else if (days < 7)
            message += days + " ng??y";
        else if (weeks < 5)
            message += weeks + " tu???n";
        else if (months < 12) {
            message += months + " th??ng";
        } else {
            message += year + "n??m";
        }
        postDetailDTO.setTimeCount(message);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
        postDetailDTO.setBudget(vnFormat.format(job.getBudget()) + " VN??");
//        String formatDate= time.
        postDetailDTO.setTime(job.getTime());
        postDetailDTO.setIsActive(job.getIsActive());
        postDetailDTO.setIsApproved(job.getIsApproved());
        if (job.getApprovedBy() != null)
            postDetailDTO.setApprovedBy(job.getApprovedBy().getFullName());
        postDetailDTO.setListSkills(job.getSkills());
        return postDetailDTO;
    }

    @Override
    public PostDetailDTO getPostDetailAdmin(int id) {
        Job job = postRepository.getJobDetail(id);
        if (job == null) return null;
        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setPostID(job.getId());
        Recruiter createdBy = job.getCreateBy();
        User user = createdBy.getUser();
        RecruiterDto recruiterDto = new RecruiterDto();
        recruiterDto.setPhone(user.getPhone());
        recruiterDto.setId(createdBy.getId());
        recruiterDto.setEmail(user.getAccount().getEmail());
        recruiterDto.setCompanyName(createdBy.getCompanyName());
        recruiterDto.setWebsite(createdBy.getWebsite());
        recruiterDto.setNumberOfFeedback(user.getFeedbackTos().size());
        recruiterDto.setTotalPosted(createdBy.getJobs().size());

        double star = 0;

        /*for (Feedback feedback : user.getFeedbackTos())
            star += feedback.getStar();
        star = star / user.getFeedbackTos().size();*/
        recruiterDto.setStar(user.getStar());
        postDetailDTO.setCreateBy(recruiterDto);
        int totalApplied = 0;
        for (Job j : createdBy.getJobs())
            totalApplied += j.getJobRequests().size();
        postDetailDTO.setTotalApplied(totalApplied);
        postDetailDTO.setJobTitle(job.getJobTitle());
        postDetailDTO.setSubCareer(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        postDetailDTO.setPaymentType(job.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
        String message = "???? ????ng c??ch ????y ";
        LocalDateTime time = job.getTime();
        long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
        long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
        long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
        long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
        long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
        long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
        if (minutes < 60)
            message += minutes + " ph??t";
        else if (hours < 24)
            message += hours + " gi???";
        else if (days < 7)
            message += days + " ng??y";
        else if (weeks < 5)
            message += weeks + " tu???n";
        else if (months < 12) {
            message += months + " th??ng";
        } else {
            message += year + "n??m";
        }
        postDetailDTO.setTimeCount(message);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
        postDetailDTO.setBudget(vnFormat.format(job.getBudget()) + " VN??");
//        String formatDate= time.
        postDetailDTO.setTime(job.getTime());
        postDetailDTO.setIsActive(job.getIsActive());
        postDetailDTO.setIsApproved(job.getIsApproved());
        if (job.getApprovedBy() != null)
            postDetailDTO.setApprovedBy(job.getApprovedBy().getFullName());
        postDetailDTO.setListSkills(job.getSkills());
        return postDetailDTO;
    }

    @Override
    public PostDetailDTO viewDetailPostByRecruiter(String recruiterId, int id) {
        try {
            Job job = postRepository.getDetailPostByRecruiter(recruiterId, id);
            PostDetailDTO postDetailDTO = new PostDetailDTO();
            postDetailDTO.setPostID(job.getId());
            Recruiter createdBy = job.getCreateBy();
            if (!job.getCreateBy().getId().equals(recruiterId)) return null;
            double star = 0;

            postDetailDTO.setTotalApplied(job.getJobRequests().size());
            postDetailDTO.setJobTitle(job.getJobTitle());
            postDetailDTO.setSubCareer(job.getSubCareer().getName());
            postDetailDTO.setDescription(job.getDescription());
            postDetailDTO.setAttach(job.getAttach());
            postDetailDTO.setArea(job.getArea());
            postDetailDTO.setPaymentType(job.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
            String message = "???? ????ng c??ch ????y ";
            LocalDateTime time = job.getTime();
            long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
            long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
            long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
            long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
            long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
            long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
            if (minutes < 60)
                message += minutes + " ph??t";
            else if (hours < 24)
                message += hours + " gi???";
            else if (days < 7)
                message += days + " ng??y";
            else if (weeks < 5)
                message += weeks + " tu???n";
            else if (months < 12) {
                message += months + " th??ng";
            } else {
                message += year + "n??m";
            }
            postDetailDTO.setTimeCount(message);
            Locale localeVN = new Locale("vi", "VN");
            NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
            postDetailDTO.setBudget(vnFormat.format(job.getBudget()) + " VN??");
//        String formatDate= time.
            postDetailDTO.setTime(job.getTime());
            postDetailDTO.setIsActive(job.getIsActive());
            postDetailDTO.setIsApproved(job.getIsApproved());
            if (job.getApprovedBy() != null)
                postDetailDTO.setApprovedBy(job.getApprovedBy().getFullName());
            postDetailDTO.setListSkills(job.getSkills());
            return postDetailDTO;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<HotJobDto> getTopJob() {
        Pageable pageable=PageRequest.of(0,5);
        List<HotJobDto> lists = new ArrayList<>();
        try {
            Page page = postRepository.getAllTopJob(pageable);
            if (page == null) return null;
            List<Job> jobs = page.getContent();
            for (Job j : jobs) {
                HotJobDto hotJobDto = new HotJobDto();
                hotJobDto.setId(j.getId());
                hotJobDto.setTitle(j.getJobTitle());
                hotJobDto.setSubcareer(j.getSubCareer().getName());
                hotJobDto.setTypebudget(j.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
                hotJobDto.setBugget(j.getBudget());
                hotJobDto.setArea(j.getArea());
                RecruiterDto recruiterDto = new RecruiterDto();
                Recruiter recruiter = j.getCreateBy();
                recruiterDto.setId(recruiter.getId());
                recruiterDto.setAvatar(recruiter.getUser().getAvatar());
                recruiterDto.setStar(recruiter.getUser().getStar());
                recruiterDto.setCompanyName(recruiter.getCompanyName());
                recruiterDto.setWebsite(recruiter.getWebsite());
                recruiterDto.setPhone(recruiter.getUser().getPhone());

                hotJobDto.setRecruiter(recruiterDto);
                lists.add(hotJobDto);
            }
            return lists;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public APIResponse<PostFindingDTO> getJobSearch(int pageNumber, int pageSize, String area, String keyword, int paymentType, int sub_career_id, String userId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<PostFindingDTO> apiResponse = new APIResponse<>();
        Page<Job> jobs;
        User user = userRepository.getReferenceById(userId);
        if (user.getIsMemberShip())
            jobs = postRepository.getAllJobMemberShipSearchAll(userId, area, keyword, paymentType, sub_career_id, pageable);
        else
            jobs = postRepository.getAllJobNormalSearchAll(userId, area, keyword, paymentType, sub_career_id, pageable);
        if (jobs != null) {
            List<Job> jobList = jobs.getContent();
            List<PostFindingDTO> listJobs = new ArrayList<>();
            for (Job j : jobList) {
                PostFindingDTO post = new PostFindingDTO();
                post.setPostID(j.getId());
                post.setJobTitle(j.getJobTitle());
                post.setSubCareer(j.getSubCareer().getName());

                String des = j.getDescription();
                String[] words = des.split("\\s");
                String results="";
                if (words.length >= 25) {
                    for(int i=0;i<25;i++){
                        results+=words[i]+" ";
                    }
                }
                else{
                    for (String word:words) results+=word+" ";
                }
                post.setDescription(results+"...");
                post.setAttach(j.getAttach());
                post.setPaymentType(j.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
                String message = "???? ????ng c??ch ????y ";
                LocalDateTime time = j.getTime();
                long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
                long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
                long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
                long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
                long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
                long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
                if (minutes < 60)
                    message += minutes + " ph??t";
                else if (hours < 24)
                    message += hours + " gi???";
                else if (days < 7)
                    message += days + " ng??y";
                else if (weeks < 5)
                    message += weeks + " tu???n";
                else if (months < 12) {
                    message += months + " th??ng";
                } else {
                    message += year + "n??m";
                }

                post.setTimeCount(message);
                Locale localeVN = new Locale("vi", "VN");
                NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
                post.setBudget(vnFormat.format(j.getBudget()) + " VN??");
                post.setCreatedDate(j.getTime());
                post.setArea(j.getArea());
                post.setIsActive(j.getIsActive());
                post.setListSkills(j.getSkills());
                post.setIsTop(j.getIsTop());
                listJobs.add(post);
            }
            apiResponse.setResults(listJobs);
            apiResponse.setPageIndex(pageNumber + 1);
            apiResponse.setTotalPages(jobs.getTotalPages());
            apiResponse.setTotalResults(jobs.getTotalElements());
        }
        return apiResponse;
    }

    @Override
    public int createPost(PostCreateDto postCreateDto) {
        Job job = mapper.map(postCreateDto, Job.class);
        Recruiter recruiter = recruiterRepository.getReferenceById(postCreateDto.getRecruiterId());
        Subcareer subcareer = subCareerRepository.getReferenceById(postCreateDto.getSubCareerId());

        Set<Skill> skills = new LinkedHashSet<>();
        for (int i : postCreateDto.getSkillIds()) {
            Skill skill = new Skill();
            skill.setId(i);
            skills.add(skill);
        }
        job.setSkills(skills);
        job.setCreateBy(recruiter);
        job.setSubCareer(subcareer);
        job.setIsActive(true);
        job.setTime(LocalDateTime.now());
        job.setFee(recruiter.getUser().getIsMemberShip() ? 0 : feeRepository.getReferenceById(1).getPrice());
        recruiter.getUser().setAccountBalance(recruiter.getUser().getAccountBalance() - job.getFee());
        job.setIsApproved(2);
        int max = postRepository.getMaxId();
        postRepository.save(job);
        return max + 1;
    }

    @Override
    public APIResponse<PostHistoryDto> getAllJobPosted(String recruiterId, int status, String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<PostHistoryDto> apiResponse = new APIResponse<>();
        Page<Job> jobs = postRepository.getAllJobPosted(recruiterId, status, keyword, pageable);
        List<Job> jobList = jobs.getContent();
        List<PostHistoryDto> postHistoryDtoList = new ArrayList<>();
        for (Job j : jobList) {
            PostHistoryDto post = new PostHistoryDto();
            post.setJobTitle(j.getJobTitle());
            post.setDescription(j.getDescription());
            post.setSubCareer(j.getSubCareer().getName());
            post.setTimeCount(j.getSubCareer().getName());
            post.setJobId(j.getId());
            String message = "???? t???o ";
            LocalDateTime time = j.getTime();
            long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
            long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
            long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
            long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
            long months = ChronoUnit.MONTHS.between(time, LocalDateTime.now());
            long year = ChronoUnit.YEARS.between(time, LocalDateTime.now());
            if (minutes < 60)
                message += minutes + " ph??t";
            else if (hours < 24)
                message += hours + " gi???";
            else if (days < 7)
                message += days + " ng??y";
            else if (weeks < 5)
                message += weeks + " tu???n";
            else if (months < 12) {
                message += months + " th??ng";
            } else {
                message += year + "n??m";
            }
            post.setTimeCount(message);
            post.setPaymentType(j.getPaymentType() == 1 ? "Tr??? theo gi???" : "Tr??? theo d??? ??n");
            post.setStatus(j.getIsApproved());
            post.setTotalApplied(j.getJobRequests().size());
            postHistoryDtoList.add(post);

        }
        apiResponse.setResults(postHistoryDtoList);
        apiResponse.setTotalPages(jobs.getTotalPages());
        apiResponse.setPageIndex(pageNumber + 1);
        apiResponse.setTotalResults(jobs.getTotalElements());
        return apiResponse;
    }


    private PostDTO mapToPostDTO(Job job) {
        return mapper.map(job, PostDTO.class);
    }
}
