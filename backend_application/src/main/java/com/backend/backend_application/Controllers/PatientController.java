package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.PatientDTO;
import com.backend.backend_application.Services.ServiceImpl.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients") // Base path for all patient-related endpoints
@CrossOrigin(origins = "http://localhost:4200") // Allow only your frontend domain
public class PatientController {

    @Autowired
    private PatientService patientService;

    // Endpoint to add a new patient
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.addPatient(patientDTO);
    }

    // Endpoint to retrieve all patients
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getAllPatients() {
        List<Map<String, String>> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    // Endpoint to retrieve patient info by name
    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getPatientInfo(@PathVariable String name) {
        Map<String, Object> patientInfo = patientService.getPatientInfo(name);
        return ResponseEntity.ok(patientInfo);
    }

    // Endpoint to update patient by name
    @PutMapping("/{name}")
    public ResponseEntity<Map<String, Object>> updatePatientByName(@PathVariable String name, @RequestBody PatientDTO patientDTO) {
        Map<String, Object> response = patientService.updatePatientByName(name, patientDTO);
        return ResponseEntity.ok(response);
    }

    // Endpoint to delete a patient by name
    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, Object>> deletePatient(@PathVariable String name) {
        ResponseEntity<Map<String, Object>> response = patientService.deletePatient(name);
        return response;
    }
}
