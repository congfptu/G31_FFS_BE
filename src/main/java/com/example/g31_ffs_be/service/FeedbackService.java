package com.example.g31_ffs_be.service;

import com.example.g31_ffs_be.dto.FeedbackDTOResponse;

public interface FeedbackService {
    FeedbackDTOResponse getFeedbackByFromUser(int pageNumber, int pageSize,String toId);
}
