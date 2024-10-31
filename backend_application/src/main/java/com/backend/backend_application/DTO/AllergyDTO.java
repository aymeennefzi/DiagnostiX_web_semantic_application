package com.backend.backend_application.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllergyDTO {
    private String name;
    private String severityLevel;
    private String lastOccurrence;
}
