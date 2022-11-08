package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.ReportDTO;
import com.example.g31_ffs_be.dto.ReportDTOResponse;
import com.example.g31_ffs_be.model.Report;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.List;

public interface ReportService {
    void addReport(Report r);
    APIResponse<ReportDTO> getAllReportByKeyword(int pageNumber, int pageSize, String keyword, String sortValue);
}
