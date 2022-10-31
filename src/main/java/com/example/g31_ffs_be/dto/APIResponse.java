package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.List;
@Data
public class APIResponse<T> {
    List<T> results;
    private int pageIndex;
    private int totalPages;
    private long totalResults;
}
