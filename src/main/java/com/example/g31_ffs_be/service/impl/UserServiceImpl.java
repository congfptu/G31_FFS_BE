package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.RequestPaymentDto;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public void addUser(User u) {
        try {
            userRepository.save(u);
        } catch (Exception e) {

        }
    }

    @Override
    public void banUser(String id) {
        User u = userRepository.findById(id).get();
        u.setIsBanned(true);
        userRepository.save(u);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || !user.getIsBanned()) {
            return false;
        } else {
            user.setVerificationCode("");
            user.setIsBanned(false);
            userRepository.save(user);
            return true;
        }

    }


    @Override
    public APIResponse<RequestPayment> searchTransactionHistoryByTime(String from, String to, String userId, int pageNo, int pageSize) {
        APIResponse<RequestPayment> apiResponse = new APIResponse<>();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<RequestPayment> page = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate from_date = LocalDate.parse(from, formatter);
        from = from_date.atTime(0, 0).toString();
        LocalDate to_date = LocalDate.parse(to, formatter);
        to = to_date.atTime(23, 59).toString();
        page = paymentRepository.getRequestPaymentByFromTo(from, to, userId, pageable);
        apiResponse.setResults(page.getContent());
        apiResponse.setPageIndex(pageNo + 1);
        apiResponse.setTotalResults(page.getTotalElements());
        apiResponse.setTotalPages(page.getTotalPages());
        return apiResponse;
    }

    @Override
    public Boolean rechargeMoney(RequestPaymentDto requestPayment) {
        User user = userRepository.getReferenceById(requestPayment.getUserId());
        RequestPayment payment = new RequestPayment();
        if (paymentRepository.findByPaymentCode(requestPayment.getPaymentCode()).isPresent() == false) {
            user.setAccountBalance((requestPayment.getAmount()) + user.getAccountBalance());
            userRepository.save(user);
            User u = new User(requestPayment.getUserId());
            payment.setUser(u);
            payment.setPaymentCode(requestPayment.getPaymentCode());
            payment.setStatus(true);
            payment.setDateRequest(LocalDateTime.now());
            payment.setAmount(requestPayment.getAmount());
            paymentRepository.save(payment);
            return true;
        }
        return false;
    }
}
