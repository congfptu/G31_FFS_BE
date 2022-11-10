package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.ReportDTOResponse;
import com.example.g31_ffs_be.repository.ReportRepository;
import com.example.g31_ffs_be.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
//@PreAuthorize("hasAuthority('recruiter') or hasAuthority('freelancer') or hasAuthority('staff') ")
public class ReportController {
    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @GetMapping("/report")
    public ResponseEntity<?> getAllReportPaging(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
            int pageSize = 10;
                return new ResponseEntity<>(    reportService.getAllReportByKeyword(pageIndex, pageSize, keyword, null), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }

    }
}
