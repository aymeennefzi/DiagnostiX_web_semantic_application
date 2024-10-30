package com.backend.backend_application.Controllers;

import com.backend.backend_application.Entites.*;
import com.backend.backend_application.Repository.IAdherentRepository;
import com.backend.backend_application.Services.IServices.IAuthenticationServices;
import com.backend.backend_application.Services.ServiceImpl.SendEmailServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  public static String uploadDirectory = System.getProperty("user.dir") + "/uploadUser";
  private final SendEmailServiceImp sendEmailService;
  private final IAuthenticationServices authenticationServices;
  private final IAdherentRepository adherentRepository;

  @PostMapping("/registerAdhrent")
  public ResponseEntity<Adherent> registerAdherent(@RequestParam("nom") String nom,
                                                   @RequestParam("prenom") String prenom,
                                                   @RequestParam("email") String email,
                                                   @RequestParam("password") String password,
                                                   @RequestParam("numeroTelephone") String numeroTelephone,
                                                   @RequestParam("cin") Long cin,
                                                   @RequestParam("dateNaissance") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateNaissance,
                                                   @RequestParam("image") MultipartFile file) throws IOException {
    Adherent adherent = new Adherent();
    Random random = new Random();
    int verificationCode = 1000 + random.nextInt(9000); // Génère un nombre entre 100000 et 999999
    adherent.setVerificationCode(String.valueOf(verificationCode));


    // Envoyer l'email de vérification
    String emailBody = "Votre code de confirmation est : " + verificationCode ;
    sendEmailService.sendEmail(email, emailBody, "Confirmation de votre compte");
    adherent.setNom(nom);
    adherent.setPrenom(prenom);
    adherent.setEmail(email);
    adherent.setPassword(password);
    adherent.setCin(cin);
    adherent.setNumeroTelephone(numeroTelephone);
    adherent.setDateNaissance(dateNaissance);
    adherent.setVerified(false);
    adherent.setRole(Role.ADHERENT);
    String originalFilename = file.getOriginalFilename();
    String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
    Path fileNameAndPath = Paths.get(uploadDirectory, uniqueFilename);
    if (!Files.exists(fileNameAndPath.getParent())) {
       Files.createDirectories(fileNameAndPath.getParent());
  }
  Files.write(fileNameAndPath, file.getBytes());
    adherent.setImage(uniqueFilename);
    Adherent savedAdherent = authenticationServices.RegisterAdherent(adherent);
    return ResponseEntity.ok(savedAdherent);
  }
  @PostMapping("/verifyUser")
  public ResponseEntity<String> verifyEtudiant(@RequestParam("email") String email,
                                               @RequestParam("code") String code) {
    Adherent adherent = adherentRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    if (adherent != null && adherent.getVerificationCode().equals(code)) {
      adherent.setVerified(true);
      adherentRepository.save(adherent);

      return ResponseEntity.ok("Compte vérifié avec succès.");
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de vérification invalide.");
  }
  @GetMapping("/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws MalformedURLException {

    Path filePath = Paths.get(uploadDirectory).resolve(filename);
    Resource file = new UrlResource(filePath.toUri());

    if (file.exists() || file.isReadable()) {
      return ResponseEntity
              .ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
              .body(file);
    } else {
      throw new RuntimeException("Could not read the file!");
    }
  }

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody User user) {
      return authenticationServices.login(user.getEmail(), user.getPassword());
  }

  @PostMapping("/refreshToken")
  public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
      return authenticationServices.refreshToken(refreshToken);
  }

  @PostMapping("/forgetpassword")
  public HashMap<String,String> forgetPassword(@RequestBody String email){
        return authenticationServices.forgetPassword(email);
  }

    @PostMapping("/resetPassword/{passwordResetToken}")
    public HashMap<String,String> resetPassword(@PathVariable String passwordResetToken, String newPassword){
        return authenticationServices.resetPassword(passwordResetToken, newPassword);
    }


}
