package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.JobSaved;
import com.example.g31_ffs_be.repository.FreelancerRepository;
import com.example.g31_ffs_be.repository.JobRequestRepository;
import com.example.g31_ffs_be.repository.JobSavedRepository;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.service.FreelancerService;
import com.example.g31_ffs_be.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/freelancer")
public class FreelancerController {
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    JobSavedRepository jobSavedRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
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
    @GetMapping("/findingJob")
    public ResponseEntity<?> findJob(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "area", defaultValue = "") String area,
                                     @RequestParam(name = "budget", defaultValue = "") String budget,
                                     @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo,
                                     @RequestParam(name = "sub_career_id", defaultValue = "-1") String sub_career_id
                                     ) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        if(!sub_career_id.equals("-1")){
            int totalPage = postRepository.getJobSearch(area, budget,sub_career_id,p).getTotalPages();
            if (totalPage >= pageIndex - 1) {
                APIResponse fas = postService.getJobSearch(pageIndex, pageSize,area,budget,sub_career_id,null);

                return new ResponseEntity<>(fas, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
            }
        }else {
            System.out.println("fsdfsd");
            int totalPage = postRepository.getJobSearch(area, budget,p).getTotalPages();
            if (totalPage >= pageIndex - 1) {
                APIResponse fas = postService.getJobSearch(pageIndex, pageSize,area,budget,null);

                return new ResponseEntity<>(fas, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
            }
        }

    }
    @PostMapping("/addJobSaved")
    public ResponseEntity<?> addJobSaved(@RequestHeader(name = "Authorization") String token,
                                        @NotEmpty @RequestParam(name = "freelancer_id") String freelancer_id,
                                         @NotEmpty   @RequestParam(name = "job_id") String job_id
                                         ) {
        try {

            jobSavedRepository.insert(job_id,freelancer_id);
                return new ResponseEntity<>("Thêm mới jobSaved thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Thêm mới jobSaved thất bại", HttpStatus.BAD_REQUEST);
        }
        }
    @PostMapping("/addJobRequest")
    public ResponseEntity<?> addJobRequest(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "freelancer_id") String freelancer_id,
                                         @NotEmpty   @RequestParam(name = "job_id") String job_id
    ) {
        try {

            jobRequestRepository.insert(job_id,freelancer_id);
            return new ResponseEntity<>("Thêm mới jobRequest thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Thêm mới jobRequest thất bại", HttpStatus.BAD_REQUEST);
        }
    }
}
