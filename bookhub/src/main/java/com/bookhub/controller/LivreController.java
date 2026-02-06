package com.bookhub.controller;

import com.bookhub.bll.LivreService;
import com.bookhub.bo.Livre;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//• GET /api/books
//• GET /api/books/{id}
//• POST /api/books (LIBRARIAN)
//• PUT /api/books/{id} (LIBRARIAN)
//• DELETE /api/books/{id} (ADMIN)


//• GET /api/books/search


@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class LivreController {

    private LivreService livreService;

    @GetMapping
    public ResponseEntity<?> rechercherTousLivres() {
        final List<Livre> livres = livreService.afficherLivres();

        if (livres == null || livres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(livres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> rechercherLivreParId(@PathVariable("id") String idInPath) {

        try {
            int id = Integer.parseInt(idInPath);
            final Livre livre = livreService.afficherDetailLivre(id);
            if (livre == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre introuvable");
            }
            return ResponseEntity.ok(livre);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> ajoutLivre(@Valid @RequestBody Livre livre) {
        try {
            livreService.ajouter(livre);
            return ResponseEntity.status(HttpStatus.CREATED).body(livre);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> miseAJourLivre(@Valid @RequestBody Livre livre) {
        try {
            if (livre == null || livre.getId() == null || livre.getId() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("La livre et l'identifiant sont obligatoires");
            }
            livreService.modifier(livre);
            return ResponseEntity.ok(livre);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLivre(@PathVariable("id") String idInPath) {
        try {
            final int idLivre = Integer.parseInt(idInPath);
            livreService.supprimer(idLivre);
            return ResponseEntity.ok("Livre (" + idLivre + ") est supprimé");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> chercherLivres(
            @RequestParam(required = false) String mot_recherche) {
        try {
            final List<Livre> resultats = livreService.rechercherLivres(mot_recherche);
            if (resultats == null || resultats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resultats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }


}
