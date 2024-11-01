package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.TipDTO;
import com.backend.backend_application.Services.ServiceImpl.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/tips")
public class TipController {
    @Autowired
    private TipService tipService;

    @GetMapping("/{cible}")
    public ResponseEntity<Map<String, Object>> getTipInfo(@PathVariable("cible") String cible) {
        Map<String, Object> result = tipService.getTipByCible(cible);
        if (result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Conseil non trouvé"));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addTip(@RequestBody TipDTO tipDTO) {
        return tipService.addTip(tipDTO);
    }

    @DeleteMapping("/delete/{cible}")
    public ResponseEntity<Map<String, Object>> deleteTip(@PathVariable String cible) {
        return tipService.deleteTip(cible);
    }

    @GetMapping
    public List<Map<String, String>> getAllTips() {
        return tipService.getAllTips();
    }
}