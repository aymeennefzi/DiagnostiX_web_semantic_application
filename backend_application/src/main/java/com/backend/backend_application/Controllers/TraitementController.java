package com.backend.backend_application.Controllers;

import com.backend.backend_application.DTO.TraitementDTO;
import lombok.RequiredArgsConstructor;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin("*")
@RequestMapping(path = "traitement", produces = "application/json")
@RequiredArgsConstructor
@RestController
public class TraitementController {

    private static final String FUSEKI_ENDPOINT = "http://localhost:3030/HealthDisease/";
    private static final String NAMESPACE = "http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#";

    // GET ALL TREATMENTS
    @GetMapping
    public ResponseEntity<?> getAllTraitements() {
        try {
            String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX onto: <" + NAMESPACE + ">\n" +
                    "SELECT ?traitement ?description WHERE {\n" +
                    "  ?traitement rdf:type onto:Traitement .\n" +
                    "  OPTIONAL { ?traitement onto:description ?description }\n" +
                    "}";

            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_ENDPOINT + "query", query);

            ResultSet results = qexec.execSelect();
            List<TraitementDTO> traitements = new ArrayList<>();

            while (results.hasNext()) {
                QuerySolution solution = results.next();
                TraitementDTO traitement = new TraitementDTO();
                String traitementUri = solution.getResource("traitement").getURI();
                traitement.setName(traitementUri.substring(traitementUri.indexOf('#') + 1));

                if (solution.contains("description")) {
                    traitement.setDescription(solution.getLiteral("description").getString());
                }
                traitements.add(traitement);
            }

            qexec.close();
            return ResponseEntity.ok(traitements);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching treatments: " + e.getMessage());
        }
    }

    // ADD NEW TREATMENT WITH DESCRIPTION
    @PostMapping
    public ResponseEntity<String> addTraitement(@RequestBody TraitementDTO traitement) {
        try {
            String description = traitement.getDescription() != null ?
                    "  onto:" + traitement.getName() + " onto:description \"" + traitement.getDescription() + "\" .\n" : "";

            UpdateRequest updateRequest = UpdateFactory.create(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX onto: <" + NAMESPACE + ">\n" +
                            "INSERT DATA {\n" +
                            "  onto:" + traitement.getName() + " rdf:type onto:Traitement .\n" +
                            "  onto:" + traitement.getName() + " rdf:type owl:NamedIndividual .\n" +
                            description +
                            "}"
            );

            UpdateProcessor processor = UpdateExecutionFactory.createRemote(
                    updateRequest, FUSEKI_ENDPOINT + "update");
            processor.execute();

            return ResponseEntity.ok("Treatment added successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding treatment: " + e.getMessage());
        }
    }

    // UPDATE TREATMENT WITH DESCRIPTION
    @PutMapping("/{oldName}")
    public ResponseEntity<String> updateTraitement(
            @PathVariable String oldName,
            @RequestBody TraitementDTO traitement) {
        try {
            UpdateRequest updateRequest = UpdateFactory.create(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX onto: <" + NAMESPACE + ">\n" +
                            "DELETE {\n" +
                            "  onto:" + oldName + " rdf:type onto:Traitement .\n" +
                            "  onto:" + oldName + " rdf:type owl:NamedIndividual .\n" +
                            "  onto:" + oldName + " onto:description ?oldDesc .\n" +
                            "}\n" +
                            "INSERT {\n" +
                            "  onto:" + traitement.getName() + " rdf:type onto:Traitement .\n" +
                            "  onto:" + traitement.getName() + " rdf:type owl:NamedIndividual .\n" +
                            (traitement.getDescription() != null ?
                                    "  onto:" + traitement.getName() + " onto:description \"" + traitement.getDescription() + "\" .\n" : "") +
                            "}\n" +
                            "WHERE {\n" +
                            "  onto:" + oldName + " rdf:type onto:Traitement .\n" +
                            "  onto:" + oldName + " rdf:type owl:NamedIndividual .\n" +
                            "  OPTIONAL { onto:" + oldName + " onto:description ?oldDesc }\n" +
                            "}"
            );

            UpdateProcessor processor = UpdateExecutionFactory.createRemote(
                    updateRequest, FUSEKI_ENDPOINT + "update");
            processor.execute();

            return ResponseEntity.ok("Treatment updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating treatment: " + e.getMessage());
        }
    }

    // DELETE TREATMENT
    @DeleteMapping("/{traitementName}")
    public ResponseEntity<String> deleteTraitement(@PathVariable String traitementName) {
        try {
            UpdateRequest updateRequest = UpdateFactory.create(
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                            "PREFIX onto: <" + NAMESPACE + ">\n" +
                            "DELETE {\n" +
                            "  onto:" + traitementName + " ?p ?o .\n" +
                            "}\n" +
                            "WHERE {\n" +
                            "  onto:" + traitementName + " ?p ?o .\n" +
                            "  onto:" + traitementName + " rdf:type onto:Traitement .\n" +
                            "}"
            );

            UpdateProcessor processor = UpdateExecutionFactory.createRemote(
                    updateRequest, FUSEKI_ENDPOINT + "update");
            processor.execute();

            return ResponseEntity.ok("Treatment deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting treatment: " + e.getMessage());
        }
    }
}