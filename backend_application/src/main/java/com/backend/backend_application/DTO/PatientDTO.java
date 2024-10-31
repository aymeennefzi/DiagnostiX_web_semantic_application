package com.backend.backend_application.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PatientDTO {
    private String nom;
    private int age;
    private String sexe;
    private double poids;
    private List<String> antecedentsMedicaux; // Antécédents Médicaux
    private LocalDate dateDiagnostic;
    private List<String> historiqueMedical;
    private int niveauDouleur;
}
