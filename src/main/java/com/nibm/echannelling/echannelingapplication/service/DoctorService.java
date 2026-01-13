package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorDTO getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setName(doctor.getName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setAvailability(doctor.getAvailability());
        dto.setVerified(doctor.getVerified());
        return dto;
    }

    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctor -> {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doctor.getId());
            dto.setEmail(doctor.getUser().getEmail());
            dto.setName(doctor.getName());
            dto.setSpecialty(doctor.getSpecialty());
            dto.setAvailability(doctor.getAvailability());
            dto.setVerified(doctor.getVerified());
            return dto;
        }).collect(Collectors.toList());
    }

    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setEmail(doctor.getUser().getEmail());
        dto.setName(doctor.getName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setAvailability(doctor.getAvailability());
        dto.setVerified(doctor.getVerified());
        return dto;
    }

    public void updateSchedule(Long id, String availability) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setAvailability(availability);
        doctorRepository.save(doctor);
    }

    public List<DoctorDTO> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty).stream().map(doctor -> {
            DoctorDTO dto = new DoctorDTO();
            dto.setId(doctor.getId());
            dto.setEmail(doctor.getUser().getEmail());
            dto.setName(doctor.getName());
            dto.setSpecialty(doctor.getSpecialty());
            dto.setAvailability(doctor.getAvailability());
            dto.setVerified(doctor.getVerified());
            return dto;
        }).collect(Collectors.toList());
    }
}