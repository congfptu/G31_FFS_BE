package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.*;
import com.example.g31_ffs_be.service.UserService;
import com.example.g31_ffs_be.service.impl.FeedbackServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
//@PreAuthorize("hasAuthority('recruiter') or hasAuthority('freelancer') or hasAuthority('staff') ")
public class UserController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserService userService;
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    PostService postService;
    @Autowired
    ServiceService serviceService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    FeedbackServiceImpl feedbackService;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserServiceRepository userServiceRepository;
    @Autowired
    FeeRepository feeRepository;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    ViewProfileHistoryRepository viewProfileHistory;

    @GetMapping("/searchTransaction")
    public ResponseEntity<?> searchTransaction(
            @RequestParam(name = "userId", defaultValue = "") String userId,
            @RequestParam(name = "from", defaultValue = "1970-01-01") String from,
            @RequestParam(name = "to", defaultValue = "1970-01-01") String to,
            @RequestParam(name = "pageNo", defaultValue = "0") String pageNo) {
        try {
            int pageIndex = Integer.parseInt(pageNo);

            return new ResponseEntity<>(userService.searchTransactionHistoryByTime(from, to, userId, pageIndex, 10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/recharge-money")
    public ResponseEntity<?> rechargeMoney(@RequestHeader(name = "Authorization") String token,
                                           @RequestBody RequestPaymentDto requestPayment) {
        try {
            return new ResponseEntity<>(userService.rechargeMoney(requestPayment), HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getInfoUser")
    public ResponseEntity<?> login(@RequestHeader(name = "Authorization") String token,
                                   @RequestParam(name = "userId", defaultValue = "") String userId) {
        try {
            Account account = accountRepository.findByUserId(userId);
            String role = account.getRole().getRoleName();
            JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
            jwtAuthResponse.setUserId(account.getId());
            jwtAuthResponse.setRole(role);
            jwtAuthResponse.setAccessToken(token);
            jwtAuthResponse.setTokenType("Bearer");
            jwtAuthResponse.setEmail(account.getEmail());
            if (!role.equals("admin") && !role.equals("staff")) {
                jwtAuthResponse.setAvatar(account.getUser().getAvatar());
                jwtAuthResponse.setAccountBalance(account.getUser().getAccountBalance());
                jwtAuthResponse.setFeePostJob(feeRepository.getReferenceById(1).getPrice());
                jwtAuthResponse.setFeeApplyJob(feeRepository.getReferenceById(2).getPrice());
                jwtAuthResponse.setFeeViewProfile(feeRepository.getReferenceById(3).getPrice());
                jwtAuthResponse.setIsMemberShip(account.getUser().getIsMemberShip());
                jwtAuthResponse.setUnReadNotification(account.getUser().getUnRead());
                if (account.getUser().getServiceDto() != null) {
                    jwtAuthResponse.setCurrentServiceId(account.getUser().getServiceDto().getId());
                    jwtAuthResponse.setCurrentServiceName(account.getUser().getServiceDto().getServiceName());
                }
            }
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedBackToId(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(name = "id", defaultValue = "") String toId,
                                             @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageSize = 5;
        try {
            int pageIndex = 0;
            pageIndex = Integer.parseInt(pageNo);
            return new ResponseEntity<>(feedbackService.getFeedbackByFromUser(pageIndex, pageSize, toId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addFeedback")
    public ResponseEntity<?> insertFeedBack(@RequestHeader(name = "Authorization") String token,
                                            @RequestBody FeedbackDTO feedback
    ) {
        try {
            if (feedbackRepository.count(feedback.getFromUserId(), feedback.getJobId(), feedback.getToUserId()) > 0)
                feedbackRepository.update(feedback.getFromUserId(), feedback.getJobId(), feedback.getToUserId(), feedback.getStar(), feedback.getContent(), LocalDateTime.now());
            else
                feedbackRepository.insert(feedback.getFromUserId(), feedback.getJobId(), feedback.getToUserId(), feedback.getStar(), feedback.getContent(), LocalDateTime.now());
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllService")
    public ResponseEntity<?> getAllServiceByRoleName(@RequestParam(name = "role", defaultValue = "") String roleName) {
        try {
            return new ResponseEntity<>(serviceService.getAllServiceByRole(roleName), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/buy-service")
    public ResponseEntity<?> addService(@RequestHeader(name = "Authorization") String token,
                                        @RequestParam(name = "userId", defaultValue = "") String userId,
                                        @RequestParam(name = "serviceId", defaultValue = "") int serviceId) {
        try {
            Service service = serviceRepository.getReferenceById(serviceId);
            User user = userRepository.getReferenceById(userId);
            userRepository.insertUserService(userId, serviceId, LocalDateTime.now(), service.getPrice());
            user.setAccountBalance(user.getAccountBalance() - service.getPrice());
            user.setIsMemberShip(true);
            userRepository.save(user);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addReport")
    public ResponseEntity<?> addReport(@RequestHeader(name = "Authorization") String token,
                                       @RequestBody ReportDTO reportDTO
    ) {

        try {
            reportRepository.insert(reportDTO.getCreatedBy(),reportDTO.getTitle(),reportDTO.getContent(),LocalDateTime.now());
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestHeader(name = "Authorization") String token,
                                            @Valid @RequestBody AccountDto accountDto) {
        if (accountService.changePasswordUser(accountDto))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.OK);
    }

    @GetMapping("/skill")
    public ResponseEntity<?> getAllSkill(@RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(skillRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping("/updateAccountBalance")
    public ResponseEntity<?> updateAccountBalance(@RequestHeader(name = "Authorization") String token,
                                                  @RequestParam(name = "userId", defaultValue = "") String userId,
                                                  @RequestParam(name = "fee", defaultValue = "0") double fee
    ) {
        try {
            User user = userRepository.getReferenceById(userId);
            user.setAccountBalance(user.getAccountBalance() - fee);
            userRepository.save(user);
            ViewProfileHistory profileHistory = new ViewProfileHistory();
            profileHistory.setUser(user);
            profileHistory.setFee(fee);

            viewProfileHistory.save(profileHistory);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "userId", defaultValue = "") String userId,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex
    ) {
        try {

            return new ResponseEntity<>(userService.getTop10Notifications(userId, pageIndex, 10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

}

