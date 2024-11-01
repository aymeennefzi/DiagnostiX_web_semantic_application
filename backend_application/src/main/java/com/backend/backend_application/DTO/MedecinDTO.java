package com.backend.backend_application.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedecinDTO {
    private String nom;
    private String specialite;
    private String localisation;
}
