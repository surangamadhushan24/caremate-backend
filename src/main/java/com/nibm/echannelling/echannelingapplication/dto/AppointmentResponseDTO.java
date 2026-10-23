package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDTO {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private LocalDateTime date;
    private String status;
    private String meetingLink;
    private String symptoms;
}