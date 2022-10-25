package com.example.g31_ffs_be.model;

import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayment {
    @Id
    @Size(max = 45)
    @Column(name = "id", nullable = false, length = 45)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date_request")
    private LocalDateTime dateRequest;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Staff approvedBy;

    @Column(name = "date_approved")
    private LocalDateTime dateApproved;

    @Size(max = 255)
    @Column(name = "response_message")
    private String responseMessage;

    @Size(max = 45)
    @Column(name = "payment_code", length = 45)
    private String paymentCode;


}