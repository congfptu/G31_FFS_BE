package com.example.g31_ffs_be.service.impl;

import com.example.g31_ffs_be.dto.APIResponse;
import com.example.g31_ffs_be.dto.FeedbackDTO;
import com.example.g31_ffs_be.dto.FeedbackDTOResponse;
import com.example.g31_ffs_be.model.Feedback;
import com.example.g31_ffs_be.model.RequestPayment;
import com.example.g31_ffs_be.repository.FeedbackRepository;
import com.example.g31_ffs_be.repository.PaymentRepository;
import com.example.g31_ffs_be.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public FeedbackDTOResponse getFeedbackByFromUser(int pageNumber, int pageSize, String fromId) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Feedback> feedbackDTOPage = feedbackRepository.getFeedbacksByFromId(fromId, pageable);
        List<Feedback> feedbackDTOS = feedbackDTOPage.getContent();
        List<FeedbackDTO> feedbackDTOS1 = new ArrayList<>();
        double star = 0;
        for (Feedback f:feedbackDTOS) {
            FeedbackDTO feedbackDTO= new FeedbackDTO();
            feedbackDTO.setId(f.getId());
            feedbackDTO.setDate(f.getDate());
            feedbackDTO.setFromUserId(f.getFrom().getId());
            feedbackDTO.setContent(f.getContent());
            star += f.getStar();
            feedbackDTO.setStar(f.getStar());
            feedbackDTO.setFromAvatar(f.getFrom().getAvatar());
            feedbackDTO.setFromFullName(f.getFrom().getFullName());
            feedbackDTOS1.add(feedbackDTO);
        }
        star = star / feedbackDTOS.size();
        FeedbackDTOResponse feedbackDTOResponse = new FeedbackDTOResponse();
        feedbackDTOResponse.setTotalFeedback(feedbackDTOS.size());
        feedbackDTOResponse.setComments(feedbackDTOS1);
        feedbackDTOResponse.setPageIndex(pageNumber + 1);
        feedbackDTOResponse.setStarAverage(star);
        feedbackDTOResponse.setTotalPages(feedbackDTOPage.getTotalPages());
        return feedbackDTOResponse;
    }
}
