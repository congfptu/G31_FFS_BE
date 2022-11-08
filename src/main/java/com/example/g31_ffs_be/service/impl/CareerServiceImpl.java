package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.*;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Freelancer;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.service.CareerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CareerServiceImpl implements CareerService {
    @Autowired
    private CareerRepository careerRepository;
    @Override
    public void addCareer(Career career) {
        try {
            careerRepository.save(career);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void updateCareer(Career career) {
    try{
        Optional<Career> careerOptional = careerRepository.findById(career.getId());
        if(careerOptional.isPresent()){
            careerRepository.save(career);
        }
    }
      catch (Exception e){
          System.out.println(e);
      }
    }

    @Override
    public void deleteCareer(Integer id) {
        try{
            Optional<Career> careerOptional = careerRepository.findById(id);
            if(careerOptional.isPresent()){
            careerRepository.deleteById(id);}
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public APIResponse<Career> getAllCareer(int pageNumber, int pageSize, String keyword, String sortValue) {
        try{
            APIResponse<Career> apiResponse=new APIResponse<>();
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Career> careerPage=careerRepository.getCareerByName(keyword,pageable);
            List<Career> careerList=careerPage.getContent();
            apiResponse.setResults(careerList);
            apiResponse.setPageIndex(pageNumber+1);
            apiResponse.setTotalPages(careerPage.getTotalPages());
            apiResponse.setTotalResults(careerPage.getTotalElements());
            return apiResponse;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @Override
    public List<CareerTitleDTO> getCareerTitle() {
        try {
            List<Career> careers=careerRepository.getAllCareer();
            List<CareerTitleDTO> list= new ArrayList<>();
            for (Career c:careers) {
                CareerTitleDTO careerTitleDTO= new CareerTitleDTO();
                careerTitleDTO.setId(c.getId());
                careerTitleDTO.setName(c.getName());
                SubCareerTitleDTO subCareerTitleDTO = new SubCareerTitleDTO();
                Set<Subcareer> listSub=c.getSubcareers();
                subCareerTitleDTO.setData(listSub);
                subCareerTitleDTO.setTitle(c.getName());
                careerTitleDTO.setSubCareers(subCareerTitleDTO);
                list.add(careerTitleDTO);
            }
            return list;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
