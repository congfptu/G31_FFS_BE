package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.ServiceDto;
import com.example.g31_ffs_be.dto.ServiceResponse;
import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Service;

import java.util.List;
public interface ServiceService {
    List<ServiceDto> getServiceByName(String name, int roleId, int pageNo, int pageSize);

    void saveService(ServiceDto serviceDto);
    List<Benefit> getBenefitsOfServiceByID(int id);
    Boolean checkServiceNameUnique(String name);
    List<Service> getAllService(String roleName);

}
