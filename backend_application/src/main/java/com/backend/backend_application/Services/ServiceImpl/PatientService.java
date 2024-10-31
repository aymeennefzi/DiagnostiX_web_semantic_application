package com.backend.backend_application.Services.ServiceImpl;

import com.backend.backend_application.DTO.PatientDTO;
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
public class PatientService {

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

    public Map<String, Object> getPatientInfo(String patientName) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> bindings = new ArrayList<>();
        String encodedName = patientName.replace(" ", "_");
        String queryString = String.format(
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "SELECT ?property ?value WHERE {\n" +
                        "    ont:%s ?property ?value .\n" +
                        "}", encodedName);

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

    public ResponseEntity<Map<String, Object>> addPatient(PatientDTO patientDTO) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "INSERT DATA { "
                + "    ont:" + patientDTO.getNom().replace(" ", "_") + " a ont:Patient ; "
                + "    ont:nom \"" + patientDTO.getNom() + "\" ; "
                + "    ont:age \"" + patientDTO.getAge() + "\" ; "
                + "    ont:sexe \"" + patientDTO.getSexe() + "\" ; "
                + "    ont:poids \"" + patientDTO.getPoids() + "\" ; "
                + "    ont:antecedentsMedicaux \"" + String.join(", ", patientDTO.getAntecedentsMedicaux()) + "\" ; "
                + "    ont:dateDiagnostic \"" + patientDTO.getDateDiagnostic() + "\" ; "
                + "    ont:historiqueMedical \"" + String.join(", ", patientDTO.getHistoriqueMedical()) + "\" ; "
                + "    ont:niveauDouleur \"" + patientDTO.getNiveauDouleur() + "\" . "
                + "}";

        UpdateRequest request = UpdateFactory.create(sparqlQuery);
        Map<String, Object> response = new HashMap<>();
        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_UPDATE_URL);
            processor.execute();
            response.put("success", true);
            response.put("message", "Le patient a été ajouté avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Une erreur s'est produite lors de l'ajout du patient : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public List<Map<String, String>> getAllPatients() {
        List<Map<String, String>> patients = new ArrayList<>();
        String queryString =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> " +
                        "SELECT ?patient ?nom ?age ?sexe ?poids ?antecedentsMedicaux ?dateDiagnostic ?historiqueMedical ?niveauDouleur WHERE { " +
                        "    ?patient a ont:Patient; " +
                        "             ont:nom ?nom; " +
                        "             ont:age ?age; " +
                        "             ont:sexe ?sexe; " +
                        "             ont:poids ?poids; " +
                        "             ont:antecedentsMedicaux ?antecedentsMedicaux; " +
                        "             ont:dateDiagnostic ?dateDiagnostic; " +
                        "             ont:historiqueMedical ?historiqueMedical; " +
                        "             ont:niveauDouleur ?niveauDouleur. " +
                        "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> patientData = new HashMap<>();
                patientData.put("patient", soln.getResource("patient").toString());
                patientData.put("nom", soln.getLiteral("nom").getString());
                patientData.put("age", soln.getLiteral("age").getString());
                patientData.put("sexe", soln.getLiteral("sexe").getString());
                patientData.put("poids", soln.getLiteral("poids").getString());
                patientData.put("antecedentsMedicaux", soln.getLiteral("antecedentsMedicaux").getString());
                patientData.put("dateDiagnostic", soln.getLiteral("dateDiagnostic").getString());
                patientData.put("historiqueMedical", soln.getLiteral("historiqueMedical").getString());
                patientData.put("niveauDouleur", soln.getLiteral("niveauDouleur").getString());
                patients.add(patientData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }

    public ResponseEntity<Map<String, Object>> deletePatient(String patientName) {
        String sparqlQuery =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?patient ?p ?o.\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?patient a ont:Patient;\n" +
                        "             ont:nom \"" + patientName + "\";\n" +
                        "             ?p ?o.\n" +
                        "}";

        Map<String, Object> response = new HashMap<>();
        UpdateRequest request = UpdateFactory.create(sparqlQuery);
        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_UPDATE_URL);
            processor.execute();
            response.put("message", "Le patient avec le nom \"" + patientName + "\" a été supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Une erreur s'est produite lors de la suppression du patient : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public Map<String, Object> updatePatientByName(String name, PatientDTO patientDTO) {
        String sparqlUpdate =
                "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#>\n" +
                        "DELETE {\n" +
                        "    ?patient ont:age ?oldAge;\n" +
                        "             ont:sexe ?oldSexe;\n" +
                        "             ont:poids ?oldPoids;\n" +
                        "             ont:antecedentsMedicaux ?oldAntecedents;\n" +
                        "             ont:dateDiagnostic ?oldDate;\n" +
                        "             ont:historiqueMedical ?oldHistorique;\n" +
                        "             ont:niveauDouleur ?oldNiveauDouleur.\n" +
                        "}\n" +
                        "INSERT {\n" +
                        "    ?patient ont:age \"" + patientDTO.getAge() + "\";\n" +
                        "             ont:sexe \"" + patientDTO.getSexe() + "\";\n" +
                        "             ont:poids \"" + patientDTO.getPoids() + "\";\n" +
                        "             ont:antecedentsMedicaux \"" + String.join(", ", patientDTO.getAntecedentsMedicaux()) + "\";\n" +
                        "             ont:dateDiagnostic \"" + patientDTO.getDateDiagnostic() + "\";\n" +
                        "             ont:historiqueMedical \"" + String.join(", ", patientDTO.getHistoriqueMedical()) + "\";\n" +
                        "             ont:niveauDouleur \"" + patientDTO.getNiveauDouleur() + "\".\n" +
                        "}\n" +
                        "WHERE {\n" +
                        "    ?patient a ont:Patient;\n" +
                        "             ont:nom \"" + name + "\";\n" +
                        "             ont:age ?oldAge;\n" +
                        "             ont:sexe ?oldSexe;\n" +
                        "             ont:poids ?oldPoids;\n" +
                        "             ont:antecedentsMedicaux ?oldAntecedents;\n" +
                        "             ont:dateDiagnostic ?oldDate;\n" +
                        "             ont:historiqueMedical ?oldHistorique;\n" +
                        "             ont:niveauDouleur ?oldNiveauDouleur.\n" +
                        "}";

        UpdateRequest request = UpdateFactory.create(sparqlUpdate);
        Map<String, Object> response = new HashMap<>();
        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_UPDATE_URL);
            processor.execute();
            response.put("message", "Le patient \"" + name + "\" a été mis à jour avec succès.");
            return response;
        } catch (Exception e) {
            response.put("error", "Une erreur s'est produite lors de la mise à jour : " + e.getMessage());
            return response;
        }
    }
}
