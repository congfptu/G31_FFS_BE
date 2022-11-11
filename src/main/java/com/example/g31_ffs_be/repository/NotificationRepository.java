package com.example.g31_ffs_be.repository;

import com.example.g31_ffs_be.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    @Query(value = "SELECT no FROM Notification no "+
            "LEFT JOIN FETCH no.from froms " +
            "LEFT JOIN FETCH no.to tos " +
            "LEFT JOIN FETCH no.job " +
            "where tos.id=:userId "+
            "order by no.date desc"
            ,
            countQuery =  "SELECT count(no.id) FROM Notification no "+
                    "LEFT JOIN  no.from froms " +
                    "LEFT JOIN  no.to tos " +
                    "LEFT JOIN  no.job " +
                    "where tos.id=:userId "
                   )
    Page<Notification> getNotificationByUserId(String userId, Pageable pageable);
}
