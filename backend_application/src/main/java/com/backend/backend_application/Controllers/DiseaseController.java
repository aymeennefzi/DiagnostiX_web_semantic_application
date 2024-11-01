package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.DiseaseDTO;
import com.backend.backend_application.Services.ServiceImpl.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;

    @GetMapping("/disease/{diseaseName}")
    public ResponseEntity<Map<String, Object>> getDiseaseInfo(@PathVariable("diseaseName") String diseaseName) {
        Map<String, Object> result = diseaseService.getDiseaseInfo(diseaseName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addDisease(@RequestBody DiseaseDTO diseaseDTO) {
        return diseaseService.addDisease(diseaseDTO);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<Map<String, Object>> deleteDisease(@PathVariable String name) {
        return diseaseService.deleteDisease(name);
    }

    @GetMapping("/diseases")
    public List<Map<String, String>> getAllDiseases() {
        return diseaseService.getAllDiseases();
    }

    @PutMapping("/update/{name}")
    public ResponseEntity<Map<String, Object>> updateDisease(@PathVariable String name, @RequestBody DiseaseDTO diseaseDTO) {
        Map<String, Object> response = diseaseService.updateDiseaseByName(name, diseaseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getDiseaseByName(@PathVariable String name) {
        Map<String, Object> diseaseMap = diseaseService.getDiseaseByName(name);
        if (diseaseMap != null) {
            return ResponseEntity.ok(diseaseMap);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Disease not found"));
        }
    }
    @GetMapping("/disease/check/{diseaseName}")
    public Map<String, Object> checkDiseaseNameUnique(@PathVariable String diseaseName) {
        return diseaseService.isDiseaseNameUnique(diseaseName);
    }
}
