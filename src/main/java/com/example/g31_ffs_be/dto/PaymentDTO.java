package com.example.g31_ffs_be.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PaymentDTO {
    String id;
    String code;
    String userId;
    double money;
    Instant dateRequest;
    Instant dateApprove;
    Integer status;
    String responseMessage;

}
