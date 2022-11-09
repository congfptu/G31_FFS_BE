package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.AccountRepository;
import com.example.g31_ffs_be.repository.CareerRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    UserServiceImpl userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    CareerService careerService;
    @Autowired
    CareerRepository careerRepository;


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
        Account acc=accountRepository.findByEmail(email);
        if(acc==null)
            return new ResponseEntity<>("Email chưa được đăng kí trong hệ thống!", HttpStatus.OK);
        accountService.forgotPassword(acc);
        return new ResponseEntity<>("Chúng tôi đã gửi link thay đổi mật khẩu của bạn ở email, vui lòng click và đổi mật khẩu!", HttpStatus.OK);
    }
    @GetMapping("/reset/verify-token")
    public ResponseEntity<?> verifyResetPasswordToken(@RequestParam(name = "resetPasswordToken", defaultValue = "") String resetPasswordToken) {
        Account acc=accountRepository.findByResetPasswordToken(resetPasswordToken);
        User u=acc.getUser();
        Instant timeReset=u.getResetPasswordTime();
        if(Instant.now().minus(2, ChronoUnit.MINUTES).compareTo(timeReset)>=0)
            return new ResponseEntity<>("Token quá hạn hoặc không hợp lệ!", HttpStatus.OK);
        else{
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
            /*   orElseThrow(() -> new UsernameNotFoundException("Không tìm được người dùng có số điện thoại là: " + loginDTO.getPhone()));*/
            if(account==null){
                return new ResponseEntity<>("Email không đúng!", HttpStatus.BAD_REQUEST);
            }
            if(!account.getUser().getIsBanned()){
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(), loginDTO.getPassword()
                ));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = tokenProvider.generateToken(authentication);
                JWTAuthResponse jwtAuthResponse=new JWTAuthResponse();
                jwtAuthResponse.setUserId(account.getId());
                jwtAuthResponse.setRole(account.getRole().getRoleName());
                jwtAuthResponse.setAccessToken(token);
                jwtAuthResponse.setTokenType("Bearer");
                jwtAuthResponse.setAvatar(account.getUser().getAvatar());
                jwtAuthResponse.setAccountBalance(account.getUser().getAccountBalance());
                jwtAuthResponse.setEmail(account.getEmail());
                jwtAuthResponse.setIsMemberShip(account.getUser().getIsMemberShip());

                jwtAuthResponse.setAvatar(account.getUser().getAvatar());
                return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(" Người dùng đã bị chặn !", HttpStatus.BAD_REQUEST);
            }
        }catch (AuthenticationException e){
            return new ResponseEntity<>("Email hoặc mật khẩu không đúng!", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getCareerTitle")
    public ResponseEntity<?> getAllCareerTitle() {
        if(careerService.getCareerTitle().size()!=0){
            List<CareerTitleDTO> list=careerService.getCareerTitle();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }

}
