package com.nibm.echannelling.echannelingapplication.service;


import com.nibm.echannelling.echannelingapplication.dto.FeedbackDTO;
import com.nibm.echannelling.echannelingapplication.entity.Feedback;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.FeedbackRepository;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.feedbackRepository = feedbackRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public void createFeedback(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setPatient(patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Patient not found")));
        feedback.setDoctor(doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        feedback.setRating(dto.getRating());
        feedback.setComment(dto.getComment());
        feedback.setTimestamp(LocalDateTime.now());
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbackForDoctor(Long doctorId) {
        return feedbackRepository.findByDoctorId(doctorId);
    }
}