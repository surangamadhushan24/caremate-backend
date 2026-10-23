package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.dto.AppointmentResponseDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.mapper.AppointmentMapper;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository,
                              AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentMapper = appointmentMapper;
    }

    public AppointmentResponseDTO createAppointment(AppointmentDTO dto) {
        Appointment appointment = appointmentMapper.toEntity(dto);

        appointment.setDoctor(doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));

        appointment.setPatient(patientRepository.findByEmail(
                        SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Patient not found")));

        appointment.setStatus("PENDING");
        appointment.setMeetingLink("https://meet.jitsi.si/appointment_" + System.currentTimeMillis());

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponseDto(saved);
    }

    public List<AppointmentResponseDTO> getMyAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentMapper.toResponseDtoList(
                appointmentRepository.findByPatientEmail(email));
    }

    public List<AppointmentResponseDTO> getDoctorAppointments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return appointmentMapper.toResponseDtoList(
                appointmentRepository.findByDoctorEmail(email));
    }

    public void updateStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }
}