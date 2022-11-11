package com.example.g31_ffs_be.repository;
import com.example.g31_ffs_be.model.RequestPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<RequestPayment,String> {
    @Query(value =  " SELECT r FROM RequestPayment r " +
                    "LEFT JOIN FETCH r.user "+
                    " WHERE r.paymentCode like CONCAT('%',:keyword,'%') " +
                    "Order by r.dateRequest desc ",
            countQuery = " SELECT count(r.id) FROM RequestPayment r " +
                    "LEFT JOIN  r.user "+
                    " WHERE r.paymentCode like CONCAT('%',:keyword,'%') " +
                    "Order by r.dateRequest desc "
           )
    Page<RequestPayment> getRequestPaymentSearchPaging(String keyword, Pageable pageable);

    @Query(value = " SELECT * FROM request_payment " +
            " WHERE user_id=:userId " +
            " and (:from like CONCAT('%','1970-01-01','%') or date_request>=:from ) " +
            "and (:to like CONCAT('%','1970-01-01','%') or date_request<=:to)" +
            "order by date_request desc"
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentByFromTo(String from, String to, String userId,Pageable pageable);

    Optional<RequestPayment> findByPaymentCode(String paymentCode);


}
