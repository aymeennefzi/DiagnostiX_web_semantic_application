package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.TipDTO;
import com.backend.backend_application.Services.ServiceImpl.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tips")
@CrossOrigin(origins = "*")
public class TipController {

    @Autowired
    private TipService tipService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> createTip(@RequestBody TipDTO tipDTO) {
        return tipService.addTip(tipDTO);
    }

    @GetMapping("/{cible}")
    public ResponseEntity<?> getTipByCible(@PathVariable String cible) {
        Map<String, Object> tip = tipService.getTipByCible(cible);
        if (tip != null) {
            return ResponseEntity.ok(tip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTips() {
        List<Map<String, Object>> tips = tipService.getAllTips();
        return ResponseEntity.ok(tips);
    }

    @PutMapping("/{cible}")
    public ResponseEntity<Map<String, Object>> updateTip(
            @PathVariable String cible,
            @RequestBody TipDTO tipDTO) {
        return tipService.updateTip(cible, tipDTO);
    }

    @DeleteMapping("/{cible}")
    public ResponseEntity<Map<String, Object>> deleteTip(@PathVariable String cible) {
        return tipService.deleteTip(cible);
    }
}