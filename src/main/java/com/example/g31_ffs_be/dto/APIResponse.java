package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class APIResponse<T> {
    List<T> results=new ArrayList<>();
    private int pageIndex;
    private int totalPages;
    private long totalResults;
}
