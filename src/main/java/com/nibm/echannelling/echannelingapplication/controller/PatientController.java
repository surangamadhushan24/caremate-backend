package com.nibm.echannelling.echannelingapplication.controller;


import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/me")
    public PatientDTO getCurrentPatient() {
        return patientService.getCurrentPatient();
    }

    @PutMapping("/update")
    public void updatePatient(@RequestBody UpdatePatientRequest request) {
        patientService.updatePatient(request);
    }
}