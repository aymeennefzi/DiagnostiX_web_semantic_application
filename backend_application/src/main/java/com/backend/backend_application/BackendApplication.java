package com.backend.backend_application;

import com.backend.backend_application.Entites.Admin;
import com.backend.backend_application.Entites.Role;
import com.backend.backend_application.Entites.User;
import com.backend.backend_application.Repository.IAdminRepository;
import com.backend.backend_application.Repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@EnableAspectJAutoProxy
@RequiredArgsConstructor
@ComponentScan(basePackages={"com.backend.backend_application" ,"com.backend.backend_application.CorsCongiguration"})
public class BackendApplication implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;

    @Autowired
    private RestTemplate restTemplate;
    private final String FUSEKI_URL = "http://localhost:3030/HealthDisease/data";

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepository.findByRole(Role.ADMIN);
        if (adminAccount == null) {
            Admin admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setNom("admin");
            admin.setPrenom("admin");
            admin.setRole(Role.ADMIN);
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            adminRepository.save(admin);
        }
        loadRdfFile();
    }

    private void loadRdfFile() {
        try {
            Path path = Path.of("src/main/resources/HealthDesase.owl");
            byte[] fileContent = Files.readAllBytes(path);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/rdf+xml"));
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileContent, headers);

            String fusekiUrl = "http://localhost:3030/HealthDisease/data";
            ResponseEntity<String> response = restTemplate.exchange(fusekiUrl, HttpMethod.POST, requestEntity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Fichier chargé avec succès !");
            } else {
                System.out.println("Erreur lors du chargement du fichier : " + response.getStatusCode());
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement : " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution : " + e.getMessage());
        }
    }

}