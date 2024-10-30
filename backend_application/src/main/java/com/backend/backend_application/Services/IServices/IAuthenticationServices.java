package com.backend.backend_application.Services.IServices;


import com.backend.backend_application.Entites.Adherent;
import com.backend.backend_application.Entites.AuthenticationResponse;
import com.backend.backend_application.Entites.RefreshTokenRequest;

import java.util.HashMap;

public interface IAuthenticationServices {
    Adherent RegisterAdherent(Adherent adherent);
    AuthenticationResponse login(String email, String password);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshToken);
    HashMap<String,String> forgetPassword(String email);
    HashMap<String,String> resetPassword(String passwordResetToken, String newPassword);

}
