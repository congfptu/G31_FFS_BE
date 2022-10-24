package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.ServiceDto;
import com.example.g31_ffs_be.dto.ServiceResponse;
import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Service;
import com.example.g31_ffs_be.repository.BenefitRepository;
import com.example.g31_ffs_be.repository.ServiceRepository;
import com.example.g31_ffs_be.service.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    BenefitRepository benefitRepository;
    @Autowired
    private ModelMapper mapper;
    private ServiceDto mapToServiceDto(Service service){
        ServiceDto serviceDto=mapper.map(service, ServiceDto.class);
        return serviceDto;
    }
    @Override
    public  ServiceResponse getServiceByName(String name,String roleId,int pageNo, int pageSize) {
        ServiceResponse serviceResponse=new ServiceResponse();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Service> page = serviceRepository.getServiceByName(name,roleId,pageable);
        List<Service> services=page.getContent();
        List<ServiceDto> serviceDtos=new ArrayList<>();
        for (Service service: services){
            ServiceDto serviceDto=new ServiceDto();
            serviceDto=mapper.map(service,ServiceDto.class);
            serviceDtos.add(serviceDto);
        }
        serviceResponse.setServices(serviceDtos);
        serviceResponse.setTotalPages(page.getTotalPages());
        serviceResponse.setPageIndex(pageNo+1);
        return serviceResponse;

    }
    private Service mapToServiceEntity(ServiceDto serviceDto){
        Service service=mapper.map(serviceDto, Service.class);
        return service;
    }
    @Override
    public void saveService(ServiceDto serviceDto) {
        Service service=serviceRepository.findById(serviceDto.getId()).get();
        service.setBenefits(null);
        serviceRepository.save(service);
        service=mapToServiceEntity(serviceDto);
        serviceRepository.save(service);
    }

    @Override
    public List<Benefit> getBenefitsOfServiceByID(int id) {

        return  serviceRepository.findById(id).get().getBenefits();
    }


    @Override
    public Boolean checkServiceNameUnique(String name) {
        if (serviceRepository.getService(name)!=null)
            return true;
        return false;
    }
}
