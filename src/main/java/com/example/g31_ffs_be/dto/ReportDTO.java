package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ReportDTO {
   String createdBy;
   String title;
   String content;
   LocalDateTime createdDate;
}
