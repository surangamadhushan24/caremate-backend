package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.ApiResponse;
import com.nibm.echannelling.echannelingapplication.dto.DoctorDTO;
import com.nibm.echannelling.echannelingapplication.service.DoctorService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ApiResponse<DoctorDTO>> getCurrentDoctor() {
        DoctorDTO currentDoctor = doctorService.getCurrentDoctor();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Doctor details fetched successfully",currentDoctor));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getAllDoctors() {
        List<DoctorDTO> allDoctors = doctorService.getAllDoctors();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"All Doctor details fetched successfully",allDoctors));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DoctorDTO>> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctorById = doctorService.getDoctorById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Doctor details fetched by id successfully",doctorById));

    }

    //TODO return updated schedule
    @PostMapping("/{id}/schedule")
    public ResponseEntity<ApiResponse<Void>> updateSchedule(@PathVariable Long id, @RequestBody String availability) {
        doctorService.updateSchedule(id, availability);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Update Schedule successfully",null));


    }

    @GetMapping(params = "specialty")
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getDoctorsBySpecialty(@RequestParam String specialty) {
        List<DoctorDTO> doctorsBySpecialty = doctorService.getDoctorsBySpecialty(specialty);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Doctor details fetched by specialty successfully",doctorsBySpecialty));

    }
}