package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.SubCareerResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.SubCareerRepository;
import com.example.g31_ffs_be.service.SubCareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
public class SubCareerController {
    @Autowired
    SubCareerService subCareerService;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    SubCareerRepository subCareerRepository;
    @GetMapping("/subCareer")
    public ResponseEntity<?> getAllSubCareer(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "name", defaultValue = "") String name,
                                             @RequestParam(name = "careerID", defaultValue = "") Integer careerID,
                                          @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {
        }
        int pageSize = 10;
        return new ResponseEntity<>( subCareerService.getAllSubCareerFilter(pageIndex, pageSize, name, careerID,null), HttpStatus.OK);
           // return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    @PreAuthorize("hasAuthority('admin')")
        @PostMapping("/subCareer/add")
        public ResponseEntity<?> createSubCareer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "career_id") Integer career_id
                ,@NotEmpty @RequestParam(name = "name") String name) {
            try {
                Subcareer subcareer1= subCareerRepository.getSubCareerBySubName(name);
                if(subcareer1==null) {
               subCareerRepository.insert(name,career_id);
                return new ResponseEntity<>("Thêm mới subcareer thành công", HttpStatus.CREATED);}
                else {
                    return new ResponseEntity<>("Thêm mới subcareer thất bại, duplicated name", HttpStatus.BAD_REQUEST);

                }
            } catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity<>("Thêm mới subcareer thất bại", HttpStatus.BAD_REQUEST);
            }
        }
    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/subCareer/update")
    public ResponseEntity<?> updateSubCareer(@RequestHeader(name = "Authorization") String token,
                                          @NotEmpty @RequestParam(name = "career_id") Integer career_id,
                                             @NotEmpty @RequestParam(name = "id") Integer id,
                                          @NotEmpty @NotBlank @RequestParam(name = "name") String name) {
        try {
            if (careerRepository.findById(career_id).isPresent()&&subCareerRepository.findById(id).isPresent() && !name.isEmpty()) {
                subCareerRepository.update(id,name,career_id);
            }
            return new ResponseEntity<>("Cập nhật subcareer thành công", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Cập nhật subcareer thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/subCareer/delete")
    public ResponseEntity<?> deleteSubCareer(@RequestHeader(name = "Authorization") String token,

                                          @NotEmpty @RequestParam(name = "id") Integer id
    ) {
        try {
            if (
                    subCareerRepository.findById(id).isPresent()) {
                subCareerRepository.deleteById(id);
                return new ResponseEntity<>("Xóa subcareer thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("ID subcareer không tồn tại", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Xóa subcareer thất bại "+e, HttpStatus.BAD_REQUEST);
        }
    }
    }

