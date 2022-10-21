package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Career;
import lombok.Data;

import java.util.List;

@Data
public class CareerResponse {
    private List<Career> careers;
    private int pageIndex;
    private int totalPages;
}
