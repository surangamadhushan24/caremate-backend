package com.nibm.echannelling.echannelingapplication.dto;



public class ChatResponse {
    private String response;
    private String suggestedSpecialty;
    private Long[] recommendedDoctorIds;

    // Constructors, getters, setters
    public ChatResponse() {}

    public ChatResponse(String response, String suggestedSpecialty, Long[] recommendedDoctorIds) {
        this.response = response;
        this.suggestedSpecialty = suggestedSpecialty;
        this.recommendedDoctorIds = recommendedDoctorIds;
    }

    // Getters and setters
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getSuggestedSpecialty() {
        return suggestedSpecialty;
    }

    public void setSuggestedSpecialty(String suggestedSpecialty) {
        this.suggestedSpecialty = suggestedSpecialty;
    }

    public Long[] getRecommendedDoctorIds() {
        return recommendedDoctorIds;
    }

    public void setRecommendedDoctorIds(Long[] recommendedDoctorIds) {
        this.recommendedDoctorIds = recommendedDoctorIds;
    }
}
