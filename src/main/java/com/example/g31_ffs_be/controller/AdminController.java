package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.StaffAdminDto;
import com.example.g31_ffs_be.model.Account;
import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.User;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.impl.AccountServiceImpl;
import com.example.g31_ffs_be.service.impl.ReportServiceImpl;
import com.example.g31_ffs_be.service.impl.StaffServiceImpl;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("")
@CrossOrigin("*")
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
    @GetMapping("/admin/report/{offset}")
    public Page<Report> getReportPaging(@PathVariable int offset){
        if(offset>=0)
            return reportService.getReportByPaging(offset-1,6);
        return null;
    }
    @PostMapping("/add/staff")
    public void addStaff(@RequestBody String strJson){

            String id="LS"+RandomString.make(8);
            JSONObject json=new JSONObject(strJson);
            if (!accountService.checkIdExist(id)) {
                Account acc = new Account();
                acc.setId(id);
                acc.setEmail(json.getString("email"));
                acc.setPassword(passwordEncoder.encode(json.getString("password")));
                accountService.addAccount(acc);
                Staff s = new Staff();
                s.setId(id);
                s.setFullname(json.getString("fullname"));
                s.setAddress(json.getString("address"));
                s.setPhone(json.getString("phone"));
                s.setIsActive(Boolean.TRUE);
                staffService.addStaff(s);
            }
    }
    @PostMapping("/admin/report/add")
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
    @PutMapping("/staff/ban")
    public void banStaff(@RequestParam String id){
        staffService.banStaff(id);

    }




}
