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
            "LEFT JOIN FETCH s.benefits b "+
            "LEFT JOIN FETCH s.role r "+
            "where s.serviceName like CONCAT('%',:name,'%') and r.id = :roleId " +
            "group by s",
    countQuery = "select count(distinct s.id) from Service s " +
            "LEFT JOIN s.benefits b "+
                    "LEFT JOIN s.role r "+
                    "where s.serviceName like CONCAT('%',:name,'%') and r.id = :roleId ")
    Page<Service> getServiceByName(String name,int roleId,Pageable pageable);
    @Query(value = "select s from Service s "
             +"LEFT JOIN fetch s.benefits "+
            "where s.serviceName =:name " )
    Service getService(String name);
    @Query(value = "SELECT s FROM Service s "+
            "LEFT JOIN fetch s.benefits b "+
            " where s.id=:id "+
            "order by b.id asc")
    Service getBenefitByServiceID(int id);

}
