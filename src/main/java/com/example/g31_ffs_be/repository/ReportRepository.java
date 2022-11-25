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
public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query(value = "select r from Report r " +
                   "left join fetch r.from u " +
                    "where u.fullName like CONCAT('%',:keyword,'%') " +
                    "or r.content like CONCAT('%',:keyword,'%') "+
                    "or r.title like CONCAT('%',:keyword,'%')",
            countQuery =  "select count(r.id) from Report r " +
                    "left join  r.from u " +
                    "where u.fullName like CONCAT('%',:keyword,'%') " +
                    "or r.content like CONCAT('%',:keyword,'%') "+
                    "or r.title like CONCAT('%',:keyword,'%')")

    Page<Report> getAllReport(String keyword, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO report (from_id,title,content,date_created) VALUES(:from_id,:title,:content,:dateCreated)"
            , nativeQuery = true)
    Integer insert(String from_id, String title, String content, LocalDateTime dateCreated);
}
