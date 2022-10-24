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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
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
                                                @RequestParam(name = "status", defaultValue = "") Integer status,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = paymentRepository.getRequestPaymentSearchPaging(keyword,status,p).getTotalPages();

        if (status==1) {
            PaymentDTOResponse fas = paymentService.getAllPaymentSearchPaging(pageIndex, pageSize, keyword,1, null);
            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else if ( status==-1) {
            PaymentDTOResponse fas = paymentService.getAllPaymentPaging(pageIndex, pageSize, keyword,null);
            return new ResponseEntity<>(fas, HttpStatus.OK);
        }
        else if (status==0) {
            PaymentDTOResponse fas = paymentService.getAllPaymentSearchPaging(pageIndex, pageSize,keyword, 0,null);
            return new ResponseEntity<>(fas, HttpStatus.OK);
        }
        else if ( status==2) {
            PaymentDTOResponse fas = paymentService.getAllPaymentSearchPaging(pageIndex, pageSize,keyword,2, null);
            return new ResponseEntity<>(fas, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    }
//    id:1,
//    status:1,
//    approveBy:11,
//    responseMessage:'noi dung'
    @PutMapping("/payment/update")
    public ResponseEntity<?> updateStatus(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "id", defaultValue = "") String id,
                                          @NotEmpty  @RequestParam(name = "status", defaultValue = "") Integer status,
                                          @NotEmpty  @RequestParam(name = "approveBy", defaultValue = "") String approveBy,
                                          @NotEmpty  @RequestParam(name = "responseMessage", defaultValue = "") String responseMessage

                                          ) {
        try {
            if (paymentRepository.findById(id).isPresent()) {
                RequestPayment requestPayment = paymentRepository.findById(id).get();
                requestPayment.setResponseMessage(responseMessage);
                Staff staff=staffRepository.getReferenceById(approveBy);
               requestPayment.setApprovedBy(staff);
                requestPayment.setStatus(status);
                requestPayment.setDateApproved(Instant.now());
                paymentRepository.save(requestPayment);
            }
            return new ResponseEntity<>("Cập nhật payment thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cập nhật payment thất bại", HttpStatus.BAD_REQUEST);
        }
    }
}
