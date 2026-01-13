
package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.VideoCallRequest;
import com.nibm.echannelling.echannelingapplication.dto.VideoCallResponse;
import com.nibm.echannelling.echannelingapplication.service.VideoCallService;
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
    public ResponseEntity<VideoCallResponse> createVideoCall(@RequestBody VideoCallRequest request) {
        VideoCallResponse response = videoCallService.createVideoCall(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<VideoCallResponse> getVideoCallByAppointmentId(@PathVariable Long appointmentId) {
        VideoCallResponse response = videoCallService.getVideoCallByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{videoCallId}/status")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public ResponseEntity<Void> updateVideoCallStatus(@PathVariable Long videoCallId, @RequestBody String status) {
        videoCallService.updateVideoCallStatus(videoCallId, status);
        return ResponseEntity.ok().build();
    }
}