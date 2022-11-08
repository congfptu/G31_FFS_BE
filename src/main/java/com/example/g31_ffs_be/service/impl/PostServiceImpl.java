package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.repository.RecruiterRepository;
import com.example.g31_ffs_be.repository.SubCareerRepository;
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
    private ModelMapper mapper;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    SubCareerRepository subCareerRepository;
    @Override
    public PostDTOResponse getAllPostByNameAndStatusPaging(int pageNumber, int pageSize, String keyword, String isApproved, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging=postRepository.getRequestPostByStatusAndDescription(keyword,isApproved,pageable);
        List<Job> paymentDTOResponseList=paymentPaging.getContent();
        List<PostDTO> fas=new ArrayList<>();
        for (Job f: paymentDTOResponseList){
            PostDTO fa=new PostDTO();
            fa=mapToPostDTO(f);
            fa.setId(f.getId());
            fa.setCreatedDate(f.getTime());
            fa.setCreatedBy(f.getCreateBy().getCompanyName());
            fa.setJobTitle(f.getJobTitle());
            fa.setIsApproved(f.getIsApproved());
             // fa.setApprovedBy(f.getApprovedBy().getFullname());
            Optional<Staff> staff= Optional.ofNullable(f.getApprovedBy());
            try{
                fa.setApprovedBy(f.getApprovedBy().getFullName());
            }
            catch (Exception e){
                fa.setApprovedBy(null);
            }
            fas.add(fa);
        }
        PostDTOResponse paymentDTOResponse= new PostDTOResponse();
        paymentDTOResponse.setPosts(fas);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return paymentDTOResponse;
    }

    @Override
    public PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging=postRepository.getRequestPostSearchByNamePaging(keyword,pageable);
        List<Job> paymentDTOResponseList=paymentPaging.getContent();
        List<PostDTO> fas=new ArrayList<>();
        for (Job f: paymentDTOResponseList){
            PostDTO fa=new PostDTO();
            fa=mapToPostDTO(f);
            fa.setId(f.getId());
            fa.setCreatedDate(f.getTime());
            fa.setCreatedBy(f.getCreateBy().getCompanyName());
            fa.setJobTitle(f.getJobTitle());
            fa.setIsApproved(f.getIsApproved());
            fa.setApprovedBy(f.getApprovedBy().getFullName());
            fas.add(fa);
        }
        PostDTOResponse paymentDTOResponse= new PostDTOResponse();
        paymentDTOResponse.setPosts(fas);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return paymentDTOResponse;
    }

    @Override
    public PostDetailDTO getPostDetail(String freelancerId,int id) {
        Job job= postRepository.getJobDetail(id);
        PostDetailDTO postDetailDTO=new PostDetailDTO();
        postDetailDTO.setPostID(job.getId());
        Recruiter createdBy=job.getCreateBy();
        User user=createdBy.getUser();
        RecruiterDto recruiterDto= new RecruiterDto();
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
        int totalApplied=0;
        for(Job j:createdBy.getJobs())
            totalApplied+=j.getJobRequests().size();
        postDetailDTO.setTotalApplied(totalApplied);
        postDetailDTO.setJobTitle(job.getJobTitle());
        postDetailDTO.setSubCareer(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        int isApply=-1;
        for(JobRequest j:job.getJobRequests()){
            if(j.getFreelancer().getId().equals(freelancerId))
                isApply=j.getStatus();
        }
        boolean isSaved=false;
        for(Freelancer freelancer: job.getFreelancers()){
            if(freelancer.getId().equals(freelancerId))
                isSaved=true;
        }
        postDetailDTO.setIsApply(isApply);
        postDetailDTO.setIsSave(isSaved);
        postDetailDTO.setPaymentType(job.getPaymentType()==1?"Trả theo giờ":"Trả theo dự án");
        String message="Đã đăng cách đây ";
        LocalDateTime time=job.getTime();
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
        postDetailDTO.setTimeCount(message);
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vnFormat = NumberFormat.getInstance(localeVN);
        postDetailDTO.setBudget(vnFormat.format(job.getBudget()) + " VNĐ");
//        String formatDate= time.
        postDetailDTO.setTime(job.getTime());
        postDetailDTO.setIsActive(job.getIsActive());
        postDetailDTO.setIsApproved(job.getIsApproved());
        postDetailDTO.setApprovedBy(job.getApprovedBy().getFullName());
        postDetailDTO.setListSkills(job.getSkills());
        return postDetailDTO;
    }

    @Override
    public APIResponse<PostFindingDTO> getJobSearch(int pageNumber, int pageSize, String area,String keyword,int paymentType,int sub_career_id,Boolean isMemberShip) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<PostFindingDTO> apiResponse = new APIResponse<>();
        Page<Job> jobs;

        if(isMemberShip)
                jobs=postRepository.getAllJobMemberShipSearchAll(area,keyword,paymentType,sub_career_id,pageable);
        else
                    jobs=postRepository.getAllJobNormalSearchAll(area,keyword,paymentType,sub_career_id,pageable);
        if(jobs!=null) {
            List<Job> jobList = jobs.getContent();
            List<PostFindingDTO> listJobs = new ArrayList<>();
            for (Job j : jobList) {
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

            apiResponse.setResults(listJobs);
            apiResponse.setPageIndex(pageNumber + 1);
            apiResponse.setTotalPages(jobs.getTotalPages());
            apiResponse.setTotalResults(jobs.getTotalElements());
        }
        return  apiResponse;
    }

    @Override
    public int createPost(PostCreateDto postCreateDto) {
        Job job=mapper.map(postCreateDto,Job.class);
        Recruiter recruiter=recruiterRepository.getReferenceById(postCreateDto.getRecruiterId());
        Subcareer subcareer=subCareerRepository.getReferenceById(postCreateDto.getSubCareerId());

        Set<Skill> skills=new LinkedHashSet<>();
        for(int i:postCreateDto.getSkillIds()){
            Skill skill=new Skill();
            skill.setId(i);
            skills.add(skill);
        }
        job.setSkills(skills);
        job.setCreateBy(recruiter);
        job.setSubCareer(subcareer);
        job.setIsActive(false);
        job.setTime(LocalDateTime.now());
        job.setFee(recruiter.getUser().getIsMemberShip()?0:0.5);
        recruiter.getUser().setAccountBalance(recruiter.getUser().getAccountBalance()-job.getFee());
        job.setIsApproved(2);
        postRepository.save(job);
        return (postRepository.getMaxId()+1);
    }

    @Override
    public APIResponse<PostHistoryDto> getAllJobPosted(String recruiterId, int status,String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<PostHistoryDto> apiResponse = new APIResponse<>();
        Page<Job> jobs=postRepository.getAllJobPosted(recruiterId,status,keyword,pageable);
        List<Job> jobList=jobs.getContent();
        List<PostHistoryDto> postHistoryDtoList=new ArrayList<>();
        for(Job j:jobList){
            PostHistoryDto post=new PostHistoryDto();
            post.setJobTitle(j.getJobTitle());
            post.setDescription(j.getDescription());
            post.setSubCareer(j.getSubCareer().getName());
            post.setTimeCount(j.getSubCareer().getName());
            String message = "Đã tạo ";
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
            post.setPaymentType(j.getPaymentType() == 1 ? "Trả theo giờ" : "Trả theo dự án");
            String statusMessage;
            if(j.getIsApproved()==1) statusMessage="Chấp thuận";
            else if(j.getIsApproved()==2) statusMessage="Đang chờ phản hồi";
            else  statusMessage="từ chối";
            post.setStatus(statusMessage);
            post.setTotalApplied(j.getJobRequests().size());
            postHistoryDtoList.add(post);

        }
        apiResponse.setResults(postHistoryDtoList);
        apiResponse.setTotalPages(jobs.getTotalPages());
        apiResponse.setPageIndex(pageNumber + 1);
        apiResponse.setTotalResults(jobs.getTotalElements());
        return apiResponse;
    }


    private PostDTO mapToPostDTO(Job job){
        return mapper.map(job, PostDTO.class);
    }
}
