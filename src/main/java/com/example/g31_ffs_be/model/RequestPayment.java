package com.example.g31_ffs_be.model;

import com.example.g31_ffs_be.model.Staff;
import com.example.g31_ffs_be.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "request_payment")
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
    private Instant dateRequest;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private Staff approvedBy;

    @Column(name = "date_approved")
    private Instant dateApproved;

    @Size(max = 255)
    @Column(name = "response_message")
    private String responseMessage;

    @Size(max = 45)
    @Column(name = "payment_code", length = 45)
    private String paymentCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Instant dateRequest) {
        this.dateRequest = dateRequest;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Staff getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Staff approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(Instant dateApproved) {
        this.dateApproved = dateApproved;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

}