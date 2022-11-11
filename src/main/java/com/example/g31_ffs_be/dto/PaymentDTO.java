package com.example.g31_ffs_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class PaymentDTO {
    int id;
    String paymentCode;
    String userId;
    double money;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    LocalDateTime dateRequest;
    Boolean status;
}
