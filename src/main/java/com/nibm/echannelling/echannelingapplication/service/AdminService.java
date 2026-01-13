package com.nibm.echannelling.echannelingapplication.service;


import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final DoctorRepository doctorRepository;

    public AdminService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public void verifyDoctor(String email) {
        Doctor doctor = doctorRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        doctor.setVerified(true);
        doctorRepository.save(doctor);
    }
}