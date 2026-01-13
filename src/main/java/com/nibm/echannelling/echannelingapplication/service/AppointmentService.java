package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment createAppointment(AppointmentDTO dto) {
        try {
            Appointment appointment = new Appointment();
            // Updated formatter to handle seconds and 'Z'
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            appointment.setDate(LocalDateTime.parse(dto.getDate(), formatter));
            appointment.setDoctor(doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found")));
            appointment.setPatient(patientRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new RuntimeException("Patient not found")));
            appointment.setStatus("PENDING");
            appointment.setSymptoms(dto.getSymptoms());
            appointment.setMeetingLink("https://meet.jitsi.si/appointment_" + System.currentTimeMillis());
            return appointmentRepository.save(appointment);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format: " + dto.getDate() + ". Expected format: yyyy-MM-dd'T'HH:mm:ss'Z'");
        }
    }

    public List<Appointment> getMyAppointments() {
        return appointmentRepository.findByPatientEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public List<Appointment> getDoctorAppointments() {
        return appointmentRepository.findByDoctorEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void updateStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }
}