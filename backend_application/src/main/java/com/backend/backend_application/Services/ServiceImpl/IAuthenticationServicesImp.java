package com.backend.backend_application.Services.ServiceImpl;


import com.backend.backend_application.Entites.*;
import com.backend.backend_application.Repository.IAdherentRepository;
import com.backend.backend_application.Repository.IUserRepository;
import com.backend.backend_application.Services.IServices.IAuthenticationServices;
import com.backend.backend_application.Services.IServices.IJWTServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
@Slf4j
@RequiredArgsConstructor
public class IAuthenticationServicesImp implements IAuthenticationServices {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final IJWTServices jwtServices;

    private final IAdherentRepository adherentRepository;

    @Override
    public Adherent RegisterAdherent(Adherent adherent) {

        adherent.setPassword(passwordEncoder.encode(adherent.getPassword()));
        adherent.setRole(Role.ADHERENT);
        return adherentRepository.save(adherent);


    }

    @Override
    public AuthenticationResponse login(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        var jwt = jwtServices.generateToken(user);
        var refreshToken = jwtServices.generateRefreshToken(new HashMap<>(), user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        authenticationResponse.setAccessToken(jwt);
        authenticationResponse.setRefreshToken(refreshToken);

        if (user.getRole() == Role.ADHERENT) {
            Adherent adherent = (Adherent) user;
            Adherent adherentDto = convertToAdherentDto(adherent);
            authenticationResponse.setUserDetails(adherentDto);

        } else {
            User userDetails = convertToUserDto(user);
            authenticationResponse.setUserDetails(userDetails);
        }

        return authenticationResponse;
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken) {
        String userEmail = jwtServices.extractUsername(refreshToken.getRefreshToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if(jwtServices.isTokenValid(refreshToken.getRefreshToken(), user)) {
            var jwt = jwtServices.generateToken(user);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse();

            authenticationResponse.setAccessToken(jwt);
            authenticationResponse.setRefreshToken(refreshToken.getRefreshToken());
            return authenticationResponse;
        }
        return null;
    }

    @Override
    public HashMap<String, String> forgetPassword(String email) {
        return null;
    }

    @Override
    public HashMap<String, String> resetPassword(String passwordResetToken, String newPassword) {
        return null;
    }




    private User convertToUserDto(User user) {
        User dto = new User();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
      //  dto.setImage(user.getImage());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        dto.setNumeroTelephone(user.getNumeroTelephone());
    return dto;
    }
    private Adherent convertToAdherentDto(Adherent adherent) {
        Adherent dto = new Adherent();
        dto.setId(adherent.getId());
        dto.setNom(adherent.getNom());
        dto.setPrenom(adherent.getPrenom());
        dto.setEmail(adherent.getEmail());
        dto.setPassword(adherent.getPassword());
        dto.setRole(adherent.getRole());
        dto.setCin(adherent.getCin());
        dto.setNumeroTelephone(adherent.getNumeroTelephone());
        dto.setDateNaissance(adherent.getDateNaissance());
        dto.setImage(adherent.getImage());
        return dto;
    }


}
