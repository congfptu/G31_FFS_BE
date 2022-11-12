package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.*;

public interface PostService {
    APIResponse<PostDTO> getAllPostByAdmin(int pageNumber, int pageSize, String keyword, int status);
    PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue);
    PostDetailDTO getPostDetail(String freelancerId,int id);
    PostDetailDTO getPostDetailAdmin(int id);

    APIResponse<PostFindingDTO> getJobSearch(int pageNumber, int pageSize, String area, String keyword, int paymentType, int sub_career_id,String userId);
    int createPost(PostCreateDto postCreateDto);
    APIResponse<PostHistoryDto> getAllJobPosted(String recruiterId,int status,String keyword,int pageNumber,int pageSize);
     PostDetailDTO viewDetailPostByRecruiter(String recruiterId,int id);


}
