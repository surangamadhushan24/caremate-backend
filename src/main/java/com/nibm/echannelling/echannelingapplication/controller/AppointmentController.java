package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.ApiResponse;
import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.dto.AppointmentResponseDTO;
import com.nibm.echannelling.echannelingapplication.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentResponseDTO>> createAppointment(
            @Valid @RequestBody AppointmentDTO dto) {

        AppointmentResponseDTO created = appointmentService.createAppointment(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getMyAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getMyAppointments();
        return ResponseEntity.ok(
                ApiResponse.success("Appointments retrieved successfully", appointments));
    }

    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<List<AppointmentResponseDTO>>> getDoctorAppointments() {
        List<AppointmentResponseDTO> appointments = appointmentService.getDoctorAppointments();
        return ResponseEntity.ok(
                ApiResponse.success("Doctor appointments retrieved successfully", appointments));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long id,
            @RequestBody String status) {

        appointmentService.updateStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.success("Appointment status updated successfully", null));
    }
}