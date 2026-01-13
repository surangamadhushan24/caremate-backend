package com.nibm.echannelling.echannelingapplication.controller;

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
import com.nibm.echannelling.echannelingapplication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthController(AuthService authService, JwtUtils jwtUtils, UserRepository userRepository,
                          PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        System.out.println("Register request received for email: " + request.getEmail());
        try {
            authService.register(request);
            System.out.println("User registered successfully: " + request.getEmail());
            return ResponseEntity.ok("Registration successful");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        System.out.println("Login request received for email: " + request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            System.out.println("Login successful for email: " + request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Get current user request received");
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Invalid authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                System.out.println("Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            String email = jwtUtils.getEmailFromToken(token);
            System.out.println("Fetching user for email: " + email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

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
                System.out.println("Unsupported role: " + user.getRole());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            UserDTO userDTO = new UserDTO(name, user.getRole().name(), user.getEmail());
            System.out.println("User details fetched successfully for email: " + email);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            System.out.println("Error fetching current user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody ChangePasswordRequest request) {
        System.out.println("Change password request received");
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Invalid authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authorization header");
            }
            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                System.out.println("Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String email = jwtUtils.getEmailFromToken(token);
            authService.changePassword(email, request);
            System.out.println("Password changed successfully for email: " + email);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            System.out.println("Change password failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}