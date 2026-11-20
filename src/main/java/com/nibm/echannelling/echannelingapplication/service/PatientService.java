package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.entity.Patient;
import com.nibm.echannelling.echannelingapplication.exception.ConflictException;
import com.nibm.echannelling.echannelingapplication.exception.ResourceNotFoundException;
import com.nibm.echannelling.echannelingapplication.mapper.PatientMapper;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.userRepository = userRepository;
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

        if (request.getEmail() != null && !request.getEmail().equals(patient.getUser().getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email already in use");
            }
            patient.getUser().setEmail(request.getEmail());
            userRepository.save(patient.getUser());
        }

        patientRepository.save(patient);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }
}