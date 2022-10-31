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
    @Query(value = " SELECT * FROM request_payment " +
            " WHERE (payment_code LIKE CONCAT('%',:keyword,'%') " +
            "or response_message like CONCAT('%',:keyword,'%')) " +
            "and status like CONCAT('%',:status,'%') " +
            "Order by payment_code asc,response_message asc,status asc "
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentSearchPaging(String keyword,String status, Pageable pageable);
    @Query(value = " SELECT * FROM request_payment "+
            " WHERE payment_code LIKE CONCAT('%',:keyword,'%') " +
            "or response_message like CONCAT('%',:keyword,'%') "
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentPaging(String keyword,Pageable pageable);
    @Query(value = " SELECT * FROM request_payment " +
            " WHERE payment_code =:keyword"
            , nativeQuery = true)
    RequestPayment getRequestPaymentByCode(String keyword);

    @Query(value = " SELECT * FROM request_payment " +
            " WHERE user_id =:userId"
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentByUserId(String userId, Pageable pageable);
    @Query(value = " SELECT * FROM request_payment " +
            " WHERE (date_request between :from and :to) and user_id=:userId"
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentByDateRequest(LocalDateTime from, LocalDateTime to, String userId,Pageable pageable);

    Optional<RequestPayment> findByPaymentCode(String paymentCode);


}
