package com.nibm.echannelling.echannelingapplication.controller;


import com.nibm.echannelling.echannelingapplication.dto.ApiResponse;
import com.nibm.echannelling.echannelingapplication.dto.PatientDTO;
import com.nibm.echannelling.echannelingapplication.dto.UpdatePatientRequest;
import com.nibm.echannelling.echannelingapplication.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAllPatients() {

        List<PatientDTO> allPatients = patientService.getAllPatients();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"fetch all patients",allPatients));

    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PatientDTO>> getCurrentPatient() {
        PatientDTO currentPatient = patientService.getCurrentPatient();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Patient details fetched successfully",currentPatient));

    }

    //TODO update id based(which patient update)
    //TODO return updated patient <ApiResponse<PatientDTO>>

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updatePatient(@RequestBody UpdatePatientRequest request) {
        patientService.updatePatient(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true,"Update Patient details successfully",null));

    }
}