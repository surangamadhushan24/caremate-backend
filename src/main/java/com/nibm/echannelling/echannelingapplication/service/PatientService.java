package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.entity.Patient;
import com.nibm.echannelling.echannelingapplication.exception.ResourceNotFoundException;
import com.nibm.echannelling.echannelingapplication.mapper.PatientMapper;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public PatientDTO getCurrentPatient() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));

        return patientMapper.toDto(patient);
    }

    public void updatePatient(UpdatePatientRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with email: " + email));

        patientMapper.updateFromRequest(request, patient);

        patientRepository.save(patient);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }
}