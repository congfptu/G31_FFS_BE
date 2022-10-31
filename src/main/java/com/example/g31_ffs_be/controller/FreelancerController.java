package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.RegisterDto;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.FreelancerService;
import com.example.g31_ffs_be.service.PostService;
import com.example.g31_ffs_be.service.impl.FreelancerServiceImpl;
import javafx.util.converter.LocalDateStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/freelancer")
public class FreelancerController {
    @Autowired
    FreelancerServiceImpl freelancerService;
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    JobSavedRepository jobSavedRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
    @Autowired
    EducationRepository educationRepository;

    @GetMapping("/getProfileFreelancer")
    public ResponseEntity<?> getProfileFreelancer(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "id", defaultValue = "") String id) {
        try{
            if (freelancerService.getFreelancerProfile(id) != null) {
                return new ResponseEntity<>(freelancerService.getFreelancerProfile(id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/findingJob")
    public ResponseEntity<?> findJob(@RequestHeader(name = "Authorization") String token,
                                     @RequestParam(name = "area", defaultValue = "") String area,
                                     @RequestParam(name = "budget", defaultValue = "") Double budget,
                                     @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                     @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo,
                                     @RequestParam(name = "sub_career_id", defaultValue = "-1") Integer sub_career_id,
                                     @RequestParam(name = "is_top") Boolean is_top
    ) {
        try {
            int pageIndex = 0;
            try {
                pageIndex = Integer.parseInt(pageNo);
            } catch (Exception e) {

            }
            int pageSize = 5;
            if (sub_career_id != -1) {
                APIResponse fas = postService.getJobSearch(pageIndex, pageSize, area, budget, keyword, is_top, sub_career_id, null);
                if (fas.getTotalPages() >= pageIndex - 1) {
                    return new ResponseEntity<>(fas, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
                }
            } else {
                APIResponse fas = postService.getJobSearch(pageIndex, pageSize, area, budget, keyword, is_top, null);
                if (fas.getTotalPages() >= pageIndex - 1) {
                    return new ResponseEntity<>(fas, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }

    }
    @PostMapping("/addJobSaved")
    public ResponseEntity<?> addJobSaved(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "freelancer_id") Integer freelancer_id,
                                         @NotEmpty @RequestParam(name = "job_id") Integer job_id
    ) {
        try {

            jobSavedRepository.insert(job_id, freelancer_id);
            return new ResponseEntity<>("Thêm mới jobSaved thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Thêm mới jobSaved thất bại", HttpStatus.BAD_REQUEST);
        }
        }
    @PostMapping("/addJobRequest")
    public ResponseEntity<?> addJobRequest(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "freelancer_id") int freelancer_id,
                                         @NotEmpty   @RequestParam(name = "job_id") int job_id
    ) {
        try {

            jobRequestRepository.insert(job_id,freelancer_id);
            return new ResponseEntity<>("Thêm mới jobRequest thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Thêm mới jobRequest thất bại", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add-education")
    public ResponseEntity<?> addEducation(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Education education,
            @RequestParam(name = "freelancerId", defaultValue = "") String freelancerId) {

       try{
           freelancerService.addEducation(education,freelancerId);
           return new ResponseEntity<>(true, HttpStatus.OK);
       }
       catch (Exception e){
           System.out.println(e);
           return new ResponseEntity<>(false, HttpStatus.OK);
       }

    }
    @DeleteMapping("/delete-education")
    public ResponseEntity<?> deleteEducation(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "educationId", defaultValue = "") String educationId) {

        try{
            freelancerService.deleteEducation(Integer.parseInt(educationId));
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @DeleteMapping("/delete-exp")
    public ResponseEntity<?> deleteExp(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "expId", defaultValue = "") String expId) {

        try{
            freelancerService.deleteWorkExperience(Integer.parseInt(expId));
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @PutMapping("/update-education")
    public ResponseEntity<?> updateEducation(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody Education education) {

        try{
            freelancerService.updateEducation(education);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @PutMapping("/update-exp")
    public ResponseEntity<?> update(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody WorkExperience workExperience) {

        try {
            freelancerService.updateWorkExperience(workExperience);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @PostMapping("/add-exp")
    public ResponseEntity<?> addWorkExperience(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody WorkExperience workExperience,
            @RequestParam(name = "freelancerId", defaultValue = "") String freelancerId) {

        try {
            freelancerService.addWorkExperience(workExperience, freelancerId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @GetMapping("/skill")
    public ResponseEntity<?> getSkillRemain(@RequestHeader(name = "Authorization") String token, @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId) {
        System.out.println(freelancerId);
        return new ResponseEntity<>(skillRepository.getAllRemainSkills(freelancerId), HttpStatus.OK);

    }
    @GetMapping("/add-skill")
    public ResponseEntity<?> addSkill(@RequestHeader(name = "Authorization") String token,
                                      @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId,
                                      @RequestBody List<Skill> skills) {
        freelancerService.addSkill(skills, freelancerId);
        return new ResponseEntity<>(true, HttpStatus.OK);

    }
    @DeleteMapping("/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader(name = "Authorization") String token,
                                         @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId,
                                         @RequestBody Skill skill) {
        try {
            freelancerService.deleteSkill(skill, freelancerId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
    @PutMapping("/update-profile")
    public ResponseEntity<?> editInformation(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(name = "freelancerId") String freelancerId,
                                             @RequestParam(name = "field") String field,
                                             @RequestParam(name = "value") String value) {
        try {
            Freelancer f = freelancerRepository.findById(freelancerId).get();
            if (field.equals("avatar"))
                f.getUser().setAvatar(value);
            else if (field.equals("costPerHour"))
                f.setCostPerHour(Double.parseDouble(value));
            else if (field.equals("description"))
                f.setDescription(value);
            freelancerRepository.save(f);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
    @PutMapping("/update-profiles")
    public ResponseEntity<?> updateDetailProfile(@RequestHeader(name = "Authorization") String token,
                                                 @RequestBody RegisterDto registerDto) {
        try {
            freelancerService.updateProfile(registerDto);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
