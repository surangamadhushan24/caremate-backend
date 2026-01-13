package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.PrescriptionDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.entity.Prescription;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.PrescriptionRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    public void createPrescription(PrescriptionDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setMedication(dto.getMedication());
        prescription.setDosage(dto.getDosage());
        prescription.setFrequency(dto.getFrequency());
        prescription.setDuration(dto.getDuration());
        prescription.setRefills(dto.getRefills());
        prescription.setInstructions(dto.getInstructions());
        prescription.setNotes(dto.getNotes());
        prescription.setCreatedAt(LocalDateTime.now());
        prescriptionRepository.save(prescription);
    }

    public List<Prescription> getPrescriptionsForAppointment(Long appointmentId) {
        return prescriptionRepository.findByAppointmentId(appointmentId);
    }

    public List<Prescription> getPrescriptionsForDoctor(String doctorEmail) {
        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return prescriptionRepository.findByAppointmentDoctorId(doctorUser.getId());
    }

    public List<Prescription> getPrescriptionsForPatient(String patientEmail) {
        User patientUser = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return prescriptionRepository.findByAppointmentPatientUserId(patientUser.getId());
    }
}