package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.service.FreelancerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/recruiter")
public class RecruiterController {

    @Autowired
    FreelancerService freelancerService;

    @GetMapping("/findFreelancer")
    public ResponseEntity<?> getPostDetail(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "address", defaultValue = "") String address,
                                           @RequestParam(name = "skill", defaultValue = "-1") String skill,
                                           @RequestParam(name = "costOption", defaultValue = "-1") int costOption,
                                           @RequestParam(name = "subCareer", defaultValue = "-1") String subCareer,
                                           @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex) {

        try {
            return new ResponseEntity<>(freelancerService.getAllFreelancerByFilter(address, costOption, subCareer, skill, pageIndex, 5), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }
}
