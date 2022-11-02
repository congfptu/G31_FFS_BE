package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.security.JwtTokenProvider;
import com.example.g31_ffs_be.service.FeedbackService;
import com.example.g31_ffs_be.service.PostService;
import com.example.g31_ffs_be.service.ServiceService;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import com.example.g31_ffs_be.service.impl.FeedbackServiceImpl;
import com.example.g31_ffs_be.service.impl.FreelancerServiceImpl;
import com.example.g31_ffs_be.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    FreelancerServiceImpl freelancerService;
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
    @Autowired PaymentRepository paymentRepository;
    @Autowired
    FeedbackServiceImpl feedbackService;
    @Autowired FeedbackRepository feedbackRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
    @Autowired ServiceRepository serviceRepository;
    @Autowired UserServiceRepository userServiceRepository;
    @GetMapping("/transaction-history")
    public ResponseEntity<?> transactionHistory(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "userId", defaultValue = "") String userId,
                                                @RequestParam(name = "pageNo", defaultValue = "0") String pageNo ) {
        try {
            int pageIndex=Integer.parseInt(pageNo);
            return new ResponseEntity<>(userService.getTransactionHistoryById(userId,pageIndex,10), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/searchTransaction")
    public ResponseEntity<?> searchTransaction(
            @RequestParam(name = "userId", defaultValue = "") String userId ,
            @RequestParam(name = "from", defaultValue = "") Date from,
            @RequestParam(name = "to", defaultValue = "") Date to,
            @RequestParam(name = "pageNo", defaultValue = "0") String pageNo)
    {
        try {
            int pageIndex=Integer.parseInt(pageNo);
            LocalDateTime fromDate= from.toLocalDate().atTime(0,0);
            LocalDateTime toDate=to.toLocalDate().atTime(23,59);
            return new ResponseEntity<>(userService.searchTransactionHistoryByTime(fromDate,toDate,userId,pageIndex,10), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
    @PutMapping("/recharge-money")
    public ResponseEntity<?> rechargeMoney(@RequestHeader(name = "Authorization") String token,
                                           @RequestBody RequestPaymentDto requestPayment) {
        try {
                return new ResponseEntity<>( userService.rechargeMoney(requestPayment), HttpStatus.OK);
        }
        catch (Exception e){

            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getInfoUser")
    public ResponseEntity<?> login(@RequestHeader(name = "Authorization") String token,
                                   @RequestParam(name = "userId", defaultValue = "") String userId) {
        try {
            Account account = accountRepository.findByUserId(userId);
            JWTAuthResponse jwtAuthResponse=new JWTAuthResponse();
            jwtAuthResponse.setUserId(account.getId());
            jwtAuthResponse.setRole(account.getRole().getRoleName());
            jwtAuthResponse.setAccountBalance(account.getUser().getAccountBalance());
            jwtAuthResponse.setAvatar(account.getUser().getAvatar());
            jwtAuthResponse.setIsMemberShip(account.getUser().getIsMemberShip());
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Không tìm thấy user!", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/feedback")
    public ResponseEntity<?> getFeedBackToId(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "id", defaultValue = "") String toId,
                                               @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageSize = 5;
        try {
            int pageIndex=0;
            pageIndex =Integer.parseInt(pageNo);
            return new ResponseEntity<>(feedbackService.getFeedbackByFromUser(pageIndex,pageSize,toId), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping ("/addFeedback")
    public ResponseEntity<?> insertFeedBack(@RequestHeader(name = "Authorization") String token,
                                            @RequestBody FeedbackDTO feedback
                                            ) {
        try {
            feedbackRepository.insert(feedback.getFromUserId(),feedback.getToUserId(),feedback.getStar(),feedback.getContent(),Instant.now());
            return new ResponseEntity<>("Create feedback successfully", HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("Failed to create feedback", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping ("/getAllService")
    public ResponseEntity<?> insertFeedBack( @RequestParam(name = "role", defaultValue = "") String roleName) {
        try {
            return new ResponseEntity<>( serviceService.getAllService(roleName), HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping ("/buy-service")
    public ResponseEntity<?> addService( @RequestHeader(name = "Authorization") String token,
                                         @RequestParam(name = "userId", defaultValue = "") String userId,
                                         @RequestParam(name = "serviceId", defaultValue = "") int serviceId)
    {
        try {
            Service service=serviceRepository.getReferenceById(serviceId);
            User user=userRepository.getReferenceById(userId);
            userRepository.insertUserService(userId,serviceId,LocalDateTime.now(),service.getPrice());
            user.setAccountBalance(user.getAccountBalance()-service.getPrice());
            user.setIsMemberShip(true);
            userRepository.save(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}

