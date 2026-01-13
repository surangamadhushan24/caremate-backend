package com.nibm.echannelling.echannelingapplication.service;


import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.entity.Patient;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public PatientDTO getCurrentPatient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setEmail(patient.getUser().getEmail());
        dto.setName(patient.getName());
        dto.setAge(patient.getAge());
        dto.setGender(patient.getGender());
        dto.setSymptoms(patient.getSymptoms());
        return dto;
    }

    public void updatePatient(UpdatePatientRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setName(request.getName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setSymptoms(request.getSymptoms());
        patientRepository.save(patient);
    }

    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patient -> {
            PatientDTO dto = new PatientDTO();
            dto.setId(patient.getId());
            dto.setEmail(patient.getUser().getEmail());
            dto.setName(patient.getName());
            dto.setAge(patient.getAge());
            dto.setGender(patient.getGender());
            dto.setSymptoms(patient.getSymptoms());
            return dto;
        }).collect(Collectors.toList());
    }
}