package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.FeeRepository;
import com.example.g31_ffs_be.repository.UserRepository;
import com.example.g31_ffs_be.security.JwtTokenProvider;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
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
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
public class GuestController {
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FeeRepository feeRepository;
    @Autowired
    UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    CareerService careerService;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    UserRepository userRepository;


    @PostMapping("/sign-up")
    public ResponseEntity<?> singUpUser(@Valid @RequestBody RegisterDto registerDto) {
        if (accountService.checkEmailExist(registerDto.getEmail())) {
            return new ResponseEntity<>("Email đã tồn tại trong hệ thống!", HttpStatus.BAD_REQUEST);
        }
        accountService.createAccount(registerDto);
        return new ResponseEntity<>("Đăng kí thành Công! Chúng tôi đã gửi email xác thực tới email của bạn.Vui lòng xác thực tài khoản của bạn!", HttpStatus.OK);

    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "email", defaultValue = "") String email) {
        Account acc = accountRepository.findByEmail(email);
        if (acc == null)
            return new ResponseEntity<>("Email chưa được đăng kí trong hệ thống!", HttpStatus.OK);
        accountService.forgotPassword(acc);
        return new ResponseEntity<>("Chúng tôi đã gửi link thay đổi mật khẩu của bạn ở email, vui lòng click và đổi mật khẩu!", HttpStatus.OK);
    }

    @GetMapping("/reset/verify-token")
    public ResponseEntity<?> verifyResetPasswordToken(@RequestParam(name = "resetPasswordToken", defaultValue = "") String resetPasswordToken) {
        Account acc = accountRepository.findByResetPasswordToken(resetPasswordToken);
        User u = acc.getUser();
        Instant timeReset = u.getResetPasswordTime();
        if (Instant.now().minus(2, ChronoUnit.MINUTES).compareTo(timeReset) >= 0)
            return new ResponseEntity<>("Token quá hạn hoặc không hợp lệ!", HttpStatus.OK);
        else {
            return new ResponseEntity<>(acc.getEmail(), HttpStatus.OK);
        }

    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDto accountDto) {
        accountService.changePassword(accountDto);
        return new ResponseEntity<>("Mật khẩu được thay đổi thành công! Mời bạn đăng nhập vào hệ thống", HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<?> singUpUser(
            @RequestParam(name = "verifyCode", defaultValue = "") String verificationCode) {
        if (userService.verify(verificationCode))
            return new ResponseEntity<>("Xác thực tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ!", HttpStatus.OK);
        else
            return new ResponseEntity<>("Tài khoản của bạn đã được xác thực hoặc verification code không hợp lệ!", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Account account = accountRepository.findByEmail(loginDTO.getEmail());
            if (account == null) {
                return new ResponseEntity<>("Email hoặc mật khẩu không đúng!", HttpStatus.BAD_REQUEST);
            }
            String role = account.getRole().getRoleName();
            if (role.equals("recruiter") && !account.getUser().getRecruiter().getIsActive())
                return new ResponseEntity<>("Tài khoản của bạn  đang được kiểm duyệt!", HttpStatus.BAD_REQUEST);
            if (role.equals("staff") && !account.getStaff().getIsActive())
                return new ResponseEntity<>("Bạn không còn quyền truy cập vào tài khoản công ty!", HttpStatus.BAD_REQUEST);
            int minuteBanRemain=0;

            if(!role.equals("admin")&&!role.equals("staff")&&account.getUser().getIsBanned())
                try {
                    minuteBanRemain = userRepository.countTimeBanRemain(account.getId());
                }
            catch (Exception e) {
                minuteBanRemain = 0;
            }
            if (role.equals("admin") || role.equals("staff") || account.getUser().getIsBanned()||minuteBanRemain <= 0) {

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(), loginDTO.getPassword()
                ));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = tokenProvider.generateToken(authentication);
                JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
                jwtAuthResponse.setUserId(account.getId());
                jwtAuthResponse.setRole(role);
                jwtAuthResponse.setAccessToken(token);
                jwtAuthResponse.setTokenType("Bearer");
                jwtAuthResponse.setEmail(account.getEmail());
                if (!role.equals("admin") && (!role.equals("staff"))) {
                    jwtAuthResponse.setAvatar(account.getUser().getAvatar());
                    jwtAuthResponse.setAccountBalance(account.getUser().getAccountBalance());
                    jwtAuthResponse.setFeePostJob(feeRepository.getReferenceById(1).getPrice());
                    jwtAuthResponse.setFeeApplyJob(feeRepository.getReferenceById(2).getPrice());
                    jwtAuthResponse.setFeeViewProfile(feeRepository.getReferenceById(3).getPrice());
                    jwtAuthResponse.setIsMemberShip(account.getUser().getIsMemberShip());
                    jwtAuthResponse.setUnReadNotification(account.getUser().getUnRead());
                    if (minuteBanRemain < 0 && account.getUser().getIsBanned()) {
                        account.getUser().setIsBanned(false);
                        accountRepository.save(account);
                    }
                }

                return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
            } else {

                Duration d = Duration.ofMinutes(minuteBanRemain);
                return new ResponseEntity<>("Bạn đã bị chặn, hãy quay lại sau " + d.toDays() + "ngày " + d.toHours() % 24 + "giờ " + d.toMinutes() % 60 + "phút", HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Email hoặc mật khẩu không đúng!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCareerTitle")
    public ResponseEntity<?> getAllCareerTitle() {
        if (careerService.getCareerTitle().size() != 0) {
            List<CareerTitleDTO> list = careerService.getCareerTitle();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testss() {
        List<String> a = new ArrayList<>();
        a.add("cong");
        a.add("cong");
        a.add("cong");
        a.add("cong");
        a.add("cong");

        return new ResponseEntity<>(a, HttpStatus.OK);
    }

}
