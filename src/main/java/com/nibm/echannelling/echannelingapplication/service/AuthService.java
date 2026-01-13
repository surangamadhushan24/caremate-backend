package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.config.JwtUtils;
import com.nibm.echannelling.echannelingapplication.dto.AuthRequest;
import com.nibm.echannelling.echannelingapplication.dto.AuthResponse;
import com.nibm.echannelling.echannelingapplication.dto.ChangePasswordRequest;
import com.nibm.echannelling.echannelingapplication.dto.RegisterRequest;
import com.nibm.echannelling.echannelingapplication.dto.UserDTO;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.entity.Patient;
import com.nibm.echannelling.echannelingapplication.entity.Role;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.repository.DoctorRepository;
import com.nibm.echannelling.echannelingapplication.repository.PatientRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PatientRepository patientRepository,
                       DoctorRepository doctorRepository, PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateToken(user);
            String name = null;
            if (user.getRole() == Role.PATIENT) {
                Patient patient = patientRepository.findByUser(user)
                        .orElseThrow(() -> new RuntimeException("Patient not found"));
                name = patient.getName();
            } else if (user.getRole() == Role.DOCTOR) {
                Doctor doctor = doctorRepository.findByUser(user)
                        .orElseThrow(() -> new RuntimeException("Doctor not found"));
                name = doctor.getName();
            } else {
                throw new RuntimeException("Unsupported role");
            }
            UserDTO userDTO = new UserDTO(name, user.getRole().name(), user.getEmail());
            return new AuthResponse(token, userDTO);
        }
        throw new RuntimeException("Invalid credentials");
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole()));
        userRepository.save(user);

        if (request.getRole().equals("PATIENT")) {
            Patient patient = new Patient();
            patient.setUser(user);
            patient.setName(request.getName());
            patient.setAge(request.getAge());
            patient.setGender(request.getGender());
            patient.setSymptoms(request.getSymptoms());
            patientRepository.save(patient);
        } else if (request.getRole().equals("DOCTOR")) {
            Doctor doctor = new Doctor();
            doctor.setUser(user);
            doctor.setName(request.getName());
            doctor.setSpecialty(request.getSpecialty());
            doctor.setAvailability(request.getAvailability());
            doctor.setVerified(false);
            doctorRepository.save(doctor);
        }
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String currentPassword = request.getCurrentPassword() != null ? request.getCurrentPassword().trim() : "";
        String newPassword = request.getNewPassword() != null ? request.getNewPassword().trim() : "";
        String confirmPassword = request.getConfirmPassword() != null ? request.getConfirmPassword().trim() : "";

        System.out.println("Attempting password change for email: " + email);
        System.out.println("Provided current password: [HIDDEN]");
        System.out.println("Stored password hash: " + user.getPassword());

        if (currentPassword.isEmpty()) {
            System.out.println("Current password is empty");
            throw new RuntimeException("Current password cannot be empty");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            System.out.println("Password match failed");
            throw new RuntimeException("Current password is incorrect. Please verify your password or try logging in again.");
        }

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("New passwords do not match");
            throw new RuntimeException("New passwords do not match");
        }

        if (newPassword.length() < 8) {
            System.out.println("New password is too short");
            throw new RuntimeException("New password must be at least 8 characters long");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        System.out.println("Password changed successfully for email: " + email);
    }
}