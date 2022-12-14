package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.*;
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

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/recruiter")
@PreAuthorize("hasAuthority('recruiter')")
public class RecruiterController {
    @Autowired
    FreelancerService freelancerService;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    RecruiterService recruiterService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecruiterRepository recruiterRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    JobRequestRepository jobRequestRepository;
    @Autowired
    PushTopHistoryRepository pushTopHistoryRepository;

    @Autowired FeeRepository feeRepository;

    @GetMapping("/findFreelancer")
    public ResponseEntity<?> getPostDetail(@RequestHeader(name = "Authorization") String token,
                                           @RequestParam(name = "city", defaultValue = "") String city,
                                           @RequestParam(name = "skill", defaultValue = "-1") List<Integer> skill,
                                           @RequestParam(name = "costOption", defaultValue = "1") int costOption,
                                           @RequestParam(name = "subCareer", defaultValue = "-1") int subCareer,
                                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                           @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex,
                                           @RequestParam(name = "isMemberShip", defaultValue = "true") Boolean isMemberShip) {
        try {
            return new ResponseEntity<>(freelancerService.getAllFreelancerByFilter(isMemberShip, city, costOption, subCareer, skill, keyword, pageIndex, 10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestHeader(name = "Authorization") String token,
                                        @RequestBody PostCreateDto post) {

        try {
            return new ResponseEntity<>(postService.createPost(post), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/addSkill")
    public ResponseEntity<?> addJobSkill(@RequestHeader(name = "Authorization") String token,
                                         @RequestParam(name = "jobId", defaultValue = "1") int jobId,
                                         @RequestParam(name = "skillId", defaultValue = "-1") int skillId
    ) {
        try {
            postRepository.insertJobSkill(jobId, skillId);
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteSkill")
    public ResponseEntity<?> deleteJobSkill(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam(name = "jobId", defaultValue = "-1") int jobId,
                                            @RequestParam(name = "skillId", defaultValue = "1") int skillId) {

        try {
            postRepository.deleteJobSkill(jobId, skillId);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update-post")
    public ResponseEntity<?> updatePost(@RequestHeader(name = "Authorization") String token,
                                        @RequestParam(name = "job_id") int jobId,
                                        @RequestParam(name = "field") String field,
                                        @RequestParam(name = "value") String value) {
        try {
            Job job = postRepository.getReferenceById(jobId);
            if (field.equals("jobTitle"))
                job.setJobTitle(value);
            else if (field.equals("area"))
                job.setArea(value);
            else if (field.equals("budget"))
                job.setBudget(Double.parseDouble(value));
            else if (field.equals("subCareer")) {
                Subcareer subcareer = new Subcareer();
                subcareer.setId(Integer.parseInt(value));
                job.setSubCareer(subcareer);
            } else {
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            postRepository.save(job);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/getAllJobPosted")
    public ResponseEntity<?> getAllJobPosted(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(name = "recruiterId", defaultValue = "") String recruiterId,
                                             @RequestParam(name = "status", defaultValue = "-1") int status,
                                             @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                             @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex) {
        try {
            return new ResponseEntity<>(postService.getAllJobPosted(recruiterId, status, keyword, pageIndex, 10), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getFreelancerApplied")
    public ResponseEntity<?> getFreelancerOfJob(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "recruiterId", defaultValue = "") String recruiterId,
                                                @RequestParam(name = "jobId", defaultValue = "-1") int jobId,
                                                @RequestParam(name = "skill", defaultValue = "-1") List<Integer> skill,
                                                @RequestParam(name = "status", defaultValue = "0") int status,
                                                @RequestParam(name = "subCareer", defaultValue = "-1") int subCareer,
                                                @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                @RequestParam(name = "city", defaultValue = "") String city,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") int pageIndex) {

        try {
            APIResponse<FreelancerFilterDto> freelancerApplied = freelancerService.getFreelancerApplied(jobId, recruiterId, city, skill, subCareer, keyword, status, pageIndex, 10);
            return new ResponseEntity<>(freelancerApplied == null ? false : freelancerApplied, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getProfileRecruiter")
    public ResponseEntity<?> getRecruiterProfile(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "recruiterId", defaultValue = "") String recruiterId) {

        try {
            RecruiterDetailDTO recruiterDetailDTO = recruiterService.getDetailRecruiter(recruiterId);
            return new ResponseEntity<>(recruiterDetailDTO == null ? false : recruiterDetailDTO, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestHeader(name = "Authorization") String token,
                                           @RequestBody RegisterDto registerDto) {
        try {
            recruiterService.updateProfile(registerDto);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/updateProfileRecruiter")
    public ResponseEntity<?> updateProfileRecruiter(@RequestHeader(name = "Authorization") String token,
                                                    @RequestBody RegisterDto registerDto) {
        try {
            recruiterService.updateProfileRecruiter(registerDto);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/updateAvatar")
    public ResponseEntity<?> editInformation(@RequestHeader(name = "Authorization") String token,
                                             @RequestParam(name = "recruiterId") String recruiterId,
                                             @RequestParam(name = "avatar") String avatar
    ) {
        try {
            User user = userRepository.getReferenceById(recruiterId);
            user.setAvatar(avatar);
            userRepository.save(user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteJob")
    public ResponseEntity<?> deleteJob(@RequestHeader(name = "Authorization") String token,
                                       @RequestParam(name = "recruiterId") String recruiterId,
                                       @RequestParam(name = "jobId") int jobId) {
        try {
            Job job = postRepository.getReferenceById(jobId);
            Recruiter r = recruiterRepository.getReferenceById(recruiterId);
            if (job.getCreateBy().getId().equals(r.getId())) {

                postRepository.deleteById(job.getId());
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/hideJob")
    public ResponseEntity<?> hideJob(@RequestHeader(name = "Authorization") String token,
                                     @RequestParam(name = "recruiterId") String recruiterId,
                                     @RequestParam(name = "jobId") int jobId
    ) {
        try {
            Job job = postRepository.getReferenceById(jobId);
            Recruiter r = recruiterRepository.getReferenceById(recruiterId);
            if (job.getCreateBy().getId().equals(r.getId())) {
                if (job.getIsActive())
                    job.setIsActive(false);
                else {
                 /*   job.setTime(LocalDateTime.now());*/
                    job.setIsActive(true);
                    postRepository.save(job);
                }
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @GetMapping("/viewDetailPost")
    public ResponseEntity<?> viewDetailPost(@RequestHeader(name = "Authorization") String token,
                                            @RequestParam(name = "recruiterId") String recruiterId,
                                            @RequestParam(name = "jobId") int jobId
    ) {
        try {
            return new ResponseEntity<>(postService.viewDetailPostByRecruiter(recruiterId, jobId), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/responseJobApply")
    public ResponseEntity<?> approvedFreelancer(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "freelancerId") String freelancerId,
                                                @RequestParam(name = "jobId") int jobId,
                                                @RequestParam(name = "status") int status
    ) {
        try {
            jobRequestRepository.responseJobApply(jobId, freelancerId, status, LocalDateTime.now());
            Notification notification = new Notification();
            User freelancerUser = userRepository.getReferenceById(freelancerId);
            freelancerUser.setUnRead(freelancerUser.getUnRead() + 1);
            userRepository.save(freelancerUser);
            Job job = postRepository.getReferenceById(jobId);
            notification.setFrom(new User(job.getCreateBy().getId()));
            notification.setTo(new User(freelancerId));
            notification.setNotificationType(status);
            notification.setDate(LocalDateTime.now());
            notification.setJob(new Job(jobId));
            notification.setStatus(false);
            notificationRepository.save(notification);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    @PutMapping("/pushTop")
    public ResponseEntity<?> pushTopJobRecruiter(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "recruiterId") String recruiterId,
                                                 @RequestParam(name = "jobId") int jobId
    ) {
        try {
            int numberOfTopJob = postRepository.countIsTopJob();
            Job job = postRepository.getReferenceById(jobId);
            int countPushTopByRecruiterId=postRepository.countPushTopBy(recruiterId);
            if (countPushTopByRecruiterId>=1)   return new ResponseEntity<>("B???n ch??? ???????c ?????y top 1 b??i ????ng trong danh s??ch c??ng vi???c n???i b???t",
                    HttpStatus.BAD_REQUEST);
            if (numberOfTopJob >=5) {
                int minOfTimeToPushTop= postRepository.minOfTimeToPushTop();
                return new ResponseEntity<>("Hi???n t???i h??? th???ng ???? h???t ch??? ?????y top b??i ????ng," +
                        " vui l??ng ch??? sau ??t nh???t "+minOfTimeToPushTop+" ng??y m???i c?? th??? c?? slot ?????y top!",
                        HttpStatus.BAD_REQUEST);
            }
            if(job.getIsTop()) return new ResponseEntity<>("B??i ????ng c???a b???n ???? l??n trang n???i b???t!", HttpStatus.BAD_REQUEST);
            Recruiter recruiter = recruiterRepository.getReferenceById(recruiterId);
            job.setIsTop(true);
            job.setTopTime(LocalDateTime.now());
            double feePushtop=feeRepository.getReferenceById(4).getPrice();
            User user=recruiter.getUser();
            user.setAccountBalance(user.getAccountBalance()-feePushtop);
            userRepository.save(user);
            postRepository.save(job);
            PushTopHistory pushTopHistory=new PushTopHistory();
            pushTopHistory.setRecruiter(recruiter);
            pushTopHistory.setFee(feePushtop);
            pushTopHistory.setDatePush(LocalDateTime.now());
            pushTopHistoryRepository.save(pushTopHistory);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Kh??ng th??? ?????y top", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getProfileFreelancer")
    public ResponseEntity<?> getProfileFreelancer(@RequestHeader(name = "Authorization") String token,
                                                  @RequestParam(name = "id", defaultValue = "") String id,
                                                  @RequestParam(name = "recruiterId", defaultValue = "") String recruiterId) {
        FreelancerProfileDTO f = freelancerService.getDetailFreelancerByRecruiter(recruiterId, id);
        try {
            if (f != null) {
                return new ResponseEntity<>(f, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/statistic")
    public ResponseEntity<?> statisticRecruiter(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "recruiterId", defaultValue = "") String recruiterId) {

        try {
            User user = userRepository.getReferenceById(recruiterId);
            Recruiter recruiter = user.getRecruiter();
            RecruiterStatistic statistic = new RecruiterStatistic();
            statistic.setAvgStar(user.getStar());
            statistic.setTotalFeedbacks(user.getFeedbackTos().size());
            statistic.setTotalPosted(recruiter.getJobs().size());

            int totalApplied = 0;
            for (Job j : recruiter.getJobs())
                totalApplied += j.getJobRequests().size();
            statistic.setTotalApplied(totalApplied);

            return new ResponseEntity<>(statistic, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }


}
