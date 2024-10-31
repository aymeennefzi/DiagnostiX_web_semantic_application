package com.backend.backend_application.Services.ServiceImpl;

import com.backend.backend_application.DTO.DiseaseDTO;
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
public class DiseaseService {
    @Autowired
    private Model model;
    private final String FUSEKI_URL = "http://localhost:3030/HealthDisease";
    private static final String FUSEKI_UPDATE_URL  = "http://localhost:3030/HealthDisease";
    private String getLocalName(String uri) {
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.lastIndexOf("#") + 1);
        }
        return uri;
    }
    public Map<String, Object> getDiseaseInfo(String disease) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> bindings = new ArrayList<>();
        String encodedDisease = disease.replace(" ", "_");

        String queryString = String.format(
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "SELECT ?property ?value WHERE {\n" +
                        "    ont:%s ?property ?value .\n" +
                        "}", encodedDisease);

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
    public ResponseEntity<Map<String, Object>> addDisease(DiseaseDTO diseaseDTO) {
    // Construction de la requête SPARQL
    String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
            + "INSERT DATA { "
            + "    ont:" + diseaseDTO.getName().replace(" ", "_") + " a ont:Disease ; "  // URI unique pour la maladie
            + "    ont:name \"" + diseaseDTO.getName() + "\" ; "  // Nom de la maladie
            + "    ont:description \"" + diseaseDTO.getDescription() + "\" ; "  // Description
            + "    ont:treatments \"" + String.join(", ", diseaseDTO.getTreatments()) + "\" ; "  // Traitements
            + "    ont:symptoms \"" + String.join(", ", diseaseDTO.getSymptoms()) + "\" . "  // Symptômes
            + "}";

    // Création de la requête d'insertion
    UpdateRequest request = UpdateFactory.create(sparqlQuery);

    Map<String, Object> response = new HashMap<>(); // Création de la réponse

    try {
        // Exécution de la requête sur l'endpoint SPARQL
        UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
        processor.execute();

        // Si l'exécution est réussie, on prépare la réponse de succès
        response.put("success", true);
        response.put("message", "La maladie a été ajoutée avec succès.");
        return ResponseEntity.ok(response); // Retourne 200 OK avec la réponse JSON
    } catch (Exception e) {
        // Si une erreur se produit, on prépare la réponse d'erreur
        response.put("success", false);
        response.put("message", "Une erreur s'est produite lors de l'ajout de la maladie : " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Retourne 500 avec la réponse JSON
    }
}
    public List<Map<String, String>> getAllDiseases() {
        List<Map<String, String>> diseases = new ArrayList<>();

        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?disease ?name ?description ?treatments ?symptoms WHERE { " +
                        "    ?disease a ont:Disease; " +
                        "             ont:name ?name; " +
                        "             ont:description ?description; " +
                        "             ont:treatments ?treatments; " +
                        "             ont:symptoms ?symptoms. " +
                        "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> diseaseData = new HashMap<>();

                diseaseData.put("disease", soln.getResource("disease").toString());
                diseaseData.put("name", soln.getLiteral("name").getString());
                diseaseData.put("description", soln.getLiteral("description").getString());
                diseaseData.put("treatments", soln.getLiteral("treatments").getString());
                diseaseData.put("symptoms", soln.getLiteral("symptoms").getString());

                diseases.add(diseaseData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diseases;
    }
    public ResponseEntity<Map<String, Object>> deleteDisease(String diseaseName) {
        String sparqlQuery =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?disease ?p ?o.\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?disease a ont:Disease;\n" +
                        "             ont:name \"" + diseaseName + "\";\n" +
                        "             ?p ?o.\n" +
                        "}";

        Map<String, Object> response = new HashMap<>();
        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_UPDATE_URL);
            processor.execute();

            response.put("message", "La maladie avec le nom \"" + diseaseName + "\" a été supprimée avec succès.");
            return ResponseEntity.ok(response); // HTTP 200 OK
        } catch (Exception e) {
            response.put("error", "Une erreur s'est produite lors de la suppression de la maladie : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // HTTP 500 Internal Server Error
        }
    }
    public Map<String, Object> updateDiseaseByName(String name, DiseaseDTO diseaseDTO) {
        // Construction de la requête SPARQL pour la mise à jour
        String sparqlUpdate =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?disease ont:description ?oldDescription;\n" +
                        "             ont:treatments ?oldTreatments;\n" +
                        "             ont:symptoms ?oldSymptoms.\n" +
                        "}\n" +
                        "INSERT {\n" +
                        "    ?disease ont:description \"" + diseaseDTO.getDescription() + "\";\n" +
                        "             ont:treatments \"" + String.join(", ", diseaseDTO.getTreatments()) + "\";\n" +
                        "             ont:symptoms \"" + String.join(", ", diseaseDTO.getSymptoms()) + "\".\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?disease a ont:Disease;\n" +
                        "             ont:name \"" + name + "\";\n" +
                        "             ont:description ?oldDescription;\n" +
                        "             ont:treatments ?oldTreatments;\n" +
                        "             ont:symptoms ?oldSymptoms.\n" +
                        "}";

        UpdateRequest request = UpdateFactory.create(sparqlUpdate);
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Exécution de la requête SPARQL
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();
            responseMap.put("message", "Maladie mise à jour avec succès.");
            responseMap.put("updatedDisease", diseaseDTO);  // Optionnel : renvoie les nouvelles données de la maladie
        } catch (Exception e) {
            responseMap.put("message", "Une erreur s'est produite lors de la mise à jour de la maladie : " + e.getMessage());
            responseMap.put("error", true);
        }

        return responseMap;
    }
    public Map<String, Object> getDiseaseByName(String diseaseName) {
        String sparqlQuery =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?description ?treatments ?symptoms " +
                        "WHERE { " +
                        "    ?disease a ont:Disease ; " +
                        "             ont:name \"" + diseaseName + "\" ; " +
                        "             ont:description ?description ; " +
                        "             ont:treatments ?treatments ; " +
                        "             ont:symptoms ?symptoms . " +
                        "}";

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, query)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, Object> diseaseMap = new HashMap<>();
                diseaseMap.put("description", soln.getLiteral("description").getString());
                diseaseMap.put("treatments", soln.getLiteral("treatments").getString());
                diseaseMap.put("symptoms", soln.getLiteral("symptoms").getString());
                return diseaseMap;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
