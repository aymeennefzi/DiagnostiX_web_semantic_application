package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.AllergyDTO;
import com.backend.backend_application.Services.ServiceImpl.AllergyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class AllergyController {
    @Autowired
    private AllergyService allergyService;

    @GetMapping("/allergy/{allergyName}")
    public ResponseEntity<Map<String, Object>> getAllergyInfo(@PathVariable("allergyName") String allergyName) {
        Map<String, Object> result = allergyService.getAllergyInfo(allergyName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addAllergy")
    public ResponseEntity<Map<String, Object>> addAllergy(@RequestBody AllergyDTO allergyDTO) {
        return allergyService.addAllergy(allergyDTO);
    }

    @DeleteMapping("/deleteAllergy/{name}")
    public ResponseEntity<Map<String, Object>> deleteAllergy(@PathVariable String name) {
        return allergyService.deleteAllergy(name);
    }

    @GetMapping("/allergies")
    public List<Map<String, String>> getAllAllergies() {
        return allergyService.getAllAllergies();
    }

    @PutMapping("/updateAllergy/{name}")
    public ResponseEntity<Map<String, Object>> updateAllergy(@PathVariable String name, @RequestBody AllergyDTO allergyDTO) {
        Map<String, Object> response = allergyService.updateAllergyByName(name, allergyDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/allergyInfo/{name}")
    public ResponseEntity<Map<String, Object>> getAllergyByName(@PathVariable String name) {
        Map<String, Object> allergyMap = allergyService.getAllergyByName(name);
        if (allergyMap != null) {
            return ResponseEntity.ok(allergyMap);
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "Allergy not found"));
        }
    }
}
