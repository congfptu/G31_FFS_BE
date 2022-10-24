package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.List;
@Data
public class ReportDTOResponse {
    List<ReportDTO> reports;
    int totalPages;
    int pageIndex;
}
