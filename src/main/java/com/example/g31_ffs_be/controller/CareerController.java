package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.CareerTitleDTO;
import com.example.g31_ffs_be.model.Ban;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.SubCareerRepository;
import com.example.g31_ffs_be.service.CareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
public class CareerController {
    @Autowired
    CareerService careerService;
    @Autowired
    CareerRepository careerRepository;
    @Autowired
    SubCareerRepository subCareerRepository;

    @GetMapping("/career")
    public ResponseEntity<?> getAllCareerPaging(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "name", defaultValue = "") String name,
                                          @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;

        CareerResponse fas = careerService.getAllCareer(pageIndex, pageSize, name, null);
        if (fas.getTotalPages() >= pageIndex - 1) {
            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/getAllCareer")
    public ResponseEntity<?> getAllCareer() {
        if(careerRepository.findAll().size()!=0){
            return new ResponseEntity<>(careerRepository.findAll(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/getCareerTitle")
    public ResponseEntity<?> getAllCareerTitle() {
        if(careerService.getCareerTitle().size()!=0){
            List<CareerTitleDTO> list=careerService.getCareerTitle();
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("không có dữ liệu. có thể server chết!", HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping("/career/add")
    public ResponseEntity<?> createCareer(@RequestHeader(name = "Authorization") String token,
                                          @RequestParam(name = "name") String name) {
        try {
            Career career1= careerRepository.getCareerByName(name);

               if(career1==null) {
                   Career career = new Career();
                   System.out.println("chay vao day");
                   career.setName(name);
                   careerRepository.save(career);
                   return new ResponseEntity<>("Thêm mới career thành công", HttpStatus.OK);
            }else{
                   return new ResponseEntity<>("Thêm mới career thất bại, duplicated name", HttpStatus.BAD_REQUEST);
               }

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Thêm mới career thất bại", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/career/update")
    public ResponseEntity<?> updateCareer(@RequestHeader(name = "Authorization") String token,
                                          @NotNull @NotBlank @RequestParam(name = "id") Integer id,
                                          @NotNull @NotBlank @RequestParam(name = "name") String name) {
        try {
            if (careerRepository.findById(id).isPresent() && !name.isEmpty()) {
                Career career = new Career();
                career.setId(id);
                career.setName(name);
                careerRepository.save(career);
            }
            return new ResponseEntity<>("Cập nhật career thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Cập nhật career thất bại", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/career/delete")
    public ResponseEntity<?> deleteCareer(@RequestHeader(name = "Authorization") String token,
                                          @NotNull @NotBlank @RequestParam(name = "id") Integer id
    ) {
        try {
            if (careerRepository.findById(id).isPresent()) {
                careerRepository.deleteById(id);
            } else {
                return new ResponseEntity<>("ID career không tồn tại", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Xóa career thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Xóa career thất bại", HttpStatus.BAD_REQUEST);
        }
    }
}
