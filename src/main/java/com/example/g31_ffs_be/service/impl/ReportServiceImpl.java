package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.PostDTO;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.ReportDTO;
import com.example.g31_ffs_be.dto.ReportDTOResponse;
import com.example.g31_ffs_be.model.Job;
import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.repository.ReportRepository;
import com.example.g31_ffs_be.service.ReportService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
    public ReportDTOResponse getReportPagingByDateOrCreatedBy(int pageNumber, int pageSize, String keyword,String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Report> paymentPaging=reportRepository.getReportByCreatedByOrCreatedDate(keyword,pageable);
        List<Report> paymentDTOResponseList=paymentPaging.getContent();
        List<ReportDTO> fas=new ArrayList<>();
        for (Report f: paymentDTOResponseList){
            ReportDTO fa=new ReportDTO();
            fa=mapToReportDTO(f);
            fa.setCreatedBy(f.getFrom().getId());
            fa.setCreatedBy(f.getFrom().getFullName());
            fa.setTitle(f.getTitle());
            fa.setContent(f.getContent());
            fa.setCreatedDate(f.getDateCreated());
            fas.add(fa);
        }
        ReportDTOResponse reportDTOResponse= new ReportDTOResponse();
        reportDTOResponse.setReports(fas);
        reportDTOResponse.setPageIndex(pageNumber+1);
        reportDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return reportDTOResponse;
    }
    private ReportDTO mapToReportDTO(Report report){
        ReportDTO reportDTO=mapper.map(report, ReportDTO.class);
        return reportDTO;
    }
}
