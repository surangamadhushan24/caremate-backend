package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.PrescriptionDTO;
import com.nibm.echannelling.echannelingapplication.entity.Prescription;
import com.nibm.echannelling.echannelingapplication.service.PrescriptionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping
    public void createPrescription(@RequestBody PrescriptionDTO dto) {
        prescriptionService.createPrescription(dto);
    }

    @GetMapping
    public List<Prescription> getPrescriptionsForPatient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return prescriptionService.getPrescriptionsForPatient(email);
    }

    @GetMapping("/appointment")
    public List<Prescription> getPrescriptionsForAppointment(@RequestParam Long appointmentId) {
        return prescriptionService.getPrescriptionsForAppointment(appointmentId);
    }

    @GetMapping("/doctor")
    public List<Prescription> getPrescriptionsForDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return prescriptionService.getPrescriptionsForDoctor(email);
    }
}