package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.SubCareerResponse;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Subcareer;

import java.util.List;

public interface SubCareerService {
    void addSubCareer(Subcareer career);
    void updateSubCareer(Subcareer career);
    void deleteSubCareer(Integer id);
    SubCareerResponse getAllSubCareer(int pageNumber, int pageSize, String keyword,Integer CareerID, String sortValue);
}
