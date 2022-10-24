package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.dto.SubCareerDTO;
import com.example.g31_ffs_be.model.Career;
import com.example.g31_ffs_be.model.Subcareer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCareerRepository extends JpaRepository<Subcareer, Integer> {
    @Query(value = " SELECT sc.* FROM `career` c " +
            "inner join `subcareer` sc on sc.career_id=c.id" +
            " WHERE sc.name LIKE CONCAT('%',:subName,'%') and sc.career_id LIKE CONCAT('%',:careerID,'%')"
            , nativeQuery = true)
    Page<Subcareer> getSubCareerByCareerIDAndSubCareerName(String subName, Integer careerID, Pageable pageable);

    @Query(value = " SELECT sc.* FROM `career` c " +
            "inner join `subcareer` sc on sc.career_id=c.id" +
            " WHERE sc.name LIKE CONCAT('%',:subName,'%') "
            , nativeQuery = true)
    Page<Subcareer> getAllSubCareerBySubCareerName(String subName, Pageable pageable);

    @Query(value = " SELECT * FROM subcareer" +
            " WHERE name=:subName"
            , nativeQuery = true)
    Subcareer getSubCareerBySubName(String subName);
}
