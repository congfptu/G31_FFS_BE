package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.dto.ReportDTOResponse;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.repository.ReportRepository;
import com.example.g31_ffs_be.service.PostService;
import com.example.g31_ffs_be.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
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
        } catch (Exception e) {

        }
        int pageSize = 4;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = reportRepository.getReportByCreatedByOrCreatedDate(keyword,p).getTotalPages();

        if (totalPage >= pageIndex - 1) {
            ReportDTOResponse fas = reportService.getReportPagingByDateOrCreatedBy(pageIndex, pageSize, keyword, null);

            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    }
}
