package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.model.Report;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReportService {
    void addReport(Report r);
    Page<Report> getReportByPaging(int offset, int pagesize);
}
