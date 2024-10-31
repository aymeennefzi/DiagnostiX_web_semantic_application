package com.backend.backend_application.Services.ServiceImpl;

import com.backend.backend_application.DTO.TipDTO;
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
public class TipService {
    @Autowired
    private Model model;
    private final String FUSEKI_URL = "http://localhost:3030/HealthDisease";

    private String getLocalName(String uri) {
        if (uri != null && uri.contains("#")) {
            return uri.substring(uri.lastIndexOf("#") + 1);
        }
        return uri;
    }

    private boolean isTipCibleUnique(String cible) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "ASK WHERE { "
                + "    ?tip a ont:Conseil ; "
                + "             ont:aCible \"" + cible + "\" . "
                + "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, sparqlQuery)) {
            return !qexec.execAsk();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResponseEntity<Map<String, Object>> addTip(TipDTO tipDTO) {
        Map<String, Object> response = new HashMap<>();

        // Vérification d'unicité de la cible
        if (!isTipCibleUnique(tipDTO.getCible())) {
            response.put("success", false);
            response.put("message", "Un conseil avec la même cible existe déjà.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "INSERT DATA { "
                + "    ont:" + tipDTO.getCible().replace(" ", "_") + " a ont:Conseil ; "
                + "    ont:aCible \"" + tipDTO.getCible() + "\" . "
                + "}";

        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();

            response.put("success", true);
            response.put("message", "Le conseil a été ajouté avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Une erreur s'est produite lors de l'ajout du conseil : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public Map<String, Object> getTipByCible(String cible) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "SELECT ?cible WHERE { "
                + "    ?tip a ont:Conseil ; "
                + "             ont:aCible \"" + cible + "\" . "
                + "}";

        Query query = QueryFactory.create(sparqlQuery);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, query)) {
            ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, Object> tipMap = new HashMap<>();
                tipMap.put("cible", cible);
                return tipMap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getAllTips() {
        List<Map<String, String>> tips = new ArrayList<>();

        String queryString = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "SELECT ?cible WHERE { "
                + "    ?tip a ont:Conseil ; "
                + "             ont:aCible ?cible . "
                + "}";

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(FUSEKI_URL, queryString)) {
            ResultSet results = qexec.execSelect();
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                Map<String, String> tipData = new HashMap<>();
                tipData.put("cible", soln.getLiteral("cible").getString());
                tips.add(tipData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tips;
    }

    public ResponseEntity<Map<String, Object>> deleteTip(String cible) {
        String sparqlQuery = "PREFIX ont: <http://www.semanticweb.org/omgboomrito1/ontologies/2024/8/untitled-ontology-12#> "
                + "DELETE { ?tip ?p ?o. } "
                + "WHERE { "
                + "    ?tip a ont:Conseil ; "
                + "             ont:aCible \"" + cible + "\" ; "
                + "             ?p ?o . "
                + "}";

        Map<String, Object> response = new HashMap<>();
        UpdateRequest request = UpdateFactory.create(sparqlQuery);

        try {
            UpdateProcessor processor = UpdateExecutionFactory.createRemote(request, FUSEKI_URL);
            processor.execute();

            response.put("message", "Le conseil avec la cible \"" + cible + "\" a été supprimé avec succès.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Une erreur s'est produite lors de la suppression du conseil : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}