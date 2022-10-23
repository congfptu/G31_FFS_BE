package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.ServiceService;
import com.example.g31_ffs_be.service.impl.*;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    StaffServiceImpl staffService;
    /* @Autowired
     PasswordEncoder passwordEncoder;*/
    @Autowired
    ReportServiceImpl reportService;
    @Autowired
    StaffRepository repository;
    @Autowired
    FreelancerServiceImpl freelancerService;
    @Autowired
    RecruiterServiceImpl recruiterService;
    @Autowired
    UserServiceImpl userService;
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
    @Autowired ServiceService serviceService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired ServiceRepository serviceRepository;

    //staffController
    @GetMapping("/staff111")
    public List<StaffAdminDto> getAllStaff() {
        return staffService.getAllStaffs();
    }

    @GetMapping("/staff")
    public ResponseEntity<?> getStaffByname(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam(name = "name", defaultValue = "") String name,
                                            @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = staffRepository.getStaffByName(name, p).getTotalPages();
        if (totalPage >= pageIndex - 1) {
            List<StaffAdminDto> sads = staffService.getStaffByName(name, pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("staffs", sads);
            map.put("pageIndex", pageIndex + 1);
            map.put("totalPages", totalPage);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }

    }
    @PutMapping("/update-staff")
    public ResponseEntity<?> updateStaff(@RequestHeader(name = "Authorization") String token,@Valid @RequestBody StaffDto staffDto) {
      staffService.updateStaff(staffDto);
      return new ResponseEntity<>("Update thành công", HttpStatus.OK);
    }

    @PostMapping("/add-staff")
    public ResponseEntity<?> addStaff(@RequestHeader(name = "Authorization") String token,@Valid @RequestBody StaffDto staffDto) {
        if (accountService.checkEmailExist(staffDto.getEmail())) {
            return new ResponseEntity<>("Email đã tồn tại trên hệ thống, vui lòng thử email khác", HttpStatus.OK);
        } else {
            String id = "LS" + RandomString.make(8);
            if (!accountService.checkIdExist(id)) {

                Account acc = new Account();
                acc.setId(id);
                acc.setEmail(staffDto.getEmail());
                acc.setPassword(staffDto.getPassword());
                Staff s = new Staff();
                s.setId(id);
                s.setFullname(staffDto.getFullName());
                s.setAddress(staffDto.getAddress());
                s.setPhone(staffDto.getPhone());
                s.setIsActive(Boolean.TRUE);
                acc.setStaff(s);
                acc.setRole(roleRepository.findById(staffDto.getRole()).get());
                accountService.addAccount(acc);
            }
        }
        return new ResponseEntity<>("đăng ký thành công", HttpStatus.OK);
    }

    @PutMapping("/ban-staff")
    public ResponseEntity<?> banStaff(@RequestParam String id) {
        staffService.banStaff(id);
        return new ResponseEntity<>("Bạn đã ngừng cấp quyền cho nhân viên này", HttpStatus.OK);

    }

    //freelancerController
    @GetMapping("/freelancer")
    public ResponseEntity<?> getFreelancerFiler(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = freelancerRepository.getFreelancerByName(name, p).getTotalPages();
        if (totalPage >= pageIndex - 1) {
            List<FreelancerAdminDto> fas = freelancerService.getFreelancerByName(name, pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("freelancers", fas);
            map.put("pageIndex", pageIndex + 1);
            map.put("totalPages", totalPage);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }

    }


    @GetMapping("/detail-freelancer")
    public ResponseEntity<?> getDetailFreelancer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "id", defaultValue = "") String id) {
        FreelancerDetailDto freelancerDetailDto=freelancerService.getDetailFreelancer(id);

        return new ResponseEntity<>(freelancerDetailDto!=null?freelancerDetailDto:"Không có thông tin người dùng này", HttpStatus.OK);
    }



    @GetMapping("/type-ban")
    public ResponseEntity<?> getAllTypeBan(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "id", defaultValue = "") String id) {
        System.out.println(id);
        return new ResponseEntity<>(typeBanRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/ban-user")
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
        return new ResponseEntity<>("", HttpStatus.OK);
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

        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = recruiterRepository.getRecruiterByName(name, p).getTotalPages();
        if (totalPage >= pageIndex - 1) {
            List<RecruiterAdminDto> ras = recruiterService.getRecruiterByName(name, pageIndex, pageSize);


            Map<String, Object> map = new HashMap<>();
            map.put("recruiters", ras);
            map.put("pageIndex", pageIndex + 1);
            map.put("totalPages", totalPage);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
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
        RecruiterDetailDTO recruiterDetailDTO=recruiterService.getDetailRecruiter(id);
        return new ResponseEntity<>(recruiterDetailDTO!=null?recruiterDetailDTO:"Không có thông tin nhà tuyển dụng này", HttpStatus.OK);
    }

    //Service
    @GetMapping("/service")
    public ResponseEntity<?> getServiceByName(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "roleId", defaultValue = "3") String roleId,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;

        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        ServiceResponse serviceResponse = serviceService.getServiceByName(name,roleId,pageIndex, pageSize);
        return new ResponseEntity<>(serviceResponse, HttpStatus.OK);
    }

    @PostMapping("/add-service")
    public ResponseEntity<?> addService(@RequestHeader(name = "Authorization") String token
            , @RequestBody ServiceDto serviceDto
    ) {
        if (serviceService.checkServiceNameUnique(serviceDto.getServiceName())) {
            return new ResponseEntity<>("tên service đã tồn tại", HttpStatus.BAD_REQUEST);
        } else {
            serviceService.saveService(serviceDto);
            return new ResponseEntity<>(serviceDto, HttpStatus.CREATED);
        }
    }
    @DeleteMapping("/delete-service")
    public ResponseEntity<?> deleteService(@RequestHeader(name = "Authorization") String token,
                                        @RequestParam(name = "id", defaultValue = "") String id) {
        try{
            int serviceId=Integer.parseInt(id);
            serviceRepository.deleteById(serviceId);
            return new ResponseEntity<>("Bạn đã xóa thành công", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Không tìm thấy service cần xóa", HttpStatus.CREATED);
        }
    }

    @GetMapping("/benefits-service")
    public ResponseEntity<?> addService(@RequestHeader(name = "Authorization") String token,
                                        @RequestParam(name = "id", defaultValue = "") String id) {
        try{
            int serviceId=Integer.parseInt(id);
            return new ResponseEntity<>(serviceService.getBenefitsOfServiceByID(serviceId), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>("Không tìm thấy service cần xóa", HttpStatus.CREATED);
        }

    }

    @PutMapping("update-service")
    public ResponseEntity<?> updateService(@RequestHeader(name = "Authorization") String token
            , @RequestBody ServiceDto serviceDto
    ) {
        if (serviceService.checkServiceNameUnique(serviceDto.getServiceName())) {
            return new ResponseEntity<>("tên service đã tồn tại", HttpStatus.BAD_REQUEST);
        } else {
            serviceService.saveService(serviceDto);
            return new ResponseEntity<>(serviceDto, HttpStatus.CREATED);
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

    @PostMapping("/addReport")
    public void addReport(@RequestBody String strJson) {
        try {
            JSONObject json = new JSONObject(strJson);
            Report report = new Report();
            User u = new User();
            u.setId(json.getString("from_id"));
            report.setContent(json.getString("content"));
            report.setTitle(json.getString("title"));
            report.setFrom(u);
            report.setDateCreated(Instant.now());
            reportService.addReport(report);
        } catch (Exception e) {

        }
    }


}
