package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Fee;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ServiceResponse {
    List<ServiceDto> services;
    Set<Benefit> benefits;
    List<Fee> fees;
}
