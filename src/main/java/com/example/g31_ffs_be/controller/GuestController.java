package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.security.JwtTokenProvider;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.DashboadService;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import com.example.g31_ffs_be.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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
    ServiceRepository serviceRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    DashboadService dashboadService;
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
            return new ResponseEntity<>("Email ???? t???n t???i trong h??? th???ng!", HttpStatus.BAD_REQUEST);
        }
        accountService.createAccount(registerDto);
        if(registerDto.getRole().equals("recruiter"))
            return new ResponseEntity<>("????ng k?? th??nh C??ng!Vui l??ng ch??? ch??ng t??i ki???m duy???t!", HttpStatus.OK);
        return new ResponseEntity<>("????ng k?? th??nh C??ng! Ch??ng t??i ???? g???i email x??c th???c t???i email c???a b???n.Vui l??ng x??c th???c t??i kho???n c???a b???n!", HttpStatus.OK);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam(name = "email", defaultValue = "") String email) {
        Account acc = accountRepository.findByEmail(email);
        if (acc == null)
            return new ResponseEntity<>("Email ch??a ???????c ????ng k?? trong h??? th???ng!", HttpStatus.BAD_REQUEST);
        accountService.forgotPassword(acc);
        return new ResponseEntity<>("Ch??ng t??i ???? g???i link thay ?????i m???t kh???u c???a b???n ??? email, vui l??ng click v?? ?????i m???t kh???u!", HttpStatus.OK);
    }

    @GetMapping("/reset/verify-token")
    public ResponseEntity<?> verifyResetPasswordToken(@RequestParam(name = "resetPasswordToken", defaultValue = "") String resetPasswordToken) {
        Account acc = accountRepository.findByResetPasswordToken(resetPasswordToken);
        User u = acc.getUser();
        Instant timeReset = u.getResetPasswordTime();
        if (Instant.now().minus(2, ChronoUnit.MINUTES).compareTo(timeReset) >= 0)
            return new ResponseEntity<>("Token qu?? h???n ho???c kh??ng h???p l???!", HttpStatus.OK);
        else {
            return new ResponseEntity<>(acc.getEmail(), HttpStatus.OK);
        }

    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody AccountDto accountDto) {
        accountService.changePassword(accountDto);
        return new ResponseEntity<>("M???t kh???u ???????c thay ?????i th??nh c??ng! M???i b???n ????ng nh???p v??o h??? th???ng", HttpStatus.OK);
    }

    @PutMapping("/verify-account")
    public ResponseEntity<?> singUpUser(
            @RequestParam(name = "verifyCode", defaultValue = "") String verificationCode) {
        if (userService.verify(verificationCode))
            return new ResponseEntity<>("X??c th???c t??i kho???n th??nh c??ng! B???n c?? th??? ????ng nh???p ngay b??y gi???!", HttpStatus.OK);
        else
            return new ResponseEntity<>("T??i kho???n c???a b???n ???? ???????c x??c th???c ho???c verification code kh??ng h???p l???!", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            Account account = accountRepository.findByEmail(loginDTO.getEmail());
            if (account == null) {
                return new ResponseEntity<>("Email ho???c m???t kh???u kh??ng ????ng!", HttpStatus.BAD_REQUEST);
            }
            String role = account.getRole().getRoleName();
            if (role.equals("recruiter") && !account.getUser().getRecruiter().getIsActive())
                return new ResponseEntity<>("T??i kho???n c???a b???n  ??ang ???????c ki???m duy???t!", HttpStatus.BAD_REQUEST);
            if (role.equals("staff") && !account.getStaff().getIsActive())
                return new ResponseEntity<>("B???n kh??ng c??n quy???n truy c???p v??o t??i kho???n c??ng ty!", HttpStatus.BAD_REQUEST);
            int minuteBanRemain=0;

            if(!role.equals("admin")&&!role.equals("staff")&&account.getUser().getIsBanned()) {
                try {
                    minuteBanRemain = userRepository.countTimeBanRemain(account.getId());
                } catch (Exception e) {
                    return new ResponseEntity<>("Vui l??ng x??c th???c t??i kho???n ho???c ch??? ki???m duy???t tr?????c khi ????ng nh???p!", HttpStatus.BAD_REQUEST);
                }
            }
            if (role.equals("admin") || role.equals("staff") || !account.getUser().getIsBanned()||minuteBanRemain <= 0) {

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
                    jwtAuthResponse.setFeePushTop(feeRepository.getReferenceById(4).getPrice());
                    jwtAuthResponse.setIsMemberShip(account.getUser().getIsMemberShip());
                    jwtAuthResponse.setUnReadNotification(account.getUser().getUnRead());
                    ServiceDto serviceDto=account.getUser().getServiceDto();
                    if (serviceDto!= null) {
                        jwtAuthResponse.setCurrentServiceId(serviceDto.getId());
                        jwtAuthResponse.setCurrentServiceName(serviceDto.getServiceName());
                        jwtAuthResponse.setDurationRemain(serviceDto.getTimeRemain());
                    }
                    if (minuteBanRemain < 0 && account.getUser().getIsBanned()) {
                        account.getUser().setIsBanned(false);
                        accountRepository.save(account);
                    }
                }

                return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
            } else {

                Duration d = Duration.ofMinutes(minuteBanRemain);
                return new ResponseEntity<>("B???n ???? b??? ch???n, h??y quay l???i sau " + d.toDays() + "ng??y " + d.toHours() % 24 + "gi??? " + d.toMinutes() % 60 + "ph??t", HttpStatus.BAD_REQUEST);
            }
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Email ho???c m???t kh???u kh??ng ????ng!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getCareerTitle")
    public ResponseEntity<?> getAllCareerTitle() {
        if (careerService.getCareerTitle().size() != 0) {
            List<CareerTitleDTO> list = careerService.getCareerTitle();
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("kh??ng c?? d??? li???u. c?? th??? server ch???t!", HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testss() {
        /*List<String> label = new ArrayList<>();
        YearMonth thisMonth  = YearMonth.now();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        label.add(thisMonth.format(monthYearFormatter));
        label.add(thisMonth.minusMonths(1).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(2).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(3).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(4).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(5).format(monthYearFormatter));
        label.add(thisMonth.minusMonths(6).format(monthYearFormatter));
     List<Object[]> obj=userRepository.countFreelancer();
    for(Object[] o:obj)
        return new ResponseEntity<>(o[1], HttpStatus.OK);*/
       /* List<Integer> arrayIntegers = new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0));
        return new ResponseEntity<>(dashboadService.getDashboardServices(), HttpStatus.OK);*/
        return new ResponseEntity<>(userService.getUserHotDto(), HttpStatus.OK);
    }

}
