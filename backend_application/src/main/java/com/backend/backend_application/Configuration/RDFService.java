package com.backend.backend_application.Configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class RDFService {
    @Bean
    public Model rdfModel() {
        Model model = ModelFactory.createDefaultModel();
        String rdfFileName = "HealthDesase.owl";
        InputStream in = RDFService.class.getResourceAsStream("/" + rdfFileName);
        if (in == null) {
            throw new RuntimeException("Fichier RDF introuvable : " + rdfFileName);
        }
        model.read(in, null);
        return model;
    }
}

