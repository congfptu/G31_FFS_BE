package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.PaymentDTOResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.model.PostDetailDTO;

public interface PostService {
    PostDTOResponse getAllPostPaging(int pageNumber, int pageSize, String keyword, Boolean isApproved, String sortValue);
    PostDetailDTO getPostDetail(String id);
}
