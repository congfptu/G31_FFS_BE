package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.PostDetailDTO;
import com.example.g31_ffs_be.dto.PostFindingDTO;

public interface PostService {
    PostDTOResponse getAllPostByNameAndStatusPaging(int pageNumber, int pageSize, String keyword, String isApproved, String sortValue);
    PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue);
    PostDetailDTO getPostDetail(int id);

    APIResponse<PostFindingDTO> getJobSearch(int pageNumber, int pageSize, String area, String keyword, int paymentType, int sub_career_id,Boolean isMemberShip);
}
