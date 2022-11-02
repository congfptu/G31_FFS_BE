package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.model.Job;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.repository.StaffRepository;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/job")
public class    PostController {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    StaffRepository staffRepository;
    @GetMapping("/post")
    public ResponseEntity<?> getAllPostSearchByDesAndStatusPaging(@RequestHeader(name = "Authorization") String token,
                                                                  @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                                  @RequestParam(name = "status", defaultValue = "") String status,
                                                                  @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
            int pageSize = 5;

            PostDTOResponse fas = postService.getAllPostByNameAndStatusPaging(pageIndex, pageSize, keyword, "", null);
            if (fas.getTotalPages() >= pageIndex - 1 && status.equals("-1")) {

                return new ResponseEntity<>(fas, HttpStatus.OK);
            } else if (fas.getTotalPages() >= pageIndex - 1) {
                fas = postService.getAllPostByNameAndStatusPaging(pageIndex, pageSize, keyword, status, null);
                return new ResponseEntity<>(fas, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("không có dữ liệu trang này! " + e, HttpStatus.NO_CONTENT);
        }
    }
    @PutMapping("/post/update")
    public ResponseEntity<?> updateStatus(@RequestHeader(name = "Authorization") String token,
                                          @NotEmpty @RequestParam(name = "id", defaultValue = "") String id,
                                          @NotEmpty @RequestParam(name = "status", defaultValue = "") Integer status,
                                          @NotEmpty @RequestParam(name = "approveBy", defaultValue = "") String approveBy


    ) {
        try {
            if (postRepository.findById(id).isPresent()) {
                Job job = postRepository.findById(id).get();
                Staff staff = staffRepository.getReferenceById(approveBy);
                job.setApprovedBy(staff);
                job.setIsApproved(status);
                postRepository.save(job);
            }
            return new ResponseEntity<>("Cập nhật post thành công", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Cập nhật post thất bại", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/postDetail")
    public ResponseEntity<?> getPostDetail(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "id", defaultValue = "") int id) {

        try {
            return new ResponseEntity<>(postService.getPostDetail(id), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }


    }
}
