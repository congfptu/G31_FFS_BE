package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.CareerResponse;
import com.example.g31_ffs_be.dto.CareerTitleDTO;
import com.example.g31_ffs_be.dto.FreelancerAdminDto;
import com.example.g31_ffs_be.dto.SubCareerTitleDTO;
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

        }
    }

    @Override
    public void updateCareer(Career career) {
    try{
        Optional<Career> careerOptional = careerRepository.findById(career.getId());
        if(careerOptional!=null){
            careerRepository.save(career);
        }
    }
      catch (Exception e){

      }
    }

    @Override
    public void deleteCareer(Integer id) {
        try{
            Optional<Career> careerOptional = careerRepository.findById(id);
            if(careerOptional!=null){
            careerRepository.deleteById(id);}
        }catch (Exception e){

        }
    }

    @Override
    public CareerResponse getAllCareer(int pageNumber, int pageSize, String keyword, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Career> careerPage=careerRepository.getCareerByName(keyword,pageable);
        List<Career> careerList=careerPage.getContent();
        CareerResponse careerResponse= new CareerResponse();
        careerResponse.setCareers(careerList);
        careerResponse.setPageIndex(pageNumber+1);
        careerResponse.setTotalPages(careerPage.getTotalPages());
        return careerResponse;
    }

    @Override
    public List<CareerTitleDTO> getCareerTitle() {
        List<Career> careers=careerRepository.findAll();
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
    }
}
