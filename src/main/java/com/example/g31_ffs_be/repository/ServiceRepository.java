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
            "where s.serviceName like CONCAT('%',:name,'%') and r.id = :roleId ")
    List<Service> getServiceByName(String name,int roleId,Pageable pageable);
    @Query(value = "select s from Service s "
             +"LEFT JOIN fetch s.benefits "+
            "where s.serviceName =:name " )
    Service getService(String name);
    @Query(value = "SELECT s FROM Service s "+
            "LEFT JOIN fetch s.benefits b "+
            " where s.id=:id "+
            "order by b.id asc")
    Service getBenefitByServiceID(int id);

    @Query(value = "SELECT distinct s FROM Service s "+
            "LEFT JOIN fetch s.benefits b "+
            "LEFT JOIN fetch s.role r where r.roleName=:roleName"
    )
    List<Service> getAllService(String roleName);

}
