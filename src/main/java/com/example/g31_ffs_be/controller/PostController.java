package com.example.g31_ffs_be.controller;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.PostDTOResponse;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.PostRepository;
import com.example.g31_ffs_be.service.CareerService;
import com.example.g31_ffs_be.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/staff")
public class PostController {
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @GetMapping("/post")
    public ResponseEntity<?> getAllPostSearchByDesAndStatusPaging(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "name", defaultValue = "") String name,
                                                @RequestParam(name = "status", defaultValue = "") Boolean status,
                                                @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = postRepository.getRequestPostByStatusAndDescription(name, status,p).getTotalPages();

        if (totalPage >= pageIndex - 1) {
            PostDTOResponse fas = postService.getAllPostByNameAndStatusPaging(pageIndex, pageSize, name,status, null);

            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/allPost")
    public ResponseEntity<?> getAllPostPaging(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "name", defaultValue = "") String name,

                                              @RequestParam(name = "pageIndex", defaultValue = "0") String pageNo) {
        int pageIndex = 0;
        try {
            pageIndex = Integer.parseInt(pageNo);
        } catch (Exception e) {

        }
        int pageSize = 5;
        Pageable p = PageRequest.of(pageIndex, pageSize);
        int totalPage = postRepository.getRequestPostSearchByNamePaging(name,p).getTotalPages();

        if (totalPage >= pageIndex - 1) {
            PostDTOResponse fas = postService.getAllPostSearchNamePaging(pageIndex, pageSize, name, null);

            return new ResponseEntity<>(fas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("không có dữ liệu trang này!", HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping("/postDetail")
    public ResponseEntity<?> getPostDetail(@RequestHeader(name = "Authorization") String token,
                                                @RequestParam(name = "id", defaultValue = "") String id
                                               ) {




            return new ResponseEntity<>(postService.getPostDetail(id), HttpStatus.OK);

}}
