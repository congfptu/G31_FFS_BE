package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.PostDetailDTO;

public interface PostService {
    PostDTOResponse getAllPostByNameAndStatusPaging(int pageNumber, int pageSize, String keyword, String isApproved, String sortValue);
    PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue);
    PostDetailDTO getPostDetail(String id);
    APIResponse getJobSearch(int pageNumber, int pageSize, String area,Double budget,String keyword,Boolean is_top, String sortValue);
    APIResponse getJobSearch(int pageNumber, int pageSize, String area,Double budget,String keyword,Boolean is_top,Integer sub_career_id, String sortValue);
}
