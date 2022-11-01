package com.example.g31_ffs_be.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class FeedbackDTOResponse {
    List<FeedbackDTO> comments;
    Integer totalFeedback;
    Double starAverage;
     Integer pageIndex;
    Integer totalPages;
    Long totalResults;
}
