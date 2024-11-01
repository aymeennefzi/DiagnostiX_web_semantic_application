package com.backend.backend_application.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipDTO {

    private String titre;
    private String cible;
    private String description;
}
