package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;

//@RestController
//@RequestMapping("/appointments")
//public class AppointmentController {
//    private final AppointmentService appointmentService;
//
//    public AppointmentController(AppointmentService appointmentService) {
//        this.appointmentService = appointmentService;
//    }
//
//    @PostMapping
//    public void createAppointment(@RequestBody AppointmentDTO dto) {
//        appointmentService.createAppointment(dto);
//    }
//
//    @GetMapping
//    public List<Appointment> getMyAppointments() {
//        return appointmentService.getMyAppointments();
//    }
//
//    @GetMapping("/doctor")
//    public List<Appointment> getDoctorAppointments() {
//        return appointmentService.getDoctorAppointments();
//    }
//
//    @PutMapping("/{id}/status")
//    public void updateStatus(@PathVariable Long id, @RequestBody String status) {
//        appointmentService.updateStatus(id, status);
//    }



import com.nibm.echannelling.echannelingapplication.dto.AppointmentDTO;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.service.AppointmentService;
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
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO dto) {
        Appointment appointment = appointmentService.createAppointment(dto);
        return ResponseEntity.ok(appointment); // Return the created appointment
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getMyAppointments() {
        List<Appointment> appointments = appointmentService.getMyAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getDoctorAppointments() {
        List<Appointment> appointments = appointmentService.getDoctorAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody String status) {
        appointmentService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
