package com.example.g31_ffs_be.model;

import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
@DynamicInsert
public class RequestPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date_request")
    private LocalDateTime dateRequest;

    @Column(name = "status")
    private Boolean status;

    @Size(max = 255)
    @Column(name = "payment_code")
    private String paymentCode;





}