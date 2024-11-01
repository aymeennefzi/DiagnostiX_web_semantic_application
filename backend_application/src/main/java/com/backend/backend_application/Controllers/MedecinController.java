package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.MedecinDTO;
import com.backend.backend_application.Services.IServices.IMedecinService;
import com.backend.backend_application.Services.ServiceImpl.MedecinServicesImp;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
    @RequestMapping("/medecins")
public class MedecinController {
    private final MedecinServicesImp medicinService;



    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllMedecins() {
        Map<String, Object> medecins = medicinService.getAllMedecins();
        return ResponseEntity.ok(medecins);
    }

    @PostMapping("/add")
    public String addMedicin(@RequestBody MedecinDTO medicinDTO) {
        return medicinService.addMedicin(medicinDTO);
    }


    @PutMapping("update/{name}")
    public ResponseEntity<Map<String, Object>> updateMedicin(
            @PathVariable String name,
                @RequestBody MedecinDTO medicinDTO) {

        Map<String, Object> response = medicinService.updateMedicinByName(name, medicinDTO);

        if (response.containsKey("error") && (boolean) response.get("error")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteMedicinByName(@PathVariable String name) {
        String response = medicinService.deleteMedicinByName(name);

        if (response.contains("Erreur")) {
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }



    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getMedicinByName(@PathVariable String name) {
        Map<String, Object> response = medicinService.getMedicinByName(name);

        if (response.containsKey("error") && (boolean) response.get("error")) {
            return ResponseEntity.status(500).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
