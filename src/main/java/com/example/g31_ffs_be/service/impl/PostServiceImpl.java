package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.PostRepository;
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
        RecruiterDto recruiterDto= new RecruiterDto();
        recruiterDto.setPhone(createdBy.getUser().getPhone());
        recruiterDto.setId(createdBy.getId());
        recruiterDto.setCompanyName(createdBy.getCompanyName());
        recruiterDto.setWebsite(createdBy.getWebsite());
        recruiterDto.setNumberOfFeedback(createdBy.getUser().getFeedbackTos().size());
        recruiterDto.setTotalPosted(createdBy.getJobs().size());

        double star = 0;
        User u = job.getCreateBy().getUser();
        for (Feedback feedback : u.getFeedbackTos())
            star += feedback.getStar();
        star = star / u.getFeedbackTos().size();
        recruiterDto.setStar(star);
        postDetailDTO.setCreateBy(recruiterDto);
        postDetailDTO.setTotalApplied(postRepository.getTotalAppliedById(createdBy.getId()));
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
        long count=ChronoUnit.HOURS.between(job.getTime(), LocalDateTime.now());
        if(count<24)
            message+=count+" giờ";
        else if(count<24*7)
            message+=count/24+" ngày";
        else if(count<24*30)
            message+=count/(24*7)+" tuần";
        else{
            message+=count/(24*30)+" tháng";
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
        APIResponse<PostFindingDTO> paymentDTOResponse = new APIResponse<>();
        Page<Job> jobs;

        if(isMemberShip)
                jobs=postRepository.getAllJobMemberShipSearchAll(area,keyword,paymentType,sub_career_id,pageable);
        else
                    jobs=postRepository.getAllJobNormalSearchAll(area,keyword,paymentType,sub_career_id,pageable);
        if(jobs!=null) {
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
                long count = ChronoUnit.HOURS.between(j.getTime(), LocalDateTime.now());
                if (count < 24)
                    message += count + " giờ";
                else if (count < 24 * 7)
                    message += count / 24 + " ngày";
                else if (count < 24 * 30)
                    message += count / (24 * 7) + " tuần";
                else {
                    message += count / (24 * 30) + " tháng";
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

            paymentDTOResponse.setResults(listJobs);
            paymentDTOResponse.setPageIndex(pageNumber + 1);
            paymentDTOResponse.setTotalPages(jobs.getTotalPages());
            paymentDTOResponse.setTotalResults(jobs.getTotalElements());
        }
        return  paymentDTOResponse;
    }



    private PostDTO mapToPostDTO(Job job){
        PostDTO postDTO=mapper.map(job, PostDTO.class);
        return postDTO;
    }
    private PostDetailDTO mapToPostDetailDTO(Job job){
        PostDetailDTO postDetailDTO=mapper.map(job, PostDetailDTO.class);
        return postDetailDTO;
    }
}
