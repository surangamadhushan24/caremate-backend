package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.FeedbackDTO;
import com.nibm.echannelling.echannelingapplication.entity.Feedback;
import com.nibm.echannelling.echannelingapplication.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public void createFeedback(@RequestBody FeedbackDTO dto) {
        feedbackService.createFeedback(dto);
    }

    @GetMapping("/doctor/{id}")
    public List<Feedback> getFeedbackForDoctor(@PathVariable Long id) {
        return feedbackService.getFeedbackForDoctor(id);
    }
}
