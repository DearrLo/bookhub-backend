package com.bookhub.controller;

import com.bookhub.bll.CommentaireService;
import com.bookhub.bo.Commentaire;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentaireController {

    private CommentaireService commentaireService;

    @PostMapping("books/{id}/ratings")
    public ResponseEntity<?> ajoutCommentaire(@Valid @RequestBody Commentaire commentaire) {
        try {
            commentaireService.laisserCommentaire(commentaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(commentaire);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/ratings/{id}")
    public ResponseEntity<?> miseAJourCommentaire(@RequestBody Commentaire commentaire) {
        try {
            if (commentaire == null || commentaire.getId() == null || commentaire.getId() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("Le commentaire et l'identifiant sont obligatoires");
            }
            Commentaire commentaireMisAJour = commentaireService.modifierCommentaire(commentaire);
            return ResponseEntity.ok(commentaireMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @DeleteMapping("/ratings/{id}")
    public ResponseEntity<?> deleteCommentaire(@PathVariable("id") String idInPath) {
        try {
            final int idCommentaire = Integer.parseInt(idInPath);
            commentaireService.supprimerCommentaire(idCommentaire);
            return ResponseEntity.ok("Commentaire (" + idCommentaire + ") est supprimé");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
