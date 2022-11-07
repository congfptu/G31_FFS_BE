package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

@Repository
public interface ReportRepository extends JpaRepository<Report,Integer> {
    @Query(value = "select * from `report` r " +
            "inner join `user` u on r.from_id=u.user_id"+
            " where u.fullname like CONCAT('%',:keyword,'%') " +
            "or r.date_created like CONCAT('%',:keyword,'%')"
            , nativeQuery = true)
    Page<Report> getReportByCreatedByOrCreatedDate(String keyword, Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO `report`(from_id,title,content,date_created) VALUES(:from_id,:title,:content,:dateCreated)"
            , nativeQuery = true)
    Integer insert(String from_id, String title, String content, LocalDateTime dateCreated);
}
