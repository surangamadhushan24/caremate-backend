package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.ApiResponse;
import com.nibm.echannelling.echannelingapplication.dto.VideoCallRequest;
import com.nibm.echannelling.echannelingapplication.dto.VideoCallResponse;
import com.nibm.echannelling.echannelingapplication.service.VideoCallService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video-calls")
public class VideoCallController {

    private final VideoCallService videoCallService;

    public VideoCallController(VideoCallService videoCallService) {
        this.videoCallService = videoCallService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<ApiResponse<VideoCallResponse>> createVideoCall(
            @Valid @RequestBody VideoCallRequest request) {

        VideoCallResponse response = videoCallService.createVideoCall(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Video call created successfully", response));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<ApiResponse<VideoCallResponse>> getVideoCallByAppointmentId(
            @PathVariable Long appointmentId) {

        VideoCallResponse response = videoCallService.getVideoCallByAppointmentId(appointmentId);
        return ResponseEntity.ok(
                ApiResponse.success("Video call retrieved successfully", response));
    }

    @PutMapping("/{videoCallId}/status")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<ApiResponse<Void>> updateVideoCallStatus(
            @PathVariable Long videoCallId,
            @RequestBody String status) {

        videoCallService.updateVideoCallStatus(videoCallId, status);
        return ResponseEntity.ok(
                ApiResponse.success("Video call status updated successfully", null));
    }
}