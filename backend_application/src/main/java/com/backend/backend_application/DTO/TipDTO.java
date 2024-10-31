package com.backend.backend_application.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipDTO {

    @NotBlank(message = "La Cible ne peut pas être vide.")
    private String Cible;

}
