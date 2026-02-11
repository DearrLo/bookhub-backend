package com.bookhub.controller;

import com.bookhub.bll.UtilisateurService;
import com.bookhub.bo.Utilisateur;
import com.bookhub.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthentificationController {

    private UtilisateurService utilisateurService;

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    /**
     * Point d'entrée pour la création de compte (Self-service).
     * @param utilisateur Objet JSON contenant les données d'inscription.
     * @return 200 OK en cas de succès, 400 Bad Request avec le motif en cas d'erreur.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        try {
            utilisateurService.ajouterUtilisateur(utilisateur);
            return ResponseEntity.ok("Utilisateur enregistré avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Point d'entrée pour l'authentification sécurisée.
     * Vérifie les identifiants et retourne un jeton JWT si l'authentification réussit.
     * @param loginRequest Objet contenant l'email et le mot de passe en clair.
     * @return Un objet Map contenant la clé "token", ou 401 Unauthorized.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur loginRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getMotDePasse())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }
    }
}
