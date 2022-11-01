package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.dto.FeedbackDTO;
import com.example.g31_ffs_be.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.Instant;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
    @Query(value = "select f from Feedback f"
            +" left join fetch f.from fr"
            +" left join fetch f.to ft"
            +" where ft.id=:fromId"
            , countQuery = "select count(f) from Feedback f"
                    +" left join  f.from fr"
                    +" left join  f.to ft"
                    +" where ft.id=:fromId")
    Page<Feedback> getFeedbacksByFromId(String fromId,Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = " insert into feedback (from_id,to_id,star,content,date) values (:from_id,:to_id,:star,:content,:date)"
            , nativeQuery = true)
    Integer insert(String from_id, String to_id, Integer star, String content, Instant date);

}
