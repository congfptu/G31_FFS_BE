package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.time.Instant;
@Data
public class ReportDTO {
    Integer id;
   String createdBy;
   String title;
   String detail;
   Instant createdDate;
}
