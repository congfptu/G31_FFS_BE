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
    String code;
    String user_id;
    double money;
    Instant dateRequest;
    Instant dateApprove;
    Integer state;
    String description;

}
