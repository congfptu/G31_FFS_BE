package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public void updateStatusPayment(PaymentDTO paymentDTO) {

    }

    private PaymentDTO mapToPaymentDTO(RequestPayment requestPayment){
        PaymentDTO paymentDTO=mapper.map(requestPayment, PaymentDTO.class);
        return paymentDTO;
    }

    @Override
    public APIResponse<PaymentDTO> getAllPaymentSearchPaging(int pageNumber, int pageSize, String keyword, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        APIResponse<PaymentDTO> apiResponse=new APIResponse<>();
        Page<RequestPayment> paymentPaging=paymentRepository.getRequestPaymentSearchPaging(keyword,pageable);
        List<RequestPayment> paymentDTOResponseList=paymentPaging.getContent();
        List<PaymentDTO> paymentDTOS=new ArrayList<>();
        for (RequestPayment requestPayment: paymentDTOResponseList){
            PaymentDTO payment=new PaymentDTO();
            payment.setId(requestPayment.getId());
            payment.setUserId(requestPayment.getUser().getId());
            payment.setPaymentCode(requestPayment.getPaymentCode());
            payment.setStatus(requestPayment.getStatus());
            payment.setMoney(requestPayment.getAmount());
            payment.setDateRequest(requestPayment.getDateRequest());
            paymentDTOS.add(payment);
        }
        apiResponse.setResults(paymentDTOS);
        apiResponse.setTotalPages(paymentPaging.getTotalPages());
        apiResponse.setTotalResults(paymentPaging.getTotalElements());
        apiResponse.setPageIndex(pageNumber+1);
        return apiResponse;
    }

}
