package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.PostDetailDTO;

public interface PostService {
    PostDTOResponse getAllPostByNameAndStatusPaging(int pageNumber, int pageSize, String keyword, Boolean isApproved, String sortValue);
    PostDTOResponse getAllPostSearchNamePaging(int pageNumber, int pageSize, String keyword, String sortValue);
    PostDetailDTO getPostDetail(String id);
}
