package com.example.g31_ffs_be.dto;

import com.example.g31_ffs_be.model.Career;
import lombok.Data;

import java.util.List;
@Data
public class PaymentDTOResponse {
    private List<PaymentDTO> payments;
    private int pageIndex;
    private int totalPages;
}
