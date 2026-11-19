package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.exception.ResourceNotFoundException;
import com.nibm.echannelling.echannelingapplication.mapper.DoctorMapper;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final UserRepository userRepository;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.userRepository = userRepository;
    }

    public DoctorDTO getCurrentDoctor() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Doctor doctor = doctorRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with email: " + email));

        return doctorMapper.toDto(doctor);
    }

    public List<DoctorDTO> getAllDoctors() {
        return doctorMapper.toDtoList(doctorRepository.findAll());
    }

    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        return doctorMapper.toDto(doctor);
    }

    public void updateSchedule(Long id, String availability) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        doctor.setAvailability(availability);
        doctorRepository.save(doctor);
    }

    public List<DoctorDTO> getDoctorsBySpecialty(String specialty) {
        return doctorMapper.toDtoList(doctorRepository.findBySpecialty(specialty));
    }
    public DoctorDTO updateProfile(DoctorDTO profileData) {
        Doctor doctor = getCurrentDoctorEntity();

        // MapStruct will only update non-null fields
        doctorMapper.updateEntityFromDto(profileData, doctor);

        // Handle email separately (it lives on User)
        if (profileData.getEmail() != null && !profileData.getEmail().isBlank()) {
            User user = doctor.getUser();
            if (!user.getEmail().equalsIgnoreCase(profileData.getEmail())
                    && userRepository.existsByEmail(profileData.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(profileData.getEmail());
        }

        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toDto(saved);
    }

    private Doctor getCurrentDoctorEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return doctorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }
}