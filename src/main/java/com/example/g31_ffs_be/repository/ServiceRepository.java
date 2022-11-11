package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service,Integer> {
    @Query(value = "select distinct s from Service s " +
            "LEFT JOIN FETCH s.role r "+
            "where r.id = :roleId ")
    List<Service> getServiceByName(int roleId);
    @Query(value = "select s from Service s "+
            "where s.serviceName =:name " )
    Service getService(String name);


}
