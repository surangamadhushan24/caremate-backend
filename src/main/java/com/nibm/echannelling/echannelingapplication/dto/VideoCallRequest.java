package com.nibm.echannelling.echannelingapplication.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VideoCallRequest {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;
}