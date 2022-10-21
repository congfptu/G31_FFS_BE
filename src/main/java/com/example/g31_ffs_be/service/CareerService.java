package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.model.Career;


public interface CareerService {
    void addCareer(Career career);
    void updateCareer(Career career);
    void deleteCareer(Integer id);
    CareerResponse getAllCareer(int pageNumber, int pageSize, String keyword, String sortValue);

}
