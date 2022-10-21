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
    public PaymentDTOResponse getAllPaymentPaging(int pageNumber, int pageSize, String keyword,Integer status, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<RequestPayment> paymentPaging=paymentRepository.getRequestPaymentPaging(keyword,status,pageable);
        List<RequestPayment> paymentDTOResponseList=paymentPaging.getContent();
        List<PaymentDTO> fas=new ArrayList<>();
        for (RequestPayment f: paymentDTOResponseList){
            PaymentDTO fa=new PaymentDTO();
            fa=mapToPaymentDTO(f);
            fa.setCode(f.getPaymentCode());
            fa.setState(f.getStatus());
            fa.setUser_id(f.getUser().getId());
            fa.setMoney(f.getAmount());
            fa.setDateApprove(f.getDateApproved());
            fa.setDateRequest(f.getDateRequest());
            fa.setDescription(f.getResponseMessage());
            fas.add(fa);
        }
        PaymentDTOResponse paymentDTOResponse= new PaymentDTOResponse();
        paymentDTOResponse.setPayments(fas);
        paymentDTOResponse.setPageIndex(pageNumber+1);
        paymentDTOResponse.setTotalPages(paymentPaging.getTotalPages());
        return paymentDTOResponse;
    }
}
