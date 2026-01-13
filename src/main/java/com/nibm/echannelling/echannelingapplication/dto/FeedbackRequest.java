package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long patientId;
    private Long doctorId;
    private int rating;
    private String comment;
}