package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.NotificationDTO;
import com.example.g31_ffs_be.dto.RequestPaymentDto;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.model.User;

import java.util.List;

public interface UserService {
   
    void addUser(User u);
    void banUser(String id);
    APIResponse<RequestPayment> searchTransactionHistoryByTime(String from, String to, String userId, int pageNo, int pageSize);
    Boolean rechargeMoney(RequestPaymentDto requestPaymentDto);
    Boolean verify(String verificationCode);

    List<NotificationDTO> getTop10Notifications(String userId);


}
