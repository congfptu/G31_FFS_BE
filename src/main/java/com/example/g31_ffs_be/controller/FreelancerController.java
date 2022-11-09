package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.FreelancerProfileDTO;
import com.example.g31_ffs_be.dto.RecruiterDetailDTO;
import com.example.g31_ffs_be.dto.RegisterDto;
import com.example.g31_ffs_be.model.*;
import com.example.g31_ffs_be.repository.*;
import com.example.g31_ffs_be.service.FreelancerService;
import com.example.g31_ffs_be.service.PostService;
import com.example.g31_ffs_be.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/freelancer")
@PreAuthorize("hasAuthority('recruiter') or hasAuthority('freelancer') or hasAuthority('staff') ")
public class FreelancerController {
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    RecruiterService recruiterService;
    @Autowired
    FreelancerRepository freelancerRepository;
    @Autowired AccountRepository accountRepository;
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
        FreelancerProfileDTO f=freelancerService.getFreelancerProfile(id);
        try{
            if (f != null) {
                return new ResponseEntity<>(f, HttpStatus.OK);
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
                                     @RequestParam(name = "userId", defaultValue = "") String userId,
                                     @RequestParam(name = "area", defaultValue = "") String area,
                                     @RequestParam(name = "paymentType", defaultValue = "-1") int paymentType,
                                     @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                     @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
                                     @RequestParam(name = "sub_career_id", defaultValue = "-1") int sub_career_id
    ) {
        try {
            User user=userRepository.findByUserId(userId);
            return new ResponseEntity<>(postService.getJobSearch(pageIndex,10,area,keyword,paymentType,sub_career_id,user.getIsMemberShip()), HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }

    }
    @PostMapping("/addJobSaved")
    public ResponseEntity<?> addJobSaved(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "freelancerId") String freelancer_id,
                                         @NotEmpty @RequestParam(name = "jobId") Integer job_id
    ) {
        try {

            if( jobSavedRepository.getJob(job_id,freelancer_id)!=null){
                 jobSavedRepository.delete(job_id,freelancer_id);
                return new ResponseEntity<>("Xóa lưu job", HttpStatus.CREATED);
            }
            else{
            jobSavedRepository.insert(job_id, freelancer_id);
                return new ResponseEntity<>("Lưu job", HttpStatus.CREATED);
            }



        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Có lỗi xảy ra, vui lòng thử lại", HttpStatus.BAD_REQUEST);
        }
        }
    @GetMapping("/getAllJobSaved")
    public ResponseEntity<?> getAllJobSaved(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "freelancerId") String freelancer_id,
                                            @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex
    ) {
        System.out.println("fsdd");
        try {
                return new ResponseEntity<>(freelancerService.getJobSaved(freelancer_id,pageIndex,10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Có lỗi xảy ra, vui lòng thử lại", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getAllJobRequest")
    public ResponseEntity<?> getAllJobRequest(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "freelancerId", defaultValue = "") String freelancerId,
                                              @RequestParam(name = "status", defaultValue = "-1") int status,
                                              @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex
    ) {
        try {
            return new ResponseEntity<>(freelancerService.getJobRequest(freelancerId,status,pageIndex,10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Có lỗi xảy ra, vui lòng thử lại", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/addJobRequest")
    public ResponseEntity<?> addJobRequest(@RequestHeader(name = "Authorization") String token,
                                         @NotEmpty @RequestParam(name = "freelancerId") String freelancer_id,
                                         @NotEmpty   @RequestParam(name = "jobId") int job_id
    ) {
        try {
            User user=userRepository.getReferenceById(freelancer_id);
            if(user.getIsMemberShip())
            jobRequestRepository.insert(job_id,freelancer_id,2, LocalDateTime.now(),0);
            else
                jobRequestRepository.insert(job_id,freelancer_id,2, LocalDateTime.now(),0.5);
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
    public ResponseEntity<?> getSkillRemain(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId,
                                            @RequestParam(name = "skill", defaultValue = "") String skill) {
        try{
            return new ResponseEntity<>(skillRepository.getAllRemainSkills(freelancerId,skill), HttpStatus.OK);
        }
        catch(Exception e){

            return new ResponseEntity<>(false, HttpStatus.OK);
        }

    }
    @PostMapping("/add-skill")
    public ResponseEntity<?> addSkill(@RequestHeader(name = "Authorization") String token,
                                      @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId,
                                      @RequestBody List<Skill> skills) {
        freelancerService.addSkill(skills, freelancerId);
        return new ResponseEntity<>(true, HttpStatus.OK);

    }
    @DeleteMapping("/delete-skill")
    public ResponseEntity<?> deleteSkill(@RequestHeader(name = "Authorization") String token,
                                         @RequestParam(name = "freelancerId", defaultValue = "0") String freelancerId,
                                         @RequestParam(name = "skillId", defaultValue = "0") int skillId ) {
        try {
            freelancerService.deleteSkill(freelancerId,skillId );
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
            else if (field.equals("cv"))
                f.setCv(value);
            else{
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
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
    @GetMapping("/getProfileRecruiter")
    public ResponseEntity<?> getProfileRecruiter(@RequestHeader(name = "Authorization") String token,
                                                  @RequestParam(name = "recruiterId", defaultValue = "") String id,
                                                  @RequestParam(name = "freelancerId", defaultValue = "") String freelancerId) {
        RecruiterDetailDTO recruiterDetailDTO=recruiterService.getProfileRecruiterByFreelancer(id,freelancerId);
        try{
            if (recruiterDetailDTO != null) {
                return new ResponseEntity<>(recruiterDetailDTO, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
