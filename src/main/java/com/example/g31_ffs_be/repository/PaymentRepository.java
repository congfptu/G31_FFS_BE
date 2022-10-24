package com.example.g31_ffs_be.repository;
import com.example.g31_ffs_be.model.RequestPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<RequestPayment,String> {
    @Query(value = " SELECT * FROM request_payment " +
            " WHERE (payment_code LIKE CONCAT('%',:keyword,'%') " +
            "or response_message like CONCAT('%',:keyword,'%')) " +
            "and status =:status " +
            "Order by payment_code asc,response_message asc,status asc "
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentSearchPaging(String keyword,Integer status, Pageable pageable);
    @Query(value = " SELECT * FROM request_payment "+
            " WHERE payment_code LIKE CONCAT('%',:keyword,'%') " +
            "or response_message like CONCAT('%',:keyword,'%') "
            , nativeQuery = true)
    Page<RequestPayment> getRequestPaymentPaging(String keyword,Pageable pageable);
    @Query(value = " SELECT * FROM request_payment " +
            " WHERE payment_code =:keyword"
            , nativeQuery = true)
    RequestPayment getRequestPaymentByCode(String keyword);
}
