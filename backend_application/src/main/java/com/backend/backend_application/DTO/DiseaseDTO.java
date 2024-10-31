package com.backend.backend_application.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class DiseaseDTO {
    private String name;
    private String description;
    private List<String> treatments;
    private List<String> symptoms;

}
