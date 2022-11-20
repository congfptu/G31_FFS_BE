package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.ServiceDto;
import com.example.g31_ffs_be.dto.ServiceResponse;
import com.example.g31_ffs_be.model.Benefit;

import com.example.g31_ffs_be.model.Fee;
import com.example.g31_ffs_be.model.Role;
import com.example.g31_ffs_be.model.Service;
import com.example.g31_ffs_be.repository.BenefitRepository;
import com.example.g31_ffs_be.repository.FeeRepository;
import com.example.g31_ffs_be.repository.RoleRepository;
import com.example.g31_ffs_be.repository.ServiceRepository;
import com.example.g31_ffs_be.service.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    BenefitRepository benefitRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FeeRepository feeRepository;
    @Autowired
    private ModelMapper mapper;
    private ServiceDto mapToServiceDto(Service service){
        ServiceDto serviceDto=mapper.map(service, ServiceDto.class);
        return serviceDto;
    }

    private Service mapToServiceEntity(ServiceDto serviceDto){
        Service service=mapper.map(serviceDto, Service.class);
        return service;
    }

    @Override
    public ServiceResponse getAllService(int roleId) {
        List<Service> services = serviceRepository.getServiceByName(roleId);
        List<ServiceDto> serviceDTOs=new ArrayList<>();
        for(Service service:services){
            serviceDTOs.add(mapToServiceDto(service));
        }
        List<Fee> fees=feeRepository.findAll();
        Role role=roleRepository.getReferenceById(roleId);
        Set<Benefit> benefits=role.getBenefits();
        ServiceResponse serviceResponse=new ServiceResponse();
        serviceResponse.setServices(serviceDTOs);
        serviceResponse.setFees(fees);
        serviceResponse.setBenefits(benefits);
        return serviceResponse;
    }

    @Override
    public void saveService(ServiceDto serviceDto) {
        Service service=serviceRepository.findById(serviceDto.getId()).get();
        serviceRepository.save(service);
        service=mapToServiceEntity(serviceDto);
        serviceRepository.save(service);
    }



    @Override
    public Boolean checkServiceNameUnique(String name) {
        if (serviceRepository.getService(name)!=null)
            return true;
        return false;
    }

    @Override
    public ServiceResponse getAllServiceByRole(String roleName) {
         ServiceResponse serviceResponse =new ServiceResponse();
         Role role=roleRepository.findByRoleName(roleName);
        List<Service> services =serviceRepository.getServiceByRoleName(roleName);
        List<ServiceDto> serviceDTOs=new ArrayList<>();
        for(Service service:services){
            serviceDTOs.add(mapToServiceDto(service));
        }
         serviceResponse.setServices(serviceDTOs);
        serviceResponse.setBenefits(role.getBenefits());
        return serviceResponse;
    }
}
