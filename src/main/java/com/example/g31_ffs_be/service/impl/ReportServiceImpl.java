package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.ReportDTO;
import com.example.g31_ffs_be.dto.ReportDTOResponse;
import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.repository.ReportRepository;
import com.example.g31_ffs_be.service.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ReportRepository reportRepository;
    @Override
    public void addReport(Report r) {

    }

    @Override
    public APIResponse<ReportDTO> getAllReportByKeyword(int pageNumber, int pageSize, String keyword, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<ReportDTO> apiResponse=new APIResponse<>();
        Page<Report> page=reportRepository.getAllReport(keyword,pageable);
        List<Report> paymentDTOResponseList=page.getContent();
        List<ReportDTO> reportDTOS=new ArrayList<>();
        for (Report report: paymentDTOResponseList){
            ReportDTO reportDTO=new ReportDTO();
            reportDTO.setCreatedBy(report.getFrom().getFullName());
            reportDTO.setTitle(report.getTitle());
            reportDTO.setContent(report.getContent());
            reportDTO.setCreatedDate(report.getDateCreated());
            reportDTOS.add(reportDTO);
        }
        apiResponse.setResults(reportDTOS);
        apiResponse.setPageIndex(pageNumber+1);
        apiResponse.setTotalResults(page.getTotalElements());
        apiResponse.setTotalPages(page.getTotalPages());
        return apiResponse;
    }
    private ReportDTO mapToReportDTO(Report report){
        ReportDTO reportDTO=mapper.map(report, ReportDTO.class);
        return reportDTO;
    }
}
