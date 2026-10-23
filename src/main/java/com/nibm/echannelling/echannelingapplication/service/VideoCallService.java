package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.VideoCallRequest;
import com.nibm.echannelling.echannelingapplication.dto.VideoCallResponse;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.entity.VideoCall;
import com.nibm.echannelling.echannelingapplication.exception.CustomException;
import com.nibm.echannelling.echannelingapplication.mapper.VideoCallMapper;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.VideoCallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class VideoCallService {

    private final VideoCallRepository videoCallRepository;
    private final AppointmentRepository appointmentRepository;
    private final VideoCallMapper videoCallMapper;

    public VideoCallService(VideoCallRepository videoCallRepository,
                            AppointmentRepository appointmentRepository,
                            VideoCallMapper videoCallMapper) {
        this.videoCallRepository = videoCallRepository;
        this.appointmentRepository = appointmentRepository;
        this.videoCallMapper = videoCallMapper;
    }

    public VideoCallResponse createVideoCall(VideoCallRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new CustomException(
                        "Appointment not found with ID: " + request.getAppointmentId()));

        if (videoCallRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new CustomException("Video call already exists for this appointment");
        }

        String roomId = "eChannelling_" + UUID.randomUUID().toString().replace("-", "");

        VideoCall videoCall = new VideoCall();
        videoCall.setAppointmentId(request.getAppointmentId());
        videoCall.setRoomId(roomId);
        videoCall.setStatus("PENDING");
        videoCall.setCreatedAt(LocalDateTime.now());

        VideoCall saved = videoCallRepository.save(videoCall);

        appointment.setMeetingLink(roomId);
        appointmentRepository.save(appointment);

        return videoCallMapper.toResponse(saved);
    }

    public VideoCallResponse getVideoCallByAppointmentId(Long appointmentId) {
        VideoCall videoCall = videoCallRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new CustomException(
                        "Video call not found for appointment ID: " + appointmentId));

        return videoCallMapper.toResponse(videoCall);
    }

    public void updateVideoCallStatus(Long videoCallId, String status) {
        VideoCall videoCall = videoCallRepository.findById(videoCallId)
                .orElseThrow(() -> new CustomException(
                        "Video call not found with ID: " + videoCallId));

        videoCall.setStatus(status);
        videoCallRepository.save(videoCall);
    }
}