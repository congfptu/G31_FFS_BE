package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Job;
import com.example.g31_ffs_be.model.Recruiter;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                fa.setApprovedBy(f.getApprovedBy().getFullname());
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
            fa.setApprovedBy(f.getApprovedBy().getFullname());
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
        RecruiterDto recruiterDto=mapToRecruiterDTO(job.getCreateBy());
        postDetailDTO.setCreateBy(recruiterDto);
        postDetailDTO.setJobTitle(job.getJobTitle());
        postDetailDTO.setSubCareer(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        postDetailDTO.setPaymentType(job.getPaymentType());
        postDetailDTO.setBudget(job.getBudget());
//        String formatDate= time.
        postDetailDTO.setTime(job.getTime());
        postDetailDTO.setIsActive(job.getIsActive());
        postDetailDTO.setIsApproved(job.getIsApproved());
        postDetailDTO.setApprovedBy(job.getApprovedBy().getFullname());
        postDetailDTO.setListSkills(job.getListSkills());
        return postDetailDTO;
    }

    @Override
    public APIResponse getJobSearch(int pageNumber, int pageSize, String keyword1, String keyword2, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging=postRepository.getJobSearch(keyword1,keyword2,pageable);
        List<Job> paymentDTOResponseList=paymentPaging.getContent();
        List<PostFindingDTO> fas=new ArrayList<>();
        for (Job f: paymentDTOResponseList){
            PostFindingDTO fa=new PostFindingDTO();
          fa.setPostID(f.getId());
          fa.setJobTitle(f.getJobTitle());
          fa.setSubCareer(f.getSubCareer().getName());
          fa.setDescription(f.getDescription());
          fa.setAttach(f.getAttach());
          fa.setPaymentType(f.getPaymentType());
          fa.setBudget(f.getBudget());
          fa.setCreatedDate(f.getTime());
          fa.setArea(f.getArea());
          fa.setIsActive(f.getIsActive());
          fa.setListSkills(f.getListSkills());
            fas.add(fa);
        }
        APIResponse paymentDTOResponse= new APIResponse();
        paymentDTOResponse.setResults(fas);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return  paymentDTOResponse;
    }

    @Override
    public APIResponse getJobSearch(int pageNumber, int pageSize, String keyword1, String keyword2, String keyword3, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging=postRepository.getJobSearch(keyword1,keyword2,keyword3,pageable);
        List<Job> paymentDTOResponseList=paymentPaging.getContent();
        List<PostFindingDTO> fas=new ArrayList<>();
        for (Job f: paymentDTOResponseList){
            PostFindingDTO fa=new PostFindingDTO();
            fa.setPostID(f.getId());
            fa.setJobTitle(f.getJobTitle());
            fa.setSubCareer(f.getSubCareer().getName());
            fa.setDescription(f.getDescription());
            fa.setAttach(f.getAttach());
            fa.setPaymentType(f.getPaymentType());
            fa.setBudget(f.getBudget());
            fa.setCreatedDate(f.getTime());
            fa.setArea(f.getArea());
            fa.setIsActive(f.getIsActive());
            fa.setListSkills(f.getListSkills());
            fas.add(fa);
        }
        APIResponse paymentDTOResponse= new APIResponse();
        paymentDTOResponse.setResults(fas);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return  paymentDTOResponse;
    }

    private PostDTO mapToPostDTO(Job job){
        PostDTO postDTO=mapper.map(job, PostDTO.class);
        return postDTO;
    }
    private RecruiterDto mapToRecruiterDTO(Recruiter recruiter){
        RecruiterDto recruiterDto=mapper.map(recruiter, RecruiterDto.class);
        return recruiterDto;
    }
    private PostDetailDTO mapToPostDetailDTO(Job job){
        PostDetailDTO postDetailDTO=mapper.map(job, PostDetailDTO.class);
        return postDetailDTO;
    }
}
