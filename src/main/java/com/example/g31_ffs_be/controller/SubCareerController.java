package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.SubCareerResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.SubCareerRepository;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.SubCareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @GetMapping("/subcareer")
    public ResponseEntity<?> getAllSubCareer(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "name", defaultValue = "") String name,
                                             @RequestParam(name = "careerID", defaultValue = "") Integer careerID,
                                          @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = subCareerRepository.getSubCareerByCareerIDAndSubCareerName(name,careerID, p).getTotalPages();

        if (totalPage >= pageIndex - 1) {
            SubCareerResponse fas = subCareerService.getAllSubCareerSearchByCareerIDAndSubName(pageIndex, pageSize, name, careerID,null);

            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }}
        @PostMapping("/subcareer/add")
        public ResponseEntity<?> createSubCareer(@RequestHeader(name = "Authorization") String token,
                                                 @RequestParam(name = "career_id") Integer career_id
                ,@NotEmpty @RequestParam(name = "name") String name) {
            try {
                Subcareer subcareer1= subCareerRepository.getSubCareerBySubName(name);
                if(subcareer1==null) {
                Subcareer subcareer = new Subcareer();
                subcareer.getCareer().setId(career_id);
                subcareer.setName(name);
                subCareerRepository.save(subcareer);
                return new ResponseEntity<>("Thêm mới subcareer thành công", HttpStatus.OK);}
                else {
                    return new ResponseEntity<>("Thêm mới subcareer thất bại, duplicated name", HttpStatus.BAD_REQUEST);

                }
            } catch (Exception e) {
                return new ResponseEntity<>("Thêm mới subcareer thất bại", HttpStatus.BAD_REQUEST);
            }
        }
    @PutMapping("/subcareer/update")
    public ResponseEntity<?> updateSubCareer(@RequestHeader(name = "Authorization") String token,
                                          @NotEmpty @RequestParam(name = "career_id") Integer career_id,
                                             @NotEmpty @RequestParam(name = "id") Integer id,
                                          @NotEmpty @NotBlank @RequestParam(name = "name") String name) {
        try {
            if (careerRepository.findById(career_id).isPresent()&&subCareerRepository.findById(id).isPresent() && !name.isEmpty()) {
                Subcareer subcareer = new Subcareer();
                subcareer.setId(id);
                subcareer.setName(name);
                subcareer.getCareer().setId(career_id);
                subCareerRepository.save(subcareer);
            }
            return new ResponseEntity<>("Cập nhật subcareer thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cập nhật subcareer thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/subcareer/delete")
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
            return new ResponseEntity<>("Xóa subcareer thất bại", HttpStatus.BAD_REQUEST);
        }
    }
    }

