package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Subcareer;
import lombok.Data;

import java.util.List;
@Data
public class SubCareerResponse {
    private List<Subcareer> subCareers;
    private int pageIndex;
    private int totalPages;
}
