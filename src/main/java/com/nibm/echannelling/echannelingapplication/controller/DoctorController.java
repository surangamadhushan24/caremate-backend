package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/me")
    public DoctorDTO getCurrentDoctor() {
        return doctorService.getCurrentDoctor();
    }

    @GetMapping
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    public DoctorDTO getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @PostMapping("/{id}/schedule")
    public void updateSchedule(@PathVariable Long id, @RequestBody String availability) {
        doctorService.updateSchedule(id, availability);
    }

    @GetMapping(params = "specialty")
    public List<DoctorDTO> getDoctorsBySpecialty(@RequestParam String specialty) {
        return doctorService.getDoctorsBySpecialty(specialty);
    }
}