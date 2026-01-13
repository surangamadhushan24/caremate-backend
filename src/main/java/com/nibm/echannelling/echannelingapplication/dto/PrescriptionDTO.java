package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;

@Data
public class PrescriptionDTO {
    private Long appointmentId;
    private String medication;
    private String dosage;
    private String frequency;
    private String duration;
    private Integer refills;
    private String instructions;
    private String notes;
}