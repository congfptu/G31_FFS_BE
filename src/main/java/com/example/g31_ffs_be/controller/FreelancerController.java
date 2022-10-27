package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.repository.FreelancerRepository;
import com.example.g31_ffs_be.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/freelancer")
public class FreelancerController {
    @Autowired
    FreelancerService freelancerService;
    @GetMapping("/getProfileFreelancer")
    public ResponseEntity<?> getAllCareer(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "id", defaultValue = "") String id) {
        if(freelancerService.getFreelancerProfile(id)!=null){
            return new ResponseEntity<>(freelancerService.getFreelancerProfile(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }
}
