package com.backend.backend_application.Services.ServiceImpl;


import com.backend.backend_application.DTO.AllergyDTO;
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
public class AllergyService {
    @Autowired
    private Model model;
    private final String FUSEKI_URL = "http://localhost:3030/HealthDisease";
    private static final String FUSEKI_UPDATE_URL = "http://localhost:3030/HealthDisease";

    private String getLocalName(String uri) {
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.lastIndexOf("#") + 1);
        }
        return uri;
    }

    public Map<String, Object> getAllergyInfo(String allergyName) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> bindings = new ArrayList<>();
        String encodedAllergy = allergyName.replace(" ", "_");

        String queryString = String.format(
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "SELECT ?property ?value WHERE {\n" +
                        "    ont:%s ?property ?value .\n" +
                        "}", encodedAllergy);

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet resultSet = qexec.execSelect();
            while (resultSet.hasNext()) {
                QuerySolution soln = resultSet.nextSolution();
                Map<String, Object> binding = new HashMap<>();
                binding.put("property", getLocalName(soln.get("property").asResource().getURI()));
                binding.put("value", soln.get("value").isLiteral()
                        ? soln.get("value").asLiteral().getString()
                        : getLocalName(soln.get("value").asResource().getURI()));
                bindings.add(binding);
            }
        }

        result.put("results", bindings);
        return result;
    }

    public ResponseEntity<Map<String, Object>> addAllergy(AllergyDTO allergyDTO) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "INSERT DATA { "
                + "    ont:" + allergyDTO.getName().replace(" ", "_") + " a ont:Allergy ; "
                + "    ont:name \"" + allergyDTO.getName() + "\" ; "
                + "    ont:severityLevel \"" + allergyDTO.getSeverityLevel() + "\" ; "
                + "    ont:lastOccurrence \"" + allergyDTO.getLastOccurrence() + "\" . "
                + "}";

        UpdateRequest request = UpdateFactory.create(sparqlQuery);
        Map<String, Object> response = new HashMap<>();

        try {
            // Use POST request to execute the SPARQL update
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();

            response.put("success", true);
            response.put("message", "Allergy added successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error adding allergy: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public List<Map<String, String>> getAllAllergies() {
        List<Map<String, String>> allergies = new ArrayList<>();

        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?allergy ?name ?severityLevel ?lastOccurrence WHERE { " +
                        "    ?allergy a ont:Allergy; " +
                        "             ont:name ?name; " +
                        "             ont:severityLevel ?severityLevel; " +
                        "             ont:lastOccurrence ?lastOccurrence. " +
                        "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> allergyData = new HashMap<>();

                allergyData.put("allergy", soln.getResource("allergy").toString());
                allergyData.put("name", soln.getLiteral("name").getString());
                allergyData.put("severityLevel", soln.getLiteral("severityLevel").getString());
                allergyData.put("lastOccurrence", soln.getLiteral("lastOccurrence").getString());

                allergies.add(allergyData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allergies;
    }

    public ResponseEntity<Map<String, Object>> deleteAllergy(String allergyName) {
        String sparqlQuery =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?allergy ?p ?o.\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?allergy a ont:Allergy;\n" +
                        "             ont:name \"" + allergyName + "\";\n" +
                        "             ?p ?o.\n" +
                        "}";

        Map<String, Object> response = new HashMap<>();
        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_UPDATE_URL);
            processor.execute();

            response.put("message", "Allergy \"" + allergyName + "\" deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error deleting allergy: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public Map<String, Object> updateAllergyByName(String name, AllergyDTO allergyDTO) {
        String sparqlUpdate =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?allergy ont:severityLevel ?oldSeverityLevel;\n" +
                        "             ont:lastOccurrence ?oldLastOccurrence.\n" +
                        "}\n" +
                        "INSERT {\n" +
                        "    ?allergy ont:severityLevel \"" + allergyDTO.getSeverityLevel() + "\";\n" +
                        "             ont:lastOccurrence \"" + allergyDTO.getLastOccurrence() + "\".\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?allergy a ont:Allergy;\n" +
                        "             ont:name \"" + name + "\";\n" +
                        "             ont:severityLevel ?oldSeverityLevel;\n" +
                        "             ont:lastOccurrence ?oldLastOccurrence.\n" +
                        "}";

        UpdateRequest request = UpdateFactory.create(sparqlUpdate);
        Map<String, Object> responseMap = new HashMap<>();

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();
            responseMap.put("message", "Allergy updated successfully.");
            responseMap.put("updatedAllergy", allergyDTO);
        } catch (Exception e) {
            responseMap.put("message", "Error updating allergy: " + e.getMessage());
            responseMap.put("error", true);
        }

        return responseMap;
    }

    public Map<String, Object> getAllergyByName(String allergyName) {
        String sparqlQuery =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?severityLevel ?lastOccurrence " +
                        "WHERE { " +
                        "    ?allergy a ont:Allergy ; " +
                        "             ont:name \"" + allergyName + "\" ; " +
                        "             ont:severityLevel ?severityLevel ; " +
                        "             ont:lastOccurrence ?lastOccurrence . " +
                        "}";

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, query)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, Object> allergyMap = new HashMap<>();
                allergyMap.put("severityLevel", soln.getLiteral("severityLevel").getString());
                allergyMap.put("lastOccurrence", soln.getLiteral("lastOccurrence").getString());
                return allergyMap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}