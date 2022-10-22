package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.PaymentDTO;
import com.example.g31_ffs_be.dto.PaymentDTOResponse;
import com.example.g31_ffs_be.dto.PostDTO;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.model.Job;
import com.example.g31_ffs_be.model.PostDetailDTO;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public PostDTOResponse getAllPostPaging(int pageNumber, int pageSize, String keyword, Boolean isApproved, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Job> paymentPaging=postRepository.getRequestPostByStatusAndDescription(keyword,isApproved,pageable);
        List<Job> paymentDTOResponseList=paymentPaging.getContent();
        List<PostDTO> fas=new ArrayList<>();
        for (Job f: paymentDTOResponseList){
            PostDTO fa=new PostDTO();
            fa=mapToPostDTO(f);
            fa.setId(f.getId());
            fa.setTime(f.getTime());
            fa.setCreatedBy(f.getCreateBy().getCompanyName());
            fa.setJobTitle(f.getJobTitle());
            fa.setIsApproved(f.getIsApproved());
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
        postDetailDTO.setCreateBy(job.getCreateBy().getCompanyName());
        postDetailDTO.setJob_title(job.getJobTitle());
        postDetailDTO.setSub_career(job.getSubCareer().getName());
        postDetailDTO.setDescription(job.getDescription());
        postDetailDTO.setAttach(job.getAttach());
        postDetailDTO.setArea(job.getArea());
        postDetailDTO.setPayment_type(job.getPaymentType());
        postDetailDTO.setBudget(job.getBudget());
        postDetailDTO.setTime(job.getTime());
        postDetailDTO.setIs_Active(job.getIsActive());
        postDetailDTO.setIs_Approved(job.getIsApproved());
        postDetailDTO.setApproved_by(job.getApprovedBy().getFullname());
        return postDetailDTO;
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
