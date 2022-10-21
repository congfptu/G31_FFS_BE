package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.PaymentDTO;
import com.example.g31_ffs_be.dto.PaymentDTOResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.RequestPayment;

public interface PaymentService {
    void updateStatusPayment(PaymentDTO paymentDTO);

    PaymentDTOResponse getAllPaymentPaging(int pageNumber, int pageSize, String keyword,Integer status, String sortValue);
}
