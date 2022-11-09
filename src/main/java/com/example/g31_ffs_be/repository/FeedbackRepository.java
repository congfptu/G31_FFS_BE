package com.example.g31_ffs_be.repository;
import com.example.g31_ffs_be.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
    @Query(value = "select f from Feedback f"
            +" left join fetch f.from fr"
            +" left join fetch f.to ft"
            +" where ft.id=:toId"
            , countQuery = "select count(f.id) from Feedback f"
                    +" left join  f.from fr"
                    +" left join  f.to ft"
                    +" where ft.id=:toId")
    Page<Feedback> getFeedbacksFromId(String toId,Pageable pageable);
    @Modifying
    @Transactional
    @Query(value = " insert into feedback (from_id,job_id,to_id,star,content,created_date) values (:fromId,:jobId,:toId,:star,:content,:createdDate)"
            , nativeQuery = true)
    Integer insert(String fromId,int jobId, String toId, Integer star, String content, LocalDateTime createdDate);

}
