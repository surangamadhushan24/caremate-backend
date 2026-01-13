package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import com.nibm.echannelling.echannelingapplication.repository.PrescriptionRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;

    public AnalyticsService(DoctorRepository doctorRepository,
                            AppointmentRepository appointmentRepository,
                            PatientRepository patientRepository,
                            PrescriptionRepository prescriptionRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public Map<String, Object> getDoctorAnalytics() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Appointment> appointments = appointmentRepository.findByDoctorEmail(email);
        long totalAppointments = appointments.size();
        long pendingAppointments = appointments.stream()
                .filter(a -> a.getStatus().equals("PENDING"))
                .count();
        long completedAppointments = appointments.stream()
                .filter(a -> a.getStatus().equals("COMPLETED"))
                .count();

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalAppointments", totalAppointments);
        analytics.put("pendingAppointments", pendingAppointments);
        analytics.put("completedAppointments", completedAppointments);
        return analytics;
    }

    public Map<String, Object> getSystemAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("doctors", doctorRepository.count());
        analytics.put("patients", patientRepository.count());
        analytics.put("appointments", appointmentRepository.count());
        analytics.put("prescriptions", prescriptionRepository.count());
        return analytics;
    }
}