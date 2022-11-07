package com.example.g31_ffs_be.repository;
import com.example.g31_ffs_be.model.Subcareer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface SubCareerRepository extends JpaRepository<Subcareer, Integer> {
//    @Query(value = " SELECT sc.* FROM `career` c " +
//            "inner join `subcareer` sc on sc.career_id=c.id" +
//            " WHERE sc.name LIKE CONCAT('%',:subName,'%') and sc.career_id =:careerID"
//            , nativeQuery = true)
    @Query(value = " SELECT s FROM Subcareer s "+
            "left join FETCH s.career c " +
            " WHERE s.name LIKE CONCAT('%',:subName,'%') and s.career.id =:careerID "
            , countQuery = "SELECT count(s) FROM Subcareer s"+
            " left join s.career c " +
            " WHERE s.name LIKE CONCAT('%',:subName,'%') and s.career.id =:careerID")
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
    @Query(value = " insert into subcareer (name,career_id) values (:name, :career_id)"
            , nativeQuery = true)
    @Modifying
    @Transactional
    Integer insert(String name,Integer career_id);
    @Query(value = " update subcareer set name=:name, career_id=:career_id where id=:id"
            , nativeQuery = true)
    @Modifying
    @Transactional
    Integer update(Integer id,String name,Integer career_id);
}
