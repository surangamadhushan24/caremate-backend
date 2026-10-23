package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.exception.ResourceNotFoundException;
import com.nibm.echannelling.echannelingapplication.mapper.DoctorMapper;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
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
}