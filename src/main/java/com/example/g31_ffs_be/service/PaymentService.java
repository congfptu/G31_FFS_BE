package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.PaymentDTO;
import com.example.g31_ffs_be.dto.PaymentDTOResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.RequestPayment;

public interface PaymentService {
    void updateStatusPayment(PaymentDTO paymentDTO);

    APIResponse<PaymentDTO> getAllPaymentSearchPaging(int pageNumber, int pageSize, String keyword, int status,int defaultStatus, String sortValue);

}
