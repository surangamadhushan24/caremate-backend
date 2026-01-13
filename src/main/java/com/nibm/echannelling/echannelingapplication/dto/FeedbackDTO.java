package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;



public class FeedbackDTO {
    private Long doctorId;
    private Integer rating;
    private String comment;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
