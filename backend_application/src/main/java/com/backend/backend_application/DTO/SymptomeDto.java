package DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;


@Data
@AllArgsConstructor
public class SymptomeDto {

    @NotBlank(message = "La description ne peut pas être vide.")
    private String Description;
    @NotBlank(message = "Le nom ne peut pas être vide.")

    private String Nom;

}

