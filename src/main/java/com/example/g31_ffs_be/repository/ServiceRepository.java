package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceRepository extends JpaRepository<Service,Integer> {
    @Query(value = "select a.* from `Service` a " +
            "where service_name like CONCAT('%',:name,'%') and a.role_id like :roleId " +
            "Order By service_name", nativeQuery = true)
    Page<Service> getServiceByName(String name,String roleId,Pageable pageable);
    @Query(value = "select * from `Service` " +
            "where service_name = :name " , nativeQuery = true)
    Service getService(String name);

}
