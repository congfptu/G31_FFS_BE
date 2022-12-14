package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.NotificationRepository;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.repository.ServiceRepository;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@org.springframework.stereotype.Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    ServiceRepository serviceRepository;

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

   /* public Boolean verify(String verificationCode) {


    }*/


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
        if (!paymentRepository.findByPaymentCode(requestPayment.getPaymentCode()).isPresent()) {
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

    @Override
    public Boolean verify(String verificationCode) {
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
    public APIResponse<NotificationDTO> getTop10Notifications(String userId,int pageNo, int pageSize) {
        APIResponse<NotificationDTO> apiResponse=new APIResponse<>();
 Pageable pageable=PageRequest.of(pageNo,pageSize);
        List<NotificationDTO> notificationDTOS=new ArrayList<>();

        Page<Notification> page = notificationRepository.getNotificationByUserId(userId,pageable);
        if(page!=null) {
            for (Notification notification : page.getContent()) {
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setType(notification.getNotificationType());
                User from = notification.getFrom();
                notificationDTO.setUserId(from.getId());
                notificationDTO.setUserName(from.getFullName());
                notificationDTO.setAvatar(from.getAvatar());
                Job job = notification.getJob();
                notificationDTO.setPostId(job.getId());
                notificationDTO.setPostTitle(job.getJobTitle());
                String message = "";
                LocalDateTime time = notification.getDate();
                long minutes = ChronoUnit.MINUTES.between(time, LocalDateTime.now());
                long hours = ChronoUnit.HOURS.between(time, LocalDateTime.now());
                long days = ChronoUnit.DAYS.between(time, LocalDateTime.now());
                long weeks = ChronoUnit.WEEKS.between(time, LocalDateTime.now());
                if (minutes < 60)
                    message += minutes + " ph??t tr?????c";
                else if (hours < 24)
                    message += hours + " gi??? tr?????c";
                else if (days < 7)
                    message += days + " ng??y tr?????c";
                else if (weeks < 5)
                    message += weeks + " tu???n tr?????c";
                notificationDTO.setTime(message);
                notificationDTOS.add(notificationDTO);
            }
            User userTo = userRepository.getReferenceById(userId);
            userTo.setUnRead(0);
            userRepository.save(userTo);
            apiResponse.setResults(notificationDTOS);
            apiResponse.setTotalPages(page.getTotalPages());
            apiResponse.setTotalResults(page.getTotalElements());
            apiResponse.setPageIndex(pageNo+1);
        }
        return apiResponse;
    }
    public List<FreelancerAdminDto> getFreelancers(List<Object[]> objects){
        List<FreelancerAdminDto> listFreelancers=new ArrayList<>();
        if(objects!=null)
        {
            for(Object[] ob:objects)
            {

                FreelancerAdminDto freelancerAdminDto=new FreelancerAdminDto();
                freelancerAdminDto.setId(ob[0].toString());
                freelancerAdminDto.setEmail(ob[1].toString());
                freelancerAdminDto.setFullName((ob[2].toString()));
                freelancerAdminDto.setTotalMoneyUsed(Double.parseDouble(ob[3].toString()));
                listFreelancers.add(freelancerAdminDto);
            }
        }
        return listFreelancers;
    }
    public List<RecruiterAdminDto> getRecruiters(List<Object[]> objects){
        List<RecruiterAdminDto> listRecruiters=new ArrayList<>();
        if(objects!=null)
        {
            for(Object[] ob:objects)
            {
                RecruiterAdminDto recruiterAdminDto=new RecruiterAdminDto();
                recruiterAdminDto.setId(ob[0].toString());
                recruiterAdminDto.setEmail(ob[1].toString());
                recruiterAdminDto.setFullName((ob[2].toString()));
                recruiterAdminDto.setTotalMoneyUsed(Double.parseDouble(ob[3].toString()));
                listRecruiters.add(recruiterAdminDto);
            }
        }
        return listRecruiters;
    }
    @Override
    public ServiceDto getCurrentServiceByUserId(String userId) {
         Pageable pageable=PageRequest.of(0,1);
         ServiceDto serviceDto=new ServiceDto();
         try{
             Page<Service> servicePage=serviceRepository.getCurrentService(userId,pageable);
             Service service= servicePage.getContent().get(0);
             serviceDto.setId(service.getId());
             serviceDto.setServiceName(service.getServiceName());
             return serviceDto;
         }
         catch (Exception e){
             return null;
         }
    }

    @Override
    public TopUserHotDto getUserHotDto() {
        TopUserHotDto topUserHotDto=new TopUserHotDto();
        topUserHotDto.setFreelancers(getFreelancers(userRepository.topFreelancerHot()));
        topUserHotDto.setRecruiters(getRecruiters(userRepository.topRecruiterHot()));
      return  topUserHotDto;
    }
}
