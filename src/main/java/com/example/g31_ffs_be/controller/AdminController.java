package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.dto.StaffDto;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import com.example.g31_ffs_be.service.impl.ReportServiceImpl;
import com.example.g31_ffs_be.service.impl.StaffServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.apache.tomcat.util.http.parser.Authorization;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    StaffServiceImpl staffService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ReportServiceImpl reportService;
    @Autowired
    StaffRepository repository;
    @Autowired private AccountServiceImpl accountService;

    @GetMapping("/admin/staff")
    public List<StaffAdminDto> getAllStaff(){
        return staffService.getAllStaffs();
    }

    @GetMapping("/all")
    public List<Staff> getAllStaffs(){
        return repository.findAll();
    }
    @GetMapping("/admin/staff/{offset}")
    public List<StaffAdminDto> getStaffPaging(@PathVariable int offset){
        if(offset>=0) return staffService.getStaffWithPaging(offset-1,6);
        return null;
    }
    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    @GetMapping("/admin/report/{offset}")
    public Page<Report> getReportPaging(@PathVariable int offset){
        if(offset>=0)
            return reportService.getReportByPaging(offset-1,6);
        return null;
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
                acc.setPassword(passwordEncoder.encode(staffDto.getPassword()));
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
    @PutMapping("/banStaff")
    public void banStaff(@RequestParam String id){
        staffService.banStaff(id);

    }




}
