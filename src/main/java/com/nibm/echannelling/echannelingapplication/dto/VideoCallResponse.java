package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VideoCallResponse {
    private Long videoCallId;
    private Long appointmentId;
    private String roomId;
    private String status;
    private LocalDateTime createdAt;
}