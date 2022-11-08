package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.*;

public interface PostService {
    PostDTOResponse getAllPostByNameAndStatusPaging(int pageNumber, int pageSize, String keyword, String isApproved, String sortValue);
    PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue);
    PostDetailDTO getPostDetail(String freelancerId,int id);

    APIResponse<PostFindingDTO> getJobSearch(int pageNumber, int pageSize, String area, String keyword, int paymentType, int sub_career_id,Boolean isMemberShip);
    int createPost(PostCreateDto postCreateDto);
    APIResponse<PostHistoryDto> getAllJobPosted(String recruiterId,int status,String keyword,int pageNumber,int pageSize);


}
