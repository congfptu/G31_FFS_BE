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
    public PostDetailDTO getPostDetail(String id) {
        Job job= postRepository.getJobDetail(id);
        PostDetailDTO postDetailDTO= mapToPostDetailDTO(job);
        postDetailDTO.setPostID(job.getId());
        RecruiterDto recruiterDto= new RecruiterDto();
        recruiterDto.setId(job.getCreateBy().getId());
        recruiterDto.setCompanyName(job.getCreateBy().getCompanyName());
        recruiterDto.setWebsite(job.getCreateBy().getWebsite());
        recruiterDto.setNumberOfFeedback(job.getCreateBy().getUser().getFeedbackTos().size());
        recruiterDto.setTotalPosted(job.getCreateBy().getJobs().size());

        double star = 0;
        User u = job.getCreateBy().getUser();
        for (Feedback feedback : u.getFeedbackTos())
            star += feedback.getStar();
        star = star / u.getFeedbackTos().size();
        recruiterDto.setStar(star);
        postDetailDTO.setCreateBy(recruiterDto);
        postDetailDTO.setJobTitle(job.getJobTitle());
        postDetailDTO.setSubCareer(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        postDetailDTO.setPaymentType("job.getPaymentType()");
        postDetailDTO.setBudget(job.getBudget());

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


        Page<Job> jobs=null;


        if(isMemberShip){
            if(paymentType==-1&&sub_career_id==-1){
                jobs=postRepository.getAllJobMemberShip(area,keyword,pageable);
            }
            else  if(paymentType>-1&&sub_career_id==-1){
                jobs=postRepository.getAllJobMemberShipWithPaymentType(area,keyword,paymentType,pageable);
            }
            else  if(paymentType == -1){
                jobs=postRepository.getAllJobMemberShipWithSub(area,keyword,sub_career_id,pageable);
            }
            else{
                jobs=postRepository.getAllJobMemberShipSearchAll(area,keyword,paymentType,sub_career_id,pageable);
            }
        }
        else {
                if(paymentType==-1&&sub_career_id==-1){
                    jobs=postRepository.getAllJobNormal(area,keyword,pageable);
                }
                else  if(paymentType>-1&&sub_career_id==-1){
                    jobs=postRepository.getAllJobNormalWithPaymentType(area,keyword,paymentType,pageable);
                }
                else  if(paymentType == -1){
                    jobs=postRepository.getAllJobNormalWithSub(area,keyword,sub_career_id,pageable);
                }
                else{
                    jobs=postRepository.getAllJobNormalSearchAll(area,keyword,paymentType,sub_career_id,pageable);
                }
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
             long count=ChronoUnit.HOURS.between(j.getTime(), LocalDateTime.now());
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
        APIResponse<PostFindingDTO> paymentDTOResponse= new APIResponse();
        paymentDTOResponse.setResults(listJobs);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(jobs.getTotalPages());
        paymentDTOResponse.setTotalResults(jobs.getTotalElements());
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
