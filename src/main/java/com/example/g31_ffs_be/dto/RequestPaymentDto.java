package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
public class RequestPaymentDto {

    private Integer id;

    private String userId;

    private Double amount;

    private LocalDateTime dateRequest;

    private Boolean status;

    private String paymentCode;
}
