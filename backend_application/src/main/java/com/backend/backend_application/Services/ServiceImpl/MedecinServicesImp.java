package com.backend.backend_application.Services.ServiceImpl;


import com.backend.backend_application.DTO.MedecinDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.query.*;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MedecinServicesImp {
    private static final String FUSEKI_URL = "http://localhost:3030/HealthDisease";
    private static final String FUSEKI_UPDATE_URL = "http://localhost:3030/HealthDisease/update";
    private static final String ONTOLOGY_PREFIX = "http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#";


    public List<Map<String, String>> getAllMedecins() {
        List<Map<String, String>> medecinsList = new ArrayList<>();

        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?medicin ?name ?specialization ?location WHERE { " +
                        "    ?medicin a ont:Medecin; " +
                        "             ont:nom ?name; " +
                        "             ont:specialite ?specialization; " +
                        "             ont:localisation ?location. " +
                        "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> medicinData = new HashMap<>();

                medicinData.put("medicin", soln.getResource("medicin").toString());
                medicinData.put("name", soln.getLiteral("name").getString());
                medicinData.put("specialization", soln.getLiteral("specialization").getString());
                medicinData.put("location", soln.getLiteral("location").getString());

                medecinsList.add(medicinData); // Add the map to the list
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return medecinsList; // Return the list of maps
    }

    public String addMedicin(MedecinDTO medicinDTO) {
        String medicinName = medicinDTO.getNom().replace(" ", "_");
        String sparqlInsert = String.format(
                "PREFIX ont: <%s>\n" +
                        "INSERT DATA {\n" +
                        "    ont:%s a ont:Medecin ;\n" +
                        "        ont:nom \"%s\" ;\n" +
                        "        ont:specialite \"%s\" ;\n" +
                        "        ont:localisation \"%s\" .\n" +
                        "}",
                ONTOLOGY_PREFIX, medicinName, medicinDTO.getNom(), medicinDTO.getSpecialite(), medicinDTO.getLocalisation()
        );

        try {
            // Exécution de la requête d'insertion dans Fuseki
            UpdateRequest updateRequest = UpdateFactory.create(sparqlInsert);
            UpdateProcessor updateProcessor = UpdateExecutionFactory.createRemote(updateRequest, FUSEKI_UPDATE_URL);
            updateProcessor.execute();

            return String.format("{\"message\": \"Médecin %s ajouté avec succès\"}", medicinDTO.getNom());
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("{\"error\": \"Erreur lors de l'ajout du médecin : %s\"}", e.getMessage());
        }
    }
    public String deleteMedicinByName(String name) {
        String sparqlDelete = String.format(
                "PREFIX ont: <%s>\n" +
                        "DELETE WHERE { ?medicin a ont:Medecin; ont:nom \"%s\"; ?p ?o . }",
                ONTOLOGY_PREFIX, name
        );

        try {
            UpdateRequest updateRequest = UpdateFactory.create(sparqlDelete);
            UpdateProcessor updateProcessor = UpdateExecutionFactory.createRemote(updateRequest, FUSEKI_UPDATE_URL);
            updateProcessor.execute();

            return String.format("{\"message\": \"Médecin avec le nom '%s' supprimé avec succès\"}", name);
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("{\"error\": \"Erreur lors de la suppression du médecin : %s\"}", e.getMessage());
        }
    }

    public Map<String, Object> updateMedicinByName(String name, MedecinDTO medicinDTO) {
        // Construction de la requête SPARQL pour la mise à jour
        String sparqlUpdate =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?medicin ont:nom ?oldName;\n" +
                        "             ont:specialite ?oldSpecialite;\n" +
                        "             ont:localisation ?oldLocalisation.\n" +
                        "}\n" +
                        "INSERT {\n" +
                        "    ?medicin ont:nom \"" + medicinDTO.getNom() + "\";\n" +
                        "             ont:specialite \"" + medicinDTO.getSpecialite() + "\";\n" +
                        "             ont:localisation \"" + medicinDTO.getLocalisation() + "\".\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?medicin a ont:Medecin;\n" +
                        "             ont:nom \"" + name + "\";\n" +
                        "             ont:specialite ?oldSpecialite;\n" +
                        "             ont:localisation ?oldLocalisation.\n" +
                        "}";

        UpdateRequest request = UpdateFactory.create(sparqlUpdate);
        Map<String, Object> responseMap = new HashMap<>();

        try {
            // Exécution de la requête SPARQL
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();
            responseMap.put("message", "Médecin mis à jour avec succès.");
            responseMap.put("updatedMedicin", medicinDTO);  // Optionnel : renvoie les nouvelles données du médecin
        } catch (Exception e) {
            responseMap.put("message", "Une erreur s'est produite lors de la mise à jour du médecin : " + e.getMessage());
            responseMap.put("error", true);
        }

        return responseMap;
    }


    public Map<String, Object> getMedicinByName(String name) {
        String sparqlQuery = String.format(
                "PREFIX ont: <%s> " +
                        "SELECT ?medicin ?nom ?specialite ?localisation WHERE { " +
                        "    ?medicin a ont:Medecin; " +
                        "             ont:nom \"%s\"; " +
                        "             ont:specialite ?specialite; " +
                        "             ont:localisation ?localisation. " +
                        "}", ONTOLOGY_PREFIX, name
        );

        Map<String, Object> responseMap = new HashMap<>();

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, sparqlQuery)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution solution = results.nextSolution();
                responseMap.put("medicin", solution.getResource("medicin").toString());

                // Retrieve and check for the nom
                if (solution.getLiteral("nom") != null) {
                    responseMap.put("nom", solution.getLiteral("nom").getString());
                } else {
                    // Log the entire solution for debugging
                    System.out.println("Solution without 'nom': " + solution);
                    responseMap.put("nom", "Nom non disponible");
                }

                responseMap.put("specialite", solution.getLiteral("specialite") != null ?
                        solution.getLiteral("specialite").getString() : "Spécialité non disponible");

                responseMap.put("localisation", solution.getLiteral("localisation") != null ?
                        solution.getLiteral("localisation").getString() : "Localisation non disponible");
            } else {
                responseMap.put("message", "Aucun médecin trouvé avec ce nom.");
            }
        } catch (Exception e) {
            responseMap.put("error", true);
            responseMap.put("message", "Une erreur s'est produite lors de la récupération du médecin : " + e.getMessage());
        }

        return responseMap;
    }



}
