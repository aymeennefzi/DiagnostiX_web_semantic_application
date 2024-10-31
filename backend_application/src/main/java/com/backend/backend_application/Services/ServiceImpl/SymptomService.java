package com.backend.backend_application.Services.ServiceImpl;

import DTO.SymptomeDto;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SymptomService {
    @Autowired
    private Model model;
    private final String FUSEKI_URL = "http://localhost:3030/HealthDisease";

    private String getLocalName(String uri) {
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.lastIndexOf("#") + 1);
        }
        return uri;
    }

    private boolean isSymptomNameUnique(String symptomName) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "ASK WHERE { "
                + "    ?symptom a ont:Symptome ; "
                + "             ont:Nom \"" + symptomName + "\" . "
                + "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, sparqlQuery)) {
            return !qexec.execAsk();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<Map<String, Object>> addSymptom(SymptomeDto symptomDTO) {
        Map<String, Object> response = new HashMap<>();

        // Vérification d'unicité du nom
        if (!isSymptomNameUnique(symptomDTO.getNom())) {
            response.put("success", false);
            response.put("message", "Un symptôme avec le même nom existe déjà.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "INSERT DATA { "
                + "    ont:" + symptomDTO.getNom().replace(" ", "_") + " a ont:Symptome ; "
                + "    ont:Nom \"" + symptomDTO.getNom() + "\" ; "
                + "    ont:aDescription \"" + symptomDTO.getDescription() + "\" . "
                + "}";

        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();

            response.put("success", true);
            response.put("message", "Le symptôme a été ajouté avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Une erreur s'est produite lors de l'ajout du symptôme : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public Map<String, Object> getSymptomByName(String symptomName) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "SELECT ?description WHERE { "
                + "    ?symptom a ont:Symptome ; "
                + "             ont:Nom \"" + symptomName + "\" ; "
                + "             ont:aDescription ?description . "
                + "}";

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, query)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, Object> symptomMap = new HashMap<>();
                symptomMap.put("nom", symptomName);
                symptomMap.put("description", soln.getLiteral("description").getString());
                return symptomMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getAllSymptoms() {
        List<Map<String, String>> symptoms = new ArrayList<>();

        String queryString = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "SELECT ?name ?description WHERE { "
                + "    ?symptom a ont:Symptome ; "
                + "             ont:Nom ?name ; "
                + "             ont:aDescription ?description . "
                + "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> symptomData = new HashMap<>();
                symptomData.put("nom", soln.getLiteral("name").getString());
                symptomData.put("description", soln.getLiteral("description").getString());
                symptoms.add(symptomData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return symptoms;
    }

    public ResponseEntity<Map<String, Object>> deleteSymptom(String symptomName) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "DELETE { ?symptom ?p ?o. } "
                + "WHERE { "
                + "    ?symptom a ont:Symptome ; "
                + "             ont:Nom \"" + symptomName + "\" ; "
                + "             ?p ?o . "
                + "}";

        Map<String, Object> response = new HashMap<>();
        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();

            response.put("message", "Le symptôme avec le nom \"" + symptomName + "\" a été supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Une erreur s'est produite lors de la suppression du symptôme : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
