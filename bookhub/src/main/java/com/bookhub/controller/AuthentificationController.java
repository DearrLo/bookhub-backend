package com.bookhub.controller;

import com.bookhub.bo.Utilisateur;
import com.bookhub.bll.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthentificationController {

    private UtilisateurService utilisateurService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur) {
        try {
            utilisateurService.ajouterUtilisateur(utilisateur);
            return ResponseEntity.ok("Utilisateur enregistré avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur loginRequest) {
//        TODO gérer JWT
        return ResponseEntity.ok("Connexion réussie pour : " + loginRequest.getEmail());
    }
}