package com.nibm.echannelling.echannelingapplication.controller;


import com.nibm.echannelling.echannelingapplication.service.AdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(adminService.getAllDoctors());
    }

    @PostMapping("/doctors/verify")
    public ResponseEntity<String> verifyDoctor(@RequestParam String email) {
        adminService.verifyDoctor(email);
        return ResponseEntity.ok("Doctor verified successfully");
    }
}