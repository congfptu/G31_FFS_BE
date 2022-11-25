package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.*;
import com.example.g31_ffs_be.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
//@PreAuthorize("hasAuthority('admin')")
public class AdminController {
    @Autowired
    StaffService staffService;
    @Autowired
    FeeRepository feeRepository;

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
    @Autowired DashboadService dashboadService;
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashBoard() {
        try{
            return new ResponseEntity<>(dashboadService.getDashboardServices(), HttpStatus.OK);
        }
       catch (Exception e){
           return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
       }
    }

    //staffController

    @GetMapping("/staff")
    public ResponseEntity<?> getStaffByFilter(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex = 0;
        try {
            try {
                pageIndex = Integer.parseInt(pageNo);
            } catch (Exception e) {

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
            if (!accountService.checkIdExist(id)) {
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
                                                @RequestParam(name = "status", defaultValue = "-1") int status,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        try {
            int pageIndex = 0;
            try {
                pageIndex = Integer.parseInt(pageNo);
            } catch (Exception e) {
            }
            Boolean statusBoolean=false;
            if(status==1) statusBoolean=true;
            int pageSize = 10;
            APIResponse<FreelancerAdminDto> apiResponse = freelancerService.getFreelancerByName(name,statusBoolean,status,pageIndex, pageSize);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("ko co du lieu trang nay", HttpStatus.OK);
        }
    }


    @GetMapping("/detail-freelancer")
    public ResponseEntity<?> getDetailFreelancer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "id", defaultValue = "") String id) {
        FreelancerDetailDto freelancerDetailDto = freelancerService.getFreelancerInfo(id);
        return new ResponseEntity<>( freelancerDetailDto, HttpStatus.OK);
    }


    @GetMapping("/type-ban")
    public ResponseEntity<?> getAllTypeBan(@RequestHeader(name = "Authorization") String token) {
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
        b.setDate(LocalDateTime.now());
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
    @GetMapping("/recruiterActivated")
    public ResponseEntity<?> getRecruiterActivated(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                @RequestParam(name = "status", defaultValue = "-1") int status,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex
    ) {
        Boolean statusBoolean=false;
        if(status==1) statusBoolean=true;
        try {
            return new ResponseEntity<>(recruiterService.getRecruiterActivated(keyword,statusBoolean,status,pageIndex,10), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/recruiterNotActivated")
    public ResponseEntity<?> getRecruiterNotActivated(@RequestHeader(name = "Authorization") String token,
                                                   @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                   @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex
    ) {
        try {
            return new ResponseEntity<>(recruiterService.getRecruiterNotActivated(keyword,pageIndex,10), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/top5-recruiter")
    public ResponseEntity<?> getTop5Recruiter(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "isActive", defaultValue = "false") Boolean isActive
    ) {
        return new ResponseEntity<>(recruiterService.getTop5Recruiter(name,isActive), HttpStatus.OK);
    }



    @GetMapping("/detail-recruiter")
    public ResponseEntity<?> getDetailRecruiter(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "id", defaultValue = "") String id) {
        RecruiterDetailDTO recruiterDetailDTO = recruiterService.getDetailRecruiter(id);
        return new ResponseEntity<>(recruiterDetailDTO, HttpStatus.OK);
    }

    //Service
    @GetMapping("/service")
    public ResponseEntity<?> getServiceByName(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "roleId", defaultValue = "-1") int roleId) {
        try {
            return new ResponseEntity<>(serviceService.getAllService(roleId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

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
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update-service")
    public ResponseEntity<?> updateService(@RequestHeader(name = "Authorization") String token
                         ,  @RequestParam(name = "serviceId", defaultValue = "") int serviceId,
                                           @RequestParam(name = "price", defaultValue = "0") double price
    ) {
        try {
            Service service=serviceRepository.getReferenceById(serviceId);
            service.setPrice(price);
            serviceRepository.save(service);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/role")
    public ResponseEntity<?> getAllRoles(@RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.CREATED);
    }

    @PutMapping("/editFee")
    public ResponseEntity<?> editFee(@RequestHeader(name = "Authorization") String token,
                                     @RequestParam(name = "feeId", defaultValue = "") int feeId,
                                     @RequestParam(name = "price", defaultValue = "0") double price
    ) {
        try {
            Fee fee = feeRepository.getReferenceById(feeId);
            fee.setPrice(price);
            feeRepository.save(fee);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/benefit")
    public ResponseEntity<?> getAllBenefits(@RequestHeader(name = "Authorization") String token) {
        return new ResponseEntity<>(benefitRepository.findAll(), HttpStatus.CREATED);
    }
    @PutMapping("/approveRecruiter")
    public ResponseEntity<?> approveRecruiter(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "userId", defaultValue = "") String userId) {
        try{
            Recruiter recruiter=recruiterRepository.getReferenceById(userId);
            recruiter.setIsActive(true);
            recruiterRepository.save(recruiter);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(false, HttpStatus.CREATED);
        }


    }
}
