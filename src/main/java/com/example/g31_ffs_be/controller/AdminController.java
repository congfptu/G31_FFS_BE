package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.security.CustomUserDetailService;
import com.example.g31_ffs_be.service.*;
import com.example.g31_ffs_be.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin("*")

@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    StaffService staffService;

    @Autowired
    ReportService reportService;
    @Autowired
    StaffRepository repository;
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    RecruiterService recruiterService;
    @Autowired
    UserService userService;
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    BenefitRepository benefitRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    TypeBanRepository typeBanRepository;
    @Autowired
    BanRepository banRepository;
    @Autowired
    ServiceService serviceService;
    @Autowired
    private AccountService accountService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomUserDetailService customUserDetailService;

    //staffController
    @GetMapping("/staff111")
    public List<StaffAdminDto> getAllStaff() {
        return staffService.getAllStaffs();
    }


    @GetMapping("/staff")
    public ResponseEntity<?> getStaffByFilter(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex = 0;
        try {
            try{
                pageIndex = Integer.parseInt(pageNo);
            }
            catch(Exception e){

            }
            int pageSize = 5;
            APIResponse<StaffAdminDto> apiResponse = staffService.getStaffByName(name, pageIndex, pageSize);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.OK);
        }
    }

    @PutMapping("/update-staff")
    public ResponseEntity<?> updateStaff(@RequestHeader(name = "Authorization") String token, @Valid @RequestBody StaffDto staffDto) {
        staffService.updateStaff(staffDto);
        return new ResponseEntity<>("Update thành công", HttpStatus.OK);
    }

    @PostMapping("/add-staff")
    public ResponseEntity<?> addStaff(@RequestHeader(name = "Authorization") String token, @Valid @RequestBody StaffDto staffDto) {
        if (accountService.checkEmailExist(staffDto.getEmail())) {
            return new ResponseEntity<>("Email đã tồn tại trên hệ thống, vui lòng thử email khác", HttpStatus.OK);
        } else {
            String id = "LS" + RandomString.make(8);
            if (accountService.checkIdExist(id)==false) {
                Account acc = new Account();
                acc.setId(id);
                acc.setEmail(staffDto.getEmail());
                acc.setPassword(passwordEncoder.encode(staffDto.getPassword()));
                acc.setCreatedDate(LocalDateTime.now());
                Staff s = new Staff();
                s.setId(id);
                s.setFullName(staffDto.getFullName());
                s.setAddress(staffDto.getAddress());
                s.setPhone(staffDto.getPhone());
                s.setIsActive(Boolean.TRUE);
                acc.setStaff(s);
                acc.setRole(roleRepository.findByRoleName("staff"));
                accountRepository.save(acc);
            }
        }
        return new ResponseEntity<>("đăng ký thành công", HttpStatus.OK);
    }

    @PutMapping("/ban-staff")
    public ResponseEntity<?> banStaff(@RequestParam(name = "id", defaultValue = "") String id) {
        Staff s = staffRepository.findById(id).get();
        staffService.banStaff(s);
        return new ResponseEntity<>(s.getIsActive() ? "Bạn đã cấp quyền cho nhân viên này" : "Bạn đã ngừng cấp quyền cho nhân viên này", HttpStatus.OK);

    }

    //freelancerController

    @GetMapping("/freelancer")
    public ResponseEntity<?> getFreelancerFiler(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {

        try {
            int pageIndex = 0;
            try{
            pageIndex = Integer.parseInt(pageNo);
            }
            catch(Exception e){

            }
            int pageSize = 10;
            APIResponse<FreelancerAdminDto> apiResponse = freelancerService.getFreelancerByName(name, pageIndex, pageSize);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("ko co du lieu trang nay", HttpStatus.OK);
        }

    }


    @GetMapping("/detail-freelancer")
    public ResponseEntity<?> getDetailFreelancer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "id", defaultValue = "") String id) {

        FreelancerDetailDto freelancerDetailDto = freelancerService.getFreelancerInfo(id);
        return new ResponseEntity<>(freelancerDetailDto != null ? freelancerDetailDto : "Không có thông tin người dùng này", HttpStatus.OK);
    }


    @GetMapping("/type-ban")
    public ResponseEntity<?> getAllTypeBan(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "id", defaultValue = "") String id) {
        System.out.println(id);
        return new ResponseEntity<>(typeBanRepository.findAll(), HttpStatus.OK);
    }

    @PutMapping("/ban-user")
    public ResponseEntity<?> banFreelancer(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "userId", defaultValue = "") String userId,
                                           @RequestParam(name = "typeBan", defaultValue = "") String typeBan,
                                           @RequestParam(name = "bannedBy", defaultValue = "") String adminId) {
        Ban b = new Ban();
        b.setBannedBy(adminId);
        b.setUser(userId);
        b.setDate(Instant.now());
        b.setTypeBan(Integer.parseInt(typeBan));
        banRepository.save(b);
        userService.banUser(userId);
        return new ResponseEntity<>("Bạn đã khóa người dùng này!", HttpStatus.OK);
    }

    @PutMapping("/unban-user")
    public ResponseEntity<?> unBanFreelancer(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(name = "userId", defaultValue = "") String userId) {
        User u = userRepository.findById(userId).get();
        u.setIsBanned(false);
        userRepository.save(u);
        return new ResponseEntity<>("Bạn đã mở khóa người dùng này!", HttpStatus.OK);
    }

    @GetMapping("/top5-freelancer")
    public ResponseEntity<?> getTop5Freelancer(@RequestHeader(name = "Authorization") String token,
                                               @RequestParam(name = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(freelancerService.getTop5ByName(name), HttpStatus.OK);
    }

    //recruiter
    @GetMapping("/recruiter")
    public ResponseEntity<?> getRecruiterFilter(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex = 0;
        int pageSize = 5;
        try {
            try{
                pageIndex = Integer.parseInt(pageNo);
            }
            catch(Exception e){
            }
            return new ResponseEntity<>(recruiterService.getRecruiterByName(name,pageIndex,pageSize), HttpStatus.OK);
        } catch (Exception e) {
        return new ResponseEntity<>("Không có nội dung trong trang này", HttpStatus.BAD_REQUEST);
    }

    }

    @GetMapping("/top5-recruiter")
    public ResponseEntity<?> getTop5Recruiter(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name) {
        return new ResponseEntity<>(recruiterService.getTop5RecruiterByName(name), HttpStatus.OK);
    }

    @GetMapping("/detail-recruiter")
    public ResponseEntity<?> getDetailRecruiter(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "id", defaultValue = "") String id) {
        RecruiterDetailDTO recruiterDetailDTO = recruiterService.getDetailRecruiter(id);
        return new ResponseEntity<>(recruiterDetailDTO != null ? recruiterDetailDTO : "Không có thông tin nhà tuyển dụng này", HttpStatus.OK);
    }

    //Service
    @GetMapping("/service")
    public ResponseEntity<?> getServiceByName(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "roleId", defaultValue = "3") int roleId,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;

        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        ServiceResponse serviceResponse = serviceService.getServiceByName(name, roleId, pageIndex, pageSize);
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }

    @PostMapping("/add-service")
    public ResponseEntity<?> addService(@RequestHeader(name = "Authorization") String token
            , @RequestBody ServiceDto serviceDto
    ) {
        if (serviceService.checkServiceNameUnique(serviceDto.getServiceName())) {
            return new ResponseEntity<>("Tên service đã tồn tại", HttpStatus.BAD_REQUEST);
        } else {
            serviceService.saveService(serviceDto);
            return new ResponseEntity<>(serviceDto, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/delete-service")
    public ResponseEntity<?> deleteService(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "id", defaultValue = "") String id) {
        try {
            int serviceId = Integer.parseInt(id);
            serviceRepository.deleteById(serviceId);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/benefits-service")
    public ResponseEntity<?> benefitService(@RequestHeader(name = "Authorization") String token,
                                        @RequestParam(name = "id", defaultValue = "") String id) {
        try {
            int serviceId = Integer.parseInt(id);
            return new ResponseEntity<>(serviceService.getBenefitsOfServiceByID(serviceId), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Không tìm thấy service cần xóa", HttpStatus.CREATED);
        }
    }

    @PutMapping("update-service")
    public ResponseEntity<?> updateService(@RequestHeader(name = "Authorization") String token
            , @RequestBody ServiceDto serviceDto
    ) {
        try {
            serviceService.saveService(serviceDto);
            return new ResponseEntity<>("Update dịch vụ thành công!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Không thành công!", HttpStatus.CREATED);
        }
    }

    @GetMapping("/role")
    public ResponseEntity<?> getAllRoles(@RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.CREATED);
    }

    @GetMapping("/benefit")
    public ResponseEntity<?> getAllBenefits(@RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(benefitRepository.findAll(), HttpStatus.CREATED);
    }
}
