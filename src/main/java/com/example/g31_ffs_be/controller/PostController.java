package com.example.g31_ffs_be.controller;


import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.model.Job;

import com.example.g31_ffs_be.model.Staff;

import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/job")
//@PreAuthorize("hasAuthority('recruiter') or hasAuthority('freelancer') or hasAuthority('staff') ")
public class  PostController {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    StaffRepository staffRepository;
    @GetMapping("/post")
    public ResponseEntity<?> getAllPostByAdmin(@RequestHeader(name = "Authorization") String token,
                                                                  @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                                  @RequestParam(name = "status", defaultValue = "-1") int status,
                                                                  @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex) {

        try {
                return new ResponseEntity<>(postService.getAllPostByAdmin(pageIndex, 10, keyword, status), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("không có dữ liệu trang này! " + e, HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateStatus(@RequestHeader(name = "Authorization") String token,
                                          @NotEmpty @RequestParam(name = "jobId", defaultValue = "") int jobId,
                                          @NotEmpty @RequestParam(name = "status", defaultValue = "") int status,
                                          @NotEmpty @RequestParam(name = "staffId", defaultValue = "") String staffId


    ) {
        try {
           Job job=postRepository.getReferenceById(jobId);
           job.setIsApproved(status);
           job.setApprovedBy(new Staff(staffId));
           postRepository.save(job);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/postDetail")
    public ResponseEntity<?> getPostDetail(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "freelancerId", defaultValue = "") String freelancerId,
                                           @RequestParam(name = "id", defaultValue = "") int id) {

        try {
            return new ResponseEntity<>(postService.getPostDetail(freelancerId,id), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
