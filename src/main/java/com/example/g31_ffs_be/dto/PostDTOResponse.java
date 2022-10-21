package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDTOResponse {
    private List<PostDTO> posts;
    private int pageIndex;
    private int totalPages;
}
