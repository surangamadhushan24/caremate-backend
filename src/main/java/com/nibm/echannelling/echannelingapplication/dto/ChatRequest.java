package com.nibm.echannelling.echannelingapplication.dto;




public class ChatRequest {
    private String symptoms;
    private Long patientId;

    // Constructors, getters, setters
    public ChatRequest() {}

    public ChatRequest(String symptoms, Long patientId) {
        this.symptoms = symptoms;
        this.patientId = patientId;
    }

    // Getters and setters
    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
