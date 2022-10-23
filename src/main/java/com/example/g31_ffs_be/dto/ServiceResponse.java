package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.List;
@Data
public class ServiceResponse {
    List<ServiceDto> services;
    int totalPages;
    int pageIndex;
}
