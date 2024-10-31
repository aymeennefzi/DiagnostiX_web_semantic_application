package com.backend.backend_application.Controllers;

import DTO.SymptomeDto;
import com.backend.backend_application.Services.ServiceImpl.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class SymptomController {
    @Autowired
    private SymptomService symptomService;

    @GetMapping("/symptom/{symptomName}")
    public ResponseEntity<Map<String, Object>> getSymptomInfo(@PathVariable("symptomName") String symptomName) {
        Map<String, Object> result = symptomService.getSymptomByName(symptomName);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Symptom not found"));
        }
    }

    @PostMapping("/addSymptom")
    public ResponseEntity<Map<String, Object>> addSymptom(@RequestBody SymptomeDto symptomDTO) {
        return symptomService.addSymptom(symptomDTO);
    }

    @DeleteMapping("/deleteSymptom/{name}")
    public ResponseEntity<Map<String, Object>> deleteSymptom(@PathVariable String name) {
        return symptomService.deleteSymptom(name);
    }

    @GetMapping("/symptoms")
    public List<Map<String, String>> getAllSymptoms() {
        return symptomService.getAllSymptoms();
    }
}
