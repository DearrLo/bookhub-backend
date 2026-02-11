package com.bookhub.controller;


import com.bookhub.bll.EmpruntService;
import com.bookhub.bo.Emprunt;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/loans")
public class EmpruntController {

    private EmpruntService empruntService;

    /**
     * API pour les bibliothécaires : liste tous les prêts en cours dans la bibliothèque.
     * @return 200 OK avec la liste ou 204 No Content.
     */
    @GetMapping
    public ResponseEntity<?> rechercherTousEmpruntsActifs() {
        final List<Emprunt> emprunts = empruntService.affficherEmpruntsActifs();

        if (emprunts == null || emprunts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(emprunts);
    }

    /**
     * API pour l'utilisateur : récupère son propre historique d'emprunts.
     * Identifie l'utilisateur via le jeton de sécurité (JWT).
     * @return 200 OK avec l'historique ou 204 No Content.
     */
    @GetMapping("/my")
    public ResponseEntity<List<Emprunt>> listeEmpruntsparUtilisateur() {

        String emailConnecte = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Emprunt> mesEmprunts = empruntService.afficherHistoriqueDEmprunts(emailConnecte);
        if (mesEmprunts == null || mesEmprunts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mesEmprunts);
    }

    /**
     * API POST : Enregistre une nouvelle demande d'emprunt pour un livre.
     * @param emprunt L'objet emprunt contenant l'ID du livre et l'utilisateur.
     * @return 201 Created ou 406 en cas de stock épuisé ou quota atteint.
     */
    @PostMapping
    public ResponseEntity<?> ajoutEmprunt(@Valid @RequestBody Emprunt emprunt) {
        try {
            empruntService.emprunterLivre(emprunt);
            return ResponseEntity.status(HttpStatus.CREATED).body(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    /**
     * API pour valider le retour physique d'un livre.
     * @param id Identifiant de l'emprunt à clôturer.
     * @return 204 No Content en cas de succès ou 406 en cas d'erreur.
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<?> miseAJourEmprunt(@PathVariable("id") Integer id) {
        try {
            Emprunt emprunt = new Emprunt();
            emprunt.setId(id);
            empruntService.validerRetourLivre(emprunt);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
