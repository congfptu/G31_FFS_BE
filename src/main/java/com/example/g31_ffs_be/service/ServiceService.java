package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.ServiceDto;
import com.example.g31_ffs_be.dto.ServiceResponse;
import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Service;

import java.util.List;
public interface ServiceService {
    ServiceResponse getAllService(int roleId);

    void saveService(ServiceDto serviceDto);
    Boolean checkServiceNameUnique(String name);
    ServiceResponse getAllServiceByRole(String roleName);

}
