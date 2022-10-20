package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.RecruiterAdminDto;
import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.dto.StaffDto;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
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
@Autowired FreelancerRepository freelancerRepository;
@Autowired
    RecruiterRepository recruiterRepository;
@Autowired StaffRepository staffRepository;
@Autowired
    TypeBanRepository typeBanRepository;
@Autowired
    BanRepository banRepository;
    @Autowired private AccountServiceImpl accountService;
    //staffController
    @GetMapping("/staff111")
    public List<StaffAdminDto> getAllStaff(){
        return staffService.getAllStaffs();
    }

    @GetMapping("/staff")
    public ResponseEntity<?>getStaffByname( @RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
    ) {
        int pageIndex=0;
        try{
            pageIndex = Integer.parseInt(pageNo);}
        catch (Exception e){

        }
        int pageSize = 5;
        Pageable p=PageRequest.of(pageIndex,pageSize);
        int totalPage= staffRepository.getStaffByName(name,p).getTotalPages();
        if(totalPage>=pageIndex-1)
        {
            List<StaffAdminDto> sads=staffService.getStaffByName(name,pageIndex,pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("staffs", sads);
            map.put("pageIndex", pageIndex+1);
            map.put("totalPages", totalPage);
            return new ResponseEntity<>(map, HttpStatus.OK);}
        else{
            return new ResponseEntity<>("không có dữ liệu trang này!",HttpStatus.NO_CONTENT);
        }

    }
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    @PostMapping("/addStaff")
    public ResponseEntity<?> addStaff(@RequestHeader(name = "Authorization") String token, @Valid @RequestBody StaffDto staffDto){
        String[] parts = token.split("\\.");
        System.out.println(decode(parts[1]));
        System.out.println();
            String id="LS"+RandomString.make(8);
        System.out.println();
       /* JSONObject json=new JSONObject(strJson).getJSONObject("params").getJSONObject("staff");*/
            if (!accountService.checkIdExist(id)) {
                Account acc = new Account();
                acc.setId(id);
                acc.setEmail(staffDto.getEmail());
               /* acc.setPassword(passwordEncoder.encode(staffDto.getPassword()));*/
                accountService.addAccount(acc);
                Staff s = new Staff();
                s.setId(id);
                s.setFullname(staffDto.getFullname());
                s.setAddress(staffDto.getAddress()  );
                s.setPhone(staffDto.getPhone());
                s.setIsActive(Boolean.TRUE);
                staffService.addStaff(s);
            }
           return new ResponseEntity<>("đăng ký thành công",HttpStatus.OK);
    }
    @PutMapping("/banStaff")
    public void banStaff(@RequestParam String id){
        staffService.banStaff(id);

    }
    //freelancerController
    @GetMapping("/freelancer")
    public ResponseEntity<?>getFreelancerFiler( @RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
                                                ) {
        int pageIndex=0;
        try{
        pageIndex = Integer.parseInt(pageNo);}
        catch (Exception e){

        }
        int pageSize = 5;
        Pageable p=PageRequest.of(pageIndex,pageSize);
        int totalPage= freelancerRepository.getFreelancerByName(name,p).getTotalPages();
        if(totalPage>=pageIndex-1)
        {
        List<FreelancerAdminDto> fas=freelancerService.getFreelancerByName(name,pageIndex,pageSize);


        Map<String, Object> map = new HashMap<>();
        map.put("freelancers", fas);
        map.put("pageIndex", pageIndex+1);
        map.put("totalPages", totalPage);
        return new ResponseEntity<>(map, HttpStatus.OK);}
        else{
            return new ResponseEntity<>("không có dữ liệu trang này!",HttpStatus.NO_CONTENT);
        }

    }
    @GetMapping("/detail-freelancer")
    public ResponseEntity<?>getDetailFreelancer(@RequestHeader(name = "Authorization") String token,
                                               @RequestParam(name = "id", defaultValue = "") String id){
        System.out.println(id);
        return new ResponseEntity<>(freelancerService.getDetailFreelancer(id), HttpStatus.OK);
    }
    @GetMapping("/detail-recruiter")
    public ResponseEntity<?>getDetailRecruiter(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "id", defaultValue = "") String id){
        System.out.println(id);
        return new ResponseEntity<>(recruiterService.getDetailRecruiter(id), HttpStatus.OK);
    }
    @GetMapping("/type-ban")
    public ResponseEntity<?>getAllTypeBan(@RequestHeader(name = "Authorization") String token,
                                               @RequestParam(name = "id", defaultValue = "") String id){
        System.out.println(id);
        return new ResponseEntity<>(typeBanRepository.findAll(), HttpStatus.OK);
    }
    @PostMapping("/ban-freelancer")
    public ResponseEntity<?>banFreelancer(@RequestHeader(name = "Authorization") String token,
                                               @RequestParam(name = "userId", defaultValue = "") String userId,
                                               @RequestParam(name = "typeBan", defaultValue = "") String typeBan,
                                               @RequestParam(name = "bannedBy", defaultValue = "") String adminId){
        Ban b=new Ban();
        b.setBannedBy(adminId);
        b.setUser(userId);
        b.setDate(Instant.now());
        b.setTypeBan(Integer.parseInt(typeBan));
        banRepository.save(b);
        userService.banUser(userId);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
//recruiter
@GetMapping("/recruiter")
public ResponseEntity<?>getRecruiterFilter( @RequestHeader(name = "Authorization") String token,
                                            @RequestParam(name = "name", defaultValue = "") String name,
                                            @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo
) {
    int pageIndex=0;

    try{
        pageIndex = Integer.parseInt(pageNo);}
    catch (Exception e){

    }
    int pageSize = 5;
    Pageable p=PageRequest.of(pageIndex,pageSize);
    int totalPage= recruiterRepository.getRecruiterByName(name,p).getTotalPages();
    if(totalPage>=pageIndex-1){
    List<RecruiterAdminDto> ras=recruiterService.getRecruiterByName(name,pageIndex,pageSize);



    Map<String, Object> map = new HashMap<>();
    map.put("recruiters", ras);
    map.put("pageIndex", pageIndex+1);
    map.put("totalPages", totalPage);
    return new ResponseEntity<>(map, HttpStatus.OK);}
    else{
        return new ResponseEntity<>("Không có nội dung trong trang này", HttpStatus.BAD_REQUEST);
    }

}
    @GetMapping("/admin/report/{offset}")
    public Page<Report> getReportPaging(@PathVariable int offset){
        if(offset>=0)
            return reportService.getReportByPaging(offset-1,6);
        return null;
    }
    @PostMapping("/addReport")
    public void addReport(@RequestBody String strJson){
        try {
            JSONObject json = new JSONObject(strJson);
            Report report=new Report();
            User u=new User();
            u.setId(json.getString("from_id"));
            report.setContent(json.getString("content"));
            report.setTitle(json.getString("title"));
            report.setFrom(u);
            report.setDateCreated(Instant.now());
            reportService.addReport(report);
        }
        catch(Exception e) {

        }
    }



}
