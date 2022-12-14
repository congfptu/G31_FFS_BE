package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.SubCareerResponse;
import com.example.g31_ffs_be.model.Subcareer;
import com.example.g31_ffs_be.repository.CareerRepository;
import com.example.g31_ffs_be.repository.SubCareerRepository;
import com.example.g31_ffs_be.service.SubCareerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCareerServiceImpl implements SubCareerService {
    @Autowired
    private SubCareerRepository subCareerRepository;
    @Autowired
    private CareerRepository careerRepository;
    @Override
    public void addSubCareer(Subcareer career) {
        try {
            subCareerRepository.save(career);
        }
        catch (Exception e){

        }
    }

    @Override
    public void updateSubCareer(Subcareer career) {
        Optional<Subcareer> careerOptional = subCareerRepository.findById(career.getId());
        try{
        if(careerOptional!=null){
            subCareerRepository.save(career);
        }
    }
      catch (Exception e){

    }
    }

    @Override
    public void deleteSubCareer(Integer id) {
        try{
            Optional<Subcareer> careerOptional = subCareerRepository.findById(id);
            if(careerOptional!=null){
                subCareerRepository.deleteById(id);}
        }catch (Exception e){

        }
    }

    @Override
    public APIResponse<Subcareer> getAllSubCareerFilter(int pageNumber, int pageSize, String keyword, Integer careerID, String sortValue) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Subcareer> subcareerPage=subCareerRepository.getSubCareerByCareerIDAndSubCareerName(keyword,careerID,pageable);
        List<Subcareer> subcareerList=subcareerPage.getContent();
        APIResponse<Subcareer> apiResponse= new APIResponse<>();
        apiResponse.setResults(subcareerList);
        apiResponse.setPageIndex(pageNumber+1);
        apiResponse.setTotalPages(subcareerPage.getTotalPages());
        apiResponse.setTotalResults(subcareerPage.getTotalElements());
        return apiResponse;
    }
}
