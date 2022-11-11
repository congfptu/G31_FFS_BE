package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Benefit;
import com.example.g31_ffs_be.model.Fee;
import lombok.Data;

import java.util.List;
@Data
public class ServiceResponse {
    List<ServiceDto> services;
    List<Benefit> benefits;
    List<Fee> fees;
}
