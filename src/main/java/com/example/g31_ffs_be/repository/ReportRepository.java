package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.dto.ReportDTO;
import com.example.g31_ffs_be.model.Report;
import com.example.g31_ffs_be.model.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {
    @Query(value = "select * from `report` r " +
            "inner join `user` u on r.from_id=u.user_id"+
            " where u.fullname like CONCAT('%',:keyword,'%') " +
            "or r.date_created like CONCAT('%',:keyword,'%')"
            , nativeQuery = true)
    Page<Report> getReportByCreatedByOrCreatedDate(String keyword, Pageable pageable);
}
