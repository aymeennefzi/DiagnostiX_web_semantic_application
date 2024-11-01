package com.backend.backend_application.Services.IServices;

import com.backend.backend_application.DTO.MedecinDTO;

import java.util.Map;

public interface IMedecinService {
    public Map<String, Object> getAllMedecins();
    public String addMedicin(MedecinDTO medicinDTO);
}
