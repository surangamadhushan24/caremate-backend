package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.exception.ResourceNotFoundException;
import com.nibm.echannelling.echannelingapplication.mapper.DoctorMapper;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public DoctorDTO updateProfile(DoctorDTO profileData) {

        System.out.println("========== UPDATE PROFILE ==========");
        System.out.println("Received name: " + profileData.getName());
        System.out.println("Received phone: " + profileData.getPhone());
        System.out.println("Received specialty: " + profileData.getSpecialty());
        System.out.println("Received experience: " + profileData.getYearsOfExperience());
        System.out.println("Received bio: " + profileData.getBio());
        System.out.println("Received education: " + profileData.getEducation());
        System.out.println("Received email: " + profileData.getEmail());

        Doctor doctor = getCurrentDoctorEntity();

        System.out.println("Before mapping: " + doctor.getName());

        doctorMapper.updateEntityFromDto(profileData, doctor);

        System.out.println("========== AFTER MAPPING ==========");
        System.out.println("Name: " + doctor.getName());
        System.out.println("Phone: " + doctor.getPhone());
        System.out.println("Specialty: " + doctor.getSpecialty());
        System.out.println("Experience: " + doctor.getYearsOfExperience());
        System.out.println("Bio: " + doctor.getBio());
        System.out.println("Education: " + doctor.getEducation());

        if (profileData.getEmail() != null &&
                !profileData.getEmail().isBlank()) {

            User user = doctor.getUser();

            if (!user.getEmail().equalsIgnoreCase(profileData.getEmail())
                    && userRepository.existsByEmail(profileData.getEmail())) {
                throw new RuntimeException("Email already in use");
            }

            user.setEmail(profileData.getEmail());
        }

        Doctor saved = doctorRepository.save(doctor);

        System.out.println("========== SAVED PROFILE ==========");
        System.out.println("Saved name: " + saved.getName());

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