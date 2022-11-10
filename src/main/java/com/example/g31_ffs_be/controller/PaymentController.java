package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.PaymentDTO;
import com.example.g31_ffs_be.dto.PaymentDTOResponse;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
//@PreAuthorize("hasAuthority('recruiter') or hasAuthority('freelancer') or hasAuthority('staff') ")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    StaffRepository staffRepository;
    @GetMapping("/paymentSearch")
    public ResponseEntity<?> getAllPaymentSearchPaging(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                @RequestParam(name = "status", defaultValue = "-1") int status,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo,
                                                @RequestParam(name = "sortValue", defaultValue = "0") String sortValue) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
            return new ResponseEntity<>(paymentService.getAllPaymentSearchPaging(pageIndex,10,keyword,status,status,sortValue), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Không có dữ liệu trang này "+e, HttpStatus.NO_CONTENT);
        }

    }
}
