package com.bookhub.controller;

import com.bookhub.bll.UtilisateurService;
import com.bookhub.bo.Utilisateur;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        String email = authentication.getName(); 
        Utilisateur u = utilisateurService.trouverParEmail(email);

        // on renvoie sans mot de passe
        return ResponseEntity.ok(Map.of(
                "email", u.getEmail(),
                "prenom", u.getPrenom(),
                "nom", u.getNom(),
                "pseudo", u.getPseudo(),
                "role", u.getRole()
        ));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMe(@RequestBody Map<String, Object> body,
                                     Authentication authentication) {
        String email = authentication.getName();

        Utilisateur u = utilisateurService.trouverParEmail(email);

        if (body.containsKey("prenom")) u.setPrenom((String) body.get("prenom"));
        if (body.containsKey("nom")) u.setNom((String) body.get("nom"));
        if (body.containsKey("pseudo")) u.setPseudo((String) body.get("pseudo"));

        // Nouveau mdp
        if (body.containsKey("newPassword")) {
            u.setMotDePasse((String) body.get("newPassword"));
        }

        Utilisateur updated = utilisateurService.modifierUtilisateur(u);

        return ResponseEntity.ok(Map.of(
                "email", updated.getEmail(),
                "prenom", updated.getPrenom(),
                "nom", updated.getNom(),
                "pseudo", updated.getPseudo(),
                "role", updated.getRole()
        ));
    }
}
