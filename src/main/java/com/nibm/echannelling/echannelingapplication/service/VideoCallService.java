package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.VideoCallRequest;
import com.nibm.echannelling.echannelingapplication.dto.VideoCallResponse;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.entity.VideoCall;
import com.nibm.echannelling.echannelingapplication.exception.CustomException;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.VideoCallRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VideoCallService {

    private final VideoCallRepository videoCallRepository;
    private final AppointmentRepository appointmentRepository;

    public VideoCallService(VideoCallRepository videoCallRepository, AppointmentRepository appointmentRepository) {
        this.videoCallRepository = videoCallRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public VideoCallResponse createVideoCall(VideoCallRequest request) {
        // Validate request
        if (request.getAppointmentId() == null) {
            throw new CustomException("Appointment ID is required");
        }

        // Find the appointment by ID
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new CustomException("Appointment not found with ID: " + request.getAppointmentId()));

        // Generate a unique room ID for Jitsi Meet
        String roomId = "eChannelling_" + UUID.randomUUID().toString().replace("-", "");

        // Create VideoCall entity
        VideoCall videoCall = new VideoCall();
        videoCall.setAppointmentId(request.getAppointmentId());
        videoCall.setRoomId(roomId);
        videoCall.setStatus("PENDING");
        videoCall.setCreatedAt(LocalDateTime.now());

        // Save video call details
        videoCall = videoCallRepository.save(videoCall);

        // Update appointment with room ID (as meeting_link)
        appointment.setMeetingLink(roomId);
        appointmentRepository.save(appointment);

        // Prepare response
        VideoCallResponse response = new VideoCallResponse();
        response.setVideoCallId(videoCall.getId());
        response.setAppointmentId(videoCall.getAppointmentId());
        response.setRoomId(videoCall.getRoomId());
        response.setStatus(videoCall.getStatus());
        response.setCreatedAt(videoCall.getCreatedAt());

        return response;
    }

    public VideoCallResponse getVideoCallByAppointmentId(Long appointmentId) {
        VideoCall videoCall = videoCallRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new CustomException("Video call not found for appointment ID: " + appointmentId));

        VideoCallResponse response = new VideoCallResponse();
        response.setVideoCallId(videoCall.getId());
        response.setAppointmentId(videoCall.getAppointmentId());
        response.setRoomId(videoCall.getRoomId());
        response.setStatus(videoCall.getStatus());
        response.setCreatedAt(videoCall.getCreatedAt());

        return response;
    }

    public void updateVideoCallStatus(Long videoCallId, String status) {
        VideoCall videoCall = videoCallRepository.findById(videoCallId)
                .orElseThrow(() -> new CustomException("Video call not found with ID: " + videoCallId));

        videoCall.setStatus(status);
        videoCallRepository.save(videoCall);
    }
}